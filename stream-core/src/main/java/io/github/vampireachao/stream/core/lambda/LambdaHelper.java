package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.bean.BeanHelper;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.Opp;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
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
    public static <T extends Serializable> LambdaExecutable resolve(T lambda) {
        Objects.requireNonNull(lambda, "lambda can not be null");
        if (lambda instanceof Proxy) {
            return LambdaExecutable.initProxy((Proxy) lambda);
        }
        return SERIALIZED_LAMBDA_EXECUTABLE_CACHE.computeIfAbsent(lambda.getClass().getName(),
                key -> new LambdaExecutable(serialize(lambda)));
    }

    @SafeVarargs
    public static <T> List<String> getPropertyNames(SerFunc<T, ?>... funcs) {
        return Steam.of(funcs).map(LambdaHelper::getPropertyName).toList();
    }

    public static <T> String getPropertyName(SerFunc<T, ?> func) {
        return Opp.of(func).map(LambdaHelper::resolve).map(LambdaExecutable::getName).map(BeanHelper::getPropertyName).get();
    }

}
