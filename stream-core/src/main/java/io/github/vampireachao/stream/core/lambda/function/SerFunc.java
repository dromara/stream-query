package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的Function
 *
 * @author VampireAchao
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface SerFunc<T, R> extends Function<T, R>, Serializable {


    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> SerFunc<T, T> identity() {
        return t -> t;
    }

}