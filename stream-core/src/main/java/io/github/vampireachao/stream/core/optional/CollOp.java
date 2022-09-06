package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
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

    static <E> CollOp<E> of(Collection<E> collection) {
        // TODO not implement yet
        return null;
    }

    static <T> CollOp<T> empty() {
        // TODO not implement yet
        return null;
    }

    <U> Op<U> map(Function<? super Collection<E>, ? extends U> mapper);

    <U> ThrowOp<U> mapToThrow(SerFunc<? super Collection<E>, ? extends U> callable);

    StrOp mapToStr(SerFunc<? super Collection<E>, ? extends CharSequence> callable);

    <U> CollOp<U> flatMap(Function<? super Collection<E>, ? extends Op<? extends U>> mapper);

    <U> Optional<U> flatMapToOptional(Function<? super Collection<E>, ? extends Optional<? extends U>> mapper);

    CollOp<E> filter(Predicate<? super Collection<E>> predicate);

    <R> CollOp<E> filterEqual(R value);

    CollOp<E> ifPresent(Consumer<? super Collection<E>> action);

    CollOp<E> or(Supplier<CollOp<E>> other);

    Steam<E> steam();


}
