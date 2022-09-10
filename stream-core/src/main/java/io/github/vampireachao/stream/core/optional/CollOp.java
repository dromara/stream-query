package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.optional.impl.CollOpImpl;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * CollOpp
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/4
 */
public interface CollOp<E> extends BaseOp<Collection<E>> {

    /**
     * Constant <code>EMPTY</code>
     */
    CollOp<?> EMPTY = new CollOpImpl<>(null);

    /**
     * <p>of.</p>
     *
     * @param collection a {@link java.util.Collection} object
     * @param <E>        a E class
     * @return a {@link io.github.vampireachao.stream.core.optional.CollOp} object
     */
    static <E> CollOp<E> of(Collection<E> collection) {

        return new CollOpImpl<>(collection);
    }

    /**
     * <p>empty.</p>
     *
     * @param <E> a E class
     * @return a {@link io.github.vampireachao.stream.core.optional.CollOp} object
     */
    @SuppressWarnings("unchecked")
    static <E> CollOp<E> empty() {
        return (CollOp<E>) EMPTY;
    }

    /**
     * <p>isEqual.</p>
     *
     * @param value a {@link java.util.Collection} object
     * @return a boolean
     */
    boolean isEqual(Collection<E> value);

    /**
     * <p>map.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.CollOp} object
     */
    <U> CollOp<U> map(Function<? super E, U> mapper);

    /**
     * <p>flatMap.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    <U> Op<U> flatMap(Function<? super Collection<E>, ? extends Op<? extends U>> mapper);

    /**
     * <p>flatMapToOptional.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @param <U>    a U class
     * @return a {@link java.util.Optional} object
     */
    <U> Optional<U> flatMapToOptional(Function<? super Collection<E>, ? extends Optional<? extends U>> mapper);

    /**
     * <p>filter.</p>
     *
     * @param predicate a {@link java.util.function.Predicate} object
     * @return a {@link io.github.vampireachao.stream.core.optional.CollOp} object
     */
    CollOp<E> filter(Predicate<? super E> predicate);

    /**
     * <p>ifPresent.</p>
     *
     * @param action a {@link java.util.function.Consumer} object
     * @return a {@link io.github.vampireachao.stream.core.optional.CollOp} object
     */
    CollOp<E> ifPresent(Consumer<? super Collection<E>> action);

    /**
     * <p>or.</p>
     *
     * @param other a {@link java.util.function.Supplier} object
     * @return a {@link io.github.vampireachao.stream.core.optional.CollOp} object
     */
    CollOp<E> or(Supplier<CollOp<E>> other);

    /**
     * <p>steam.</p>
     *
     * @return a {@link io.github.vampireachao.stream.core.stream.Steam} object
     */
    Steam<E> steam();


}
