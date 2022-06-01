package io.github.vampireachao.stream.core.lambda;

import org.apache.flink.api.java.typeutils.TypeExtractionUtils;
import org.apache.flink.shaded.asm9.org.objectweb.asm.Type;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;


/**
 * LambdaHelper
 *
 * @author <achao1441470436@gmail.com>
 * @since 2022/5/29 9:19
 */
public class LambdaHelper {

    private static final String CONSTRUCTOR_METHOD_NAME = "<init>";

    private LambdaHelper() {
        /* Do not new me! */
    }

    private static SerializedLambda serialize(Serializable lambda) {
        Objects.requireNonNull(lambda, "lambda can not be null");
        if (lambda instanceof SerializedLambda) {
            return (SerializedLambda) lambda;
        }
        final Class<? extends Serializable> clazz = lambda.getClass();
        if (!clazz.isSynthetic()) {
            throw new IllegalArgumentException("Not a lambda expression: " + clazz.getName());
        }
        try {
            final Method writeReplace = clazz.getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            final Object maybeSerLambda = writeReplace.invoke(lambda);
            if (Objects.nonNull(maybeSerLambda) && maybeSerLambda instanceof SerializedLambda) {
                return (SerializedLambda) maybeSerLambda;
            }
            throw new IllegalStateException("writeReplace result value is not java.lang.invoke.SerializedLambda");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }


    public static Executable resolve(Serializable lambda) {
        final SerializedLambda serializedLambda = serialize(lambda);
        final String methodName = serializedLambda.getImplMethodName();
        final Class<?> implClass;
        try {
            implClass = Class.forName(serializedLambda.getImplClass().replace("/", "."), true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        if (CONSTRUCTOR_METHOD_NAME.equals(methodName)) {
            for (Constructor<?> constructor : implClass.getDeclaredConstructors()) {
                if (Type.getConstructorDescriptor(constructor).equals(serializedLambda.getImplMethodSignature())) {
                    return constructor;
                }
            }
        } else {
            List<Method> methods = TypeExtractionUtils.getAllDeclaredMethods(implClass);
            for (Method method : methods) {
                if (method.getName().equals(methodName)
                        && Type.getMethodDescriptor(method).equals(serializedLambda.getImplMethodSignature())) {
                    return method;
                }
            }
        }
        throw new IllegalStateException("No lambda method found.");
    }

}
