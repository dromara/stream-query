package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * SerSerBiCons
 *
 * @author VampireAchao
 * @since 2022/6/8
 */
@FunctionalInterface
public interface SerBiCons<T, U> extends BiConsumer<T, U>, Serializable {
    /**
     * multi
     *
     * @param consumers lambda
     * @param <T>       type
     * @return lambda
     */
    @SafeVarargs
    static <T, U> SerBiCons<T, U> multi(SerBiCons<T, U>... consumers) {
        return Stream.of(consumers).reduce(SerBiCons::andThen).orElseGet(() -> (o, q) -> {});
    }

    /**
     * Returns a composed {@code SerBiCons} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code SerBiCons} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default SerBiCons<T, U> andThen(SerBiCons<? super T, ? super U> after) {
        Objects.requireNonNull(after);
        return (l, r) -> {
            accept(l, r);
            after.accept(l, r);
        };
    }
}
