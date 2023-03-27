package org.dromara.stream.core.lambda.function;

import org.dromara.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * SerBiFunc
 *
 * @author VampireAchao Cizai_

 * @since 2022/6/8
 */
@FunctionalInterface
public interface SerBiFunc<T, U, R> extends BiFunction<T, U, R>, Serializable {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     * @throws java.lang.Exception if any.
     */
    @SuppressWarnings("all")
    R applying(T t, U u) throws Throwable;

    /**
     * Applies this function to the given arguments.
     */
    @Override
    default R apply(T t, U u) {
        try {
            return this.applying(t, u);
        } catch (Throwable e) {
            throw new LambdaInvokeException(e);
        }
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws java.lang.NullPointerException if after is null
     */
    default <V> SerBiFunc<T, U, V> andThen(SerFunc<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> after.apply(this.apply(t, u));
    }
}

