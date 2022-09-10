package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.impl.ThrowOpImpl;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * ThrowOpp
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/4
 */
public interface ThrowOp<T> extends BaseOp<T>, IOptional<T, ThrowOp<T>> {

    /**
     * Constant <code>EMPTY</code>
     */
    @SuppressWarnings("safevarargs")
    ThrowOp<?> EMPTY = new ThrowOpImpl<>(null, Exception.class);

    /**
     * <p>of.</p>
     *
     * @param callable a {@link java.util.concurrent.Callable} object
     * @param <T>      a T class
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    static <T> ThrowOp<T> of(Callable<T> callable) {
        return new ThrowOpImpl<>(callable, Exception.class);
    }

    /**
     * <p>of.</p>
     *
     * @param callable       a {@link java.util.concurrent.Callable} object
     * @param exceptionClazz a {@link java.lang.Class} object
     * @param <T>            a T class
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    @SafeVarargs
    static <T> ThrowOp<T> of(Callable<T> callable, Class<? extends Exception>... exceptionClazz) {
        return new ThrowOpImpl<>(callable, exceptionClazz);
    }

    /**
     * <p>empty.</p>
     *
     * @param <T> a T class
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    @SuppressWarnings("unchecked")
    static <T> ThrowOp<T> empty() {
        return (ThrowOp<T>) EMPTY;
    }

    /**
     * <p>ofOptional.</p>
     *
     * @param apply a {@link java.util.Optional} object
     * @param <T>   a T class
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    @SuppressWarnings("safevarargs")
    static <T> ThrowOp<T> ofOptional(Optional<T> apply) {
        return new ThrowOpImpl<>(() -> apply.orElse(null));
    }

    /**
     * <p>map.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    <U> Op<U> map(Function<? super T, ? extends U> mapper);


    /**
     * <p>mapToColl.</p>
     *
     * @param callable a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param <U>      a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.CollOp} object
     */
    <U> CollOp<U> mapToColl(SerFunc<? super T, ? extends Collection<U>> callable);

    /**
     * <p>mapToStr.</p>
     *
     * @param callable a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @return a {@link io.github.vampireachao.stream.core.optional.StrOp} object
     */
    StrOp mapToStr(SerFunc<? super T, ? extends CharSequence> callable);

    /**
     * <p>flatMap.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    <U> Op<U> flatMap(Function<? super T, ? extends Op<? extends U>> mapper);

    /**
     * <p>flatMapToOptional.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @param <U>    a U class
     * @return a {@link java.util.Optional} object
     */
    <U> Optional<U> flatMapToOptional(Function<T, ? extends Optional<U>> mapper);

    /**
     * <p>filter.</p>
     *
     * @param predicate a {@link java.util.function.Predicate} object
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    ThrowOp<T> filter(Predicate<? super T> predicate);

    /**
     * <p>filterEqual.</p>
     *
     * @param value a R object
     * @param <R>   a R class
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    <R> ThrowOp<T> filterEqual(R value);

    /**
     * <p>ifPresent.</p>
     *
     * @param action a {@link java.util.function.Consumer} object
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    ThrowOp<T> ifPresent(Consumer<? super T> action);

    /**
     * <p>or.</p>
     *
     * @param other a {@link java.util.function.Supplier} object
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    ThrowOp<T> or(Supplier<ThrowOp<T>> other);

    /**
     * <p>isPresentV.</p>
     *
     * @return a boolean
     */
    boolean isPresentV();

    /**
     * <p>isEmptyV.</p>
     *
     * @return a boolean
     */
    boolean isEmptyV();

    /**
     * <p>steam.</p>
     *
     * @return a {@link io.github.vampireachao.stream.core.stream.Steam} object
     */
    Steam<T> steam();

    /**
     * <p>getException.</p>
     *
     * @return a {@link java.lang.Exception} object
     */
    Exception getException();

}
