package io.github.vampireachao.stream.core.optional;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * BaseOp
 *
 * @author VampireAchao
 * @since 2022/9/4
 */
public interface BaseOp<T> {

    boolean isPresent();

    boolean isEmpty();

    boolean isEqual(Object value);

    boolean is(Predicate<T> predicate);

    T get();

    T orElse(T other);

    T orElseGet(Supplier<T> other);

    T orElseRun(Runnable other);

    T orElseThrow();

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

}
