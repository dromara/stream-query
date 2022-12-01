package io.github.vampireachao.stream.core.lambda.function;

import io.github.vampireachao.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的Function
 *
 * @author VampireAchao Cizai_

 * @see java.util.function.Function
 */
@FunctionalInterface
public interface SerFunc<T, R> extends Function<T, R>, Serializable {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     * @throws java.lang.Exception if any.
     */
    @SuppressWarnings("all")
    R applying(T t) throws Exception;

    /**
     * Applies this function to the given argument.
     */
    @Override
    default R apply(T t) {
        try {
            return applying(t);
        } catch (Exception e) {
            throw new LambdaInvokeException(e);
        }
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> SerFunc<T, T> identity() {
        return t -> t;
    }

    /**
     * casting identity
     *
     * @param <T> param type
     * @param <R> result type
     * @return identity after casting
     */
    @SuppressWarnings("unchecked")
    static <T, R> Function<T, R> castingIdentity() {
        return t -> (R) t;
    }
}
