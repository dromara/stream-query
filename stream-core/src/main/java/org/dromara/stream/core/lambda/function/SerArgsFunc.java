package org.dromara.stream.core.lambda.function;

import org.dromara.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.Objects;

/**
 * 可序列化的SerArgsFunc
 *
 * @author VampireAchao Cizai_

 * @see SerArgsFunc
 */
@SuppressWarnings("all")
@FunctionalInterface
public interface SerArgsFunc<T, R> extends Serializable {

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> SerArgsFunc<T, T> last() {
        return t -> t != null && t.length > 0 ? t[t.length - 1] : null;
    }

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     * @throws java.lang.Exception if any.
     */
    @SuppressWarnings("unchecked")
    R applying(T... t) throws Throwable;

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    @SuppressWarnings("unchecked")
    default R apply(T... t) {
        try {
            return applying(t);
        } catch (Throwable e) {
            throw new LambdaInvokeException(e);
        }
    }

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>    the type of input to the {@code before} function, and to the
     *               composed function
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the {@code before}
     * function and then applies this function
     * @throws java.lang.NullPointerException if before is null
     * @see #andThen(SerArgsFunc)
     */
    @SuppressWarnings("unchecked")
    default <V> SerArgsFunc<V, R> compose(SerArgsFunc<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V... v) -> apply(before.apply(v));
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
     * @see #compose(SerArgsFunc)
     */
    @SuppressWarnings("unchecked")
    default <V> SerArgsFunc<T, V> andThen(SerArgsFunc<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T... t) -> after.apply(apply(t));
    }
}
