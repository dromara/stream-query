package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * SerBiFunc
 *
 * @author VampireAchao
 * @since 2022/6/8
 */
@FunctionalInterface
public interface SerBiFunc<T, U, R> extends BiFunction<T, U, R>, Serializable {

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
     * @throws NullPointerException if after is null
     */
    default <V> SerBiFunc<T, U, V> andThen(SerFunc<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> after.apply(apply(t, u));
    }
}

