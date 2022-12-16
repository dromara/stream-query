package io.github.vampireachao.stream.core.lambda.function;

import io.github.vampireachao.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * SerArgsSerArgsCons
 *
 * @author VampireAchao Cizai_

 * @since 2022/6/8
 */
@FunctionalInterface
public interface SerArgsCons<T> extends Serializable {

    /**
     * multi
     *
     * @param consumers lambda
     * @param <T>       type
     * @return lambda
     */
    @SafeVarargs
    static <T> SerArgsCons<T> multi(SerArgsCons<T>... consumers) {
        return Stream.of(consumers).reduce(SerArgsCons::andThen).orElseGet(() -> o -> {
        });
    }

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input arguments
     * @throws java.lang.Exception maybe throw exception
     */
    @SuppressWarnings("unchecked")
    void accepting(T... t) throws Throwable;

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input arguments
     */
    @SuppressWarnings("unchecked")
    default void accept(T... t) {
        try {
            accepting(t);
        } catch (Throwable e) {
            throw new LambdaInvokeException(e);
        }
    }

    /**
     * Returns a composed {@code SerArgsCons} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code SerArgsCons} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws java.lang.NullPointerException if {@code after} is null
     */
    default SerArgsCons<T> andThen(SerArgsCons<? super T> after) {
        Objects.requireNonNull(after);
        return (T... t) -> {
            this.accept(t);
            after.accept(t);
        };
    }

    /**
     * nothing
     *
     * @param <T> a T class
     * @return nothing
     */
    static <T> SerArgsCons<T> nothing() {
        return t -> {
        };
    }
}
