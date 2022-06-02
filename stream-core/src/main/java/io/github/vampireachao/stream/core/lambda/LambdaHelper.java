package io.github.vampireachao.stream.core.lambda;

import org.apache.flink.api.java.typeutils.TypeExtractionUtils;
import org.apache.flink.shaded.asm9.org.objectweb.asm.Type;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;


/**
 * LambdaHelper
 *
 * @author VampireAchao
 * @since 2022/5/29 9:19
 */
public class LambdaHelper {

    private static final String CONSTRUCTOR_METHOD_NAME = "<init>";

    private static final WeakHashMap<String, SerializedLambda> SERIALIZED_LAMBDA_CACHE = new WeakHashMap<>();

    private LambdaHelper() {
        /* Do not new me! */
    }

    public static SerializedLambda serialize(Serializable lambda) {
        Objects.requireNonNull(lambda, "lambda can not be null");
        if (lambda instanceof SerializedLambda) {
            return (SerializedLambda) lambda;
        }
        if (lambda instanceof Proxy) {
            throw new IllegalStateException("Are you debugging?Get out!!!");
        }
        final Class<? extends Serializable> clazz = lambda.getClass();
        return SERIALIZED_LAMBDA_CACHE.computeIfAbsent(clazz.getName(), key -> {
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
        });
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
