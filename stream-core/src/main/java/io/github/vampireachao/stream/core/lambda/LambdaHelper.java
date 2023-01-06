package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.bean.BeanHelper;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.optional.Opp;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;


/**
 * LambdaHelper
 *
 * @author VampireAchao Cizai_

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
            if (maybeSerLambda instanceof SerializedLambda) {
                return (SerializedLambda) maybeSerLambda;
            }
            throw new IllegalStateException("writeReplace result value is not java.lang.invoke.SerializedLambda");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * Resolve the lambda to a {@link io.github.vampireachao.stream.core.lambda.LambdaExecutable} instance.
     *
     * @param lambda The lambda to resolve.
     * @param <T>    a T class
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

    public static <T> T revert(Class<? super T> clazz, Executable executable) {
        final Method funcMethod = Steam.of(clazz.getMethods()).findFirst(method -> Modifier.isAbstract(method.getModifiers()))
                .orElseThrow(() -> new LambdaInvokeException("not a functional interface"));
        final MethodHandle implMethod;
        final MethodType instantiatedMethodType;
        if (executable instanceof Method) {
            final Method method = (Method) executable;
            implMethod = ((SerSupp<MethodHandle>) () -> MethodHandles.lookup().unreflect(method)).get();
            instantiatedMethodType = MethodType.methodType(method.getReturnType(), method.getDeclaringClass(), method.getParameterTypes());
        } else {
            final Constructor<?> constructor = (Constructor<?>) executable;
            implMethod = ((SerSupp<MethodHandle>) () -> MethodHandles.lookup().unreflectConstructor(constructor)).get();
            instantiatedMethodType = MethodType.methodType(constructor.getDeclaringClass(), constructor.getParameterTypes());
        }
        final CallSite callSite = ((SerSupp<CallSite>) () ->
                Serializable.class.isAssignableFrom(clazz) ?
                        LambdaMetafactory.altMetafactory(
                                MethodHandles.lookup(),
                                funcMethod.getName(),
                                MethodType.methodType(clazz),
                                MethodType.methodType(funcMethod.getReturnType(), funcMethod.getParameterTypes()),
                                implMethod,
                                instantiatedMethodType,
                                LambdaMetafactory.FLAG_SERIALIZABLE
                        ) :
                        LambdaMetafactory.metafactory(
                                MethodHandles.lookup(),
                                funcMethod.getName(),
                                MethodType.methodType(clazz),
                                MethodType.methodType(funcMethod.getReturnType(), funcMethod.getParameterTypes()),
                                implMethod,
                                instantiatedMethodType
                        )
        ).get();
        final MethodHandle target = callSite.getTarget();
        return ((SerSupp<T>) () -> SerFunc.<Object, T>cast().apply(target.invoke())).get();
    }


    /**
     * <p>getPropertyNames.</p>
     *
     * @param funcs a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param <T>   a T class
     * @return a {@link java.util.List} object
     */
    @SafeVarargs
    public static <T> List<String> getPropertyNames(SerFunc<T, ?>... funcs) {
        return Steam.of(funcs).map(LambdaHelper::getPropertyName).toList();
    }

    /**
     * <p>getPropertyName.</p>
     *
     * @param func a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param <T>  a T class
     * @return a {@link java.lang.String} object
     */
    public static <T> String getPropertyName(SerFunc<T, ?> func) {
        return Opp.of(func).map(LambdaHelper::resolve).map(LambdaExecutable::getName).map(BeanHelper::getPropertyName).get();
    }

}
