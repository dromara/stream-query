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
 * @author VampireAchao
 * @since 2022/9/4
 */
@SuppressWarnings("all")
public interface Op<T> extends BaseOp<T>, IOptional<T, Op<T>>, ITypeOp<T> {

    Op<?> EMPTY = new OpImpl<>(null);

    static <T> Op<T> of(T value) {
        return new OpImpl<>(value);
    }

    static <T> Op<T> ofOptional(Optional<T> value) {
        return new OpImpl<>(value.orElse(null));
    }

    static <T> Op<T> empty() {
        return (Op<T>) EMPTY;
    }

    <U> Op<U> map(Function<? super T, ? extends U> mapper);

    <U> ThrowOp<U> mapToThrow(SerFunc<? super T, ? extends U> mapper);

    <U> CollOp<U> mapToColl(SerFunc<? super T, ? extends Collection<U>> mapper);

    StrOp mapToStr(SerFunc<? super T, ? extends CharSequence> mapper);

    <U> Op<U> flatMap(Function<? super T, ? extends Op<? extends U>> mapper);

    <U> Optional<U> flatMapToOptional(Function<? super T, ? extends Optional<U>> mapper);

    Op<T> filter(Predicate<? super T> predicate);

    <R> Op<T> filterEqual(R value);

    Op<T> ifPresent(Consumer<? super T> action);

    Op<T> or(Supplier<Op<T>> other);

    Steam<T> steam();
}
