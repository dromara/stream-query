package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.impl.ThrowOpImpl;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Collection;
import java.util.Objects;
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

    ThrowOp EMPTY = new ThrowOpImpl<>(null, Exception.class);

    static <T> ThrowOp<T> of(Callable<T> callable) {

        return new ThrowOpImpl<>(callable, Exception.class);
    }

    static <T> ThrowOp<T> of(Callable<T> callable, Class<? extends Exception>... exceptionClazz) {

        return new ThrowOpImpl<>(callable, exceptionClazz);
    }

    static <T> ThrowOp<T> empty() {

        return EMPTY;
    }

    static <T> ThrowOp<T> ofOptional(Optional<T> apply){
        return new ThrowOpImpl<>(()->apply.orElse(null));
    }

    <U> Op<U> map(Function<? super T, ? extends U> mapper);


    <U> CollOp<U> mapToColl(SerFunc<? super T, ? extends Collection<U>> callable);

    StrOp mapToStr(SerFunc<? super T, ? extends CharSequence> callable);

    <U> Op<U> flatMap(Function<? super T, ? extends Op<? extends U>> mapper);

    <U> Optional<U> flatMapToOptional(Function<T, ? extends Optional<U>> mapper);

    ThrowOp<T> filter(Predicate<? super T> predicate);

    <R> ThrowOp<T> filterEqual(R value);

    ThrowOp<T> ifPresent(Consumer<? super T> action);

    ThrowOp<T> or(Supplier<ThrowOp<T>> other);

    boolean isPresentV();

    boolean isEmptyV();

    Steam<T> steam();

    Exception getException();

}
