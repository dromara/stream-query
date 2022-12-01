package io.github.vampireachao.stream.core.lambda.function;

import io.github.vampireachao.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 可序列化的Consumer
 *
 * @author VampireAchao Cizai_

 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface SerCons<T> extends Consumer<T>, Serializable {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     * @throws java.lang.Exception if any.
     */
    @SuppressWarnings("all")
    void accepting(T t) throws Exception;

    /**
     * Performs this operation on the given argument.
     */
    @Override
    default void accept(T t) {
        try {
            accepting(t);
        } catch (Exception e) {
            throw new LambdaInvokeException(e);
        }
    }

    /**
     * multi
     *
     * @param consumers lambda
     * @param <T>       type
     * @return lambda
     */
    @SafeVarargs
    static <T> SerCons<T> multi(SerCons<T>... consumers) {
        return Stream.of(consumers).reduce(SerCons::andThen).orElseGet(() -> o -> {
        });
    }

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws java.lang.NullPointerException if {@code after} is null
     */
    default SerCons<T> andThen(SerCons<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }

    /**
     * nothing
     *
     * @param <T> a T class
     * @return nothing
     */
    static <T> SerCons<T> nothing() {
        return t -> {
        };
    }
}
