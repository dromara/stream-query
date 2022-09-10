package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.impl.StrOpImpl;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * StrOp
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/4
 */
public interface StrOp extends BaseOp<String> {

    /**
     * Constant <code>EMPTY</code>
     */
    StrOp EMPTY = new StrOpImpl(null);

    /**
     * <p>of.</p>
     *
     * @param value a {@link java.lang.String} object
     * @return a {@link io.github.vampireachao.stream.core.optional.StrOp} object
     */
    static StrOp of(String value) {
        return new StrOpImpl(value);
    }

    /**
     * <p>empty.</p>
     *
     * @return a {@link io.github.vampireachao.stream.core.optional.StrOp} object
     */
    static StrOp empty() {
        return EMPTY;
    }

    /**
     * <p>map.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    <U> Op<U> map(Function<String, ? extends U> mapper);

    /**
     * <p>mapToThrow.</p>
     *
     * @param mapper a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    <U> ThrowOp<U> mapToThrow(SerFunc<String, ? extends U> mapper);

    /**
     * <p>mapToColl.</p>
     *
     * @param mapper a {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.CollOp} object
     */
    <U> CollOp<U> mapToColl(SerFunc<String, ? extends Collection<U>> mapper);

    /**
     * <p>flatMap.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @param <U>    a U class
     * @return a {@link io.github.vampireachao.stream.core.optional.Op} object
     */
    <U> Op<U> flatMap(Function<String, ? extends Op<? extends U>> mapper);

    /**
     * <p>flatMapToOptional.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @param <U>    a U class
     * @return a {@link java.util.Optional} object
     */
    <U> Optional<U> flatMapToOptional(Function<String, ? extends Optional<U>> mapper);

    /**
     * <p>filter.</p>
     *
     * @param predicate a {@link java.util.function.Predicate} object
     * @return a {@link io.github.vampireachao.stream.core.optional.StrOp} object
     */
    StrOp filter(Predicate<String> predicate);

    /**
     * <p>filterEqual.</p>
     *
     * @param value a R object
     * @param <R>   a R class
     * @return a {@link io.github.vampireachao.stream.core.optional.StrOp} object
     */
    <R> StrOp filterEqual(R value);

    /**
     * <p>ifPresent.</p>
     *
     * @param action a {@link java.util.function.Consumer} object
     * @return a {@link io.github.vampireachao.stream.core.optional.StrOp} object
     */
    StrOp ifPresent(Consumer<String> action);

    /**
     * <p>or.</p>
     *
     * @param other a {@link java.util.function.Supplier} object
     * @return a {@link io.github.vampireachao.stream.core.optional.StrOp} object
     */
    StrOp or(Supplier<StrOp> other);

    /**
     * <p>steam.</p>
     *
     * @return a {@link io.github.vampireachao.stream.core.stream.Steam} object
     */
    Steam<String> steam();

}
