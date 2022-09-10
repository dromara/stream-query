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

    /**
     * <p>isPresent.</p>
     *
     * @return a boolean
     */
    boolean isPresent();

    /**
     * <p>isEmpty.</p>
     *
     * @return a boolean
     */
    boolean isEmpty();

    /**
     * <p>isEqual.</p>
     *
     * @param value a T object
     * @return a boolean
     */
    boolean isEqual(T value);

    /**
     * <p>is.</p>
     *
     * @param predicate a {@link java.util.function.Predicate} object
     * @return a boolean
     */
    boolean is(Predicate<T> predicate);

    /**
     * <p>get.</p>
     *
     * @return a T object
     */
    T get();

    /**
     * <p>orElse.</p>
     *
     * @param other a T object
     * @return a T object
     */
    T orElse(T other);

    /**
     * <p>orElseGet.</p>
     *
     * @param other a {@link java.util.function.Supplier} object
     * @return a T object
     */
    T orElseGet(Supplier<T> other);

    /**
     * <p>orElseRun.</p>
     *
     * @param other a {@link java.lang.Runnable} object
     * @return a T object
     */
    T orElseRun(Runnable other);

    /**
     * <p>orElseThrow.</p>
     *
     * @return a T object
     */
    T orElseThrow();

    /**
     * <p>orElseThrow.</p>
     *
     * @param exceptionSupplier a {@link java.util.function.Supplier} object
     * @param <X>               a X class
     * @return a T object
     * @throws X if any.
     */
    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

}
