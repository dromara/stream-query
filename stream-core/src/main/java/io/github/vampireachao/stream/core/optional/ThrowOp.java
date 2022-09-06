package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
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

    static <T> ThrowOp<T> of(Callable<T> callable) {
        // TODO not implement yet
        return null;
    }

    static <T> ThrowOp<T> of(Callable<T> callable, Class<? extends Exception>... exceptionClazz) {
        // TODO not implement yet
        return null;
    }

    static <T> ThrowOp<T> empty() {
        // TODO not implement yet
        return null;
    }

    <U> Op<U> map(Function<? super T, ? extends U> mapper);

    <U> ThrowOp<U> mapToThrow(SerFunc<? super T, ? extends U> callable);

    <U> CollOp<U> mapToColl(SerFunc<? super T, ? extends Collection<U>> callable);

    StrOp mapToStr(SerFunc<? super T, ? extends CharSequence> callable);

    <U> Op<U> flatMap(Function<? super T, ? extends Op<? extends U>> mapper);

    <U> Optional<U> flatMapToOptional(Function<? super T, ? extends Optional<? extends U>> mapper);

    Op<T> filter(Predicate<? super T> predicate);

    <R> Op<T> filterEqual(R value);

    Op<T> ifPresent(Consumer<? super T> action);

    Op<T> or(Supplier<Op<? extends T>> other);

    Steam<T> steam();

    Exception getException();

}
