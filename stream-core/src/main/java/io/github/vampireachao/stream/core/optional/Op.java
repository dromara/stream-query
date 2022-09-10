package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.impl.OpImpl;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Opp
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/4
 */
@SuppressWarnings("all")
public interface Op<T> extends BaseOp<T>, IOptional<T, Op<T>>, ITypeOp<T> {

    /**
     * Constant <code>EMPTY</code>
     */
    Op<?> EMPTY = new OpImpl<>(null);

    /**
     * <p>of.</p>
     *
     * @param value a T object
     * @param <T>   a T class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    static <T> Op<T> of(T value) {
        return new OpImpl<>(value);
    }

    /**
     * <p>ofOptional.</p>
     *
     * @param value a {@link java.util.Optional} object
     * @param <T>   a T class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    static <T> Op<T> ofOptional(Optional<T> value) {
        return new OpImpl<>(value.orElse(null));
    }

    /**
     * <p>empty.</p>
     *
     * @param <T> a T class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    @SuppressWarnings("unchecked")
    static <T> Op<T> empty() {
        return (Op<T>) EMPTY;
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
     * <p>mapToThrow.</p>
     *
     * @param mapper a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    <U> ThrowOp<U> mapToThrow(SerFunc<? super T, ? extends U> mapper);

    /**
     * <p>mapToColl.</p>
     *
     * @param mapper a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.CollOp} object
     */
    <U> CollOp<U> mapToColl(SerFunc<? super T, ? extends Collection<U>> mapper);

    /**
     * <p>mapToStr.</p>
     *
     * @param mapper a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @return a {@link io.github.vampireachao.stream.core.optional.StrOp} object
     */
    StrOp mapToStr(SerFunc<? super T, ? extends String> mapper);

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
    <U> Optional<U> flatMapToOptional(Function<? super T, ? extends Optional<U>> mapper);

    /**
     * <p>filter.</p>
     *
     * @param predicate a {@link java.util.function.Predicate} object
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    Op<T> filter(Predicate<? super T> predicate);

    /**
     * <p>filterEqual.</p>
     *
     * @param value a R object
     * @param <R>   a R class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    <R> Op<T> filterEqual(R value);

    /**
     * <p>ifPresent.</p>
     *
     * @param action a {@link java.util.function.Consumer} object
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    Op<T> ifPresent(Consumer<? super T> action);

    /**
     * <p>or.</p>
     *
     * @param other a {@link java.util.function.Supplier} object
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    Op<T> or(Supplier<Op<T>> other);

    /**
     * <p>steam.</p>
     *
     * @return a {@link io.github.vampireachao.stream.core.stream.Steam} object
     */
    Steam<T> steam();
}
