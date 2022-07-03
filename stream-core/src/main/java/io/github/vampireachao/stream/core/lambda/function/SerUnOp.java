package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * 可序列化的UnaryOperator
 *
 * @author VampireAchao
 * @see java.util.function.UnaryOperator
 */
@FunctionalInterface
public interface SerUnOp<T> extends UnaryOperator<T>, Serializable {

    /**
     * Returns a unary operator that always returns its input argument.
     *
     * @param <T> the type of the input and output of the operator
     * @return a unary operator that always returns its input argument
     */
    static <T> SerUnOp<T> identity() {
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
    static <T, R, F extends Function<T, R>> SerUnOp<T> casting(F function) {
        return t -> (T) function.apply(t);
    }

}