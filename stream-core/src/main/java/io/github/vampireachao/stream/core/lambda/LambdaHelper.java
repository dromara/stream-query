package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.reflect.ReflectHelper;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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

    private static final WeakHashMap<String, LambdaExecutable> SERIALIZED_LAMBDA_EXECUTABLE_CACHE = new WeakHashMap<>();

    private LambdaHelper() {
        /* Do not new me! */
    }

    /**
     * Resolve the lambda to a {@link SerializedLambda} instance.
     *
     * @param lambda The lambda to resolve.
     * @return SerializedLambda
     */
    private static SerializedLambda serialize(Serializable lambda) {
        if (lambda instanceof SerializedLambda) {
            return (SerializedLambda) lambda;
        }
        if (lambda instanceof Proxy) {
            throw new IllegalStateException("Are you debugging?Get out!!!");
        }
        final Class<? extends Serializable> clazz = lambda.getClass();
        if (!clazz.isSynthetic()) {
            throw new IllegalArgumentException("Not a lambda expression: " + clazz.getName());
        }
        try {
            final Method writeReplace = ReflectHelper.accessible(clazz.getDeclaredMethod("writeReplace"));
            final Object maybeSerLambda = writeReplace.invoke(lambda);
            if (Objects.nonNull(maybeSerLambda) && maybeSerLambda instanceof SerializedLambda) {
                return (SerializedLambda) maybeSerLambda;
            }
            throw new IllegalStateException("writeReplace result value is not java.lang.invoke.SerializedLambda");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * Resolve the lambda to a {@link LambdaExecutable} instance.
     *
     * @param lambda The lambda to resolve.
     * @return LambdaExecutable
     */
    public static LambdaExecutable resolve(Serializable lambda) {
        Objects.requireNonNull(lambda, "lambda can not be null");
        return SERIALIZED_LAMBDA_EXECUTABLE_CACHE.computeIfAbsent(lambda.getClass().getName(), key -> {
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
                    if (ReflectHelper.getDescriptor(constructor).equals(serializedLambda.getImplMethodSignature())) {
                        return new LambdaExecutable(constructor);
                    }
                }
            } else {
                List<Method> methods = ReflectHelper.getAllDeclaredMethods(implClass);
                for (Method method : methods) {
                    if (method.getName().equals(methodName)
                            && ReflectHelper.getDescriptor(method).equals(serializedLambda.getImplMethodSignature())) {
                        return new LambdaExecutable(method);
                    }
                }
            }
            throw new IllegalStateException("No lambda method found.");
        });
    }

}
