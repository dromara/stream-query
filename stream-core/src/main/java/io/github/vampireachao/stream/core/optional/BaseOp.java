package io.github.vampireachao.stream.core.optional;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * BaseOp
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/4
 */
public interface BaseOp<T> {

    boolean isPresent();

    boolean isEmpty();

    boolean isEqual(T value);

    boolean is(Predicate<T> predicate);

    T get();

    T orElse(T other);

    T orElseGet(Supplier<T> other);

    T orElseRun(Runnable other);

    T orElseThrow();

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

}
