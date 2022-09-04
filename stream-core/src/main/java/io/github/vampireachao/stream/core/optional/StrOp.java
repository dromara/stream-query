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
 * StrOp
 *
 * @author VampireAchao
 * @since 2022/9/4
 */
public interface StrOp extends BaseOp<String> {

    StrOp EMPTY = null;

    static StrOp of(CharSequence value) {
        return null;
    }

    static StrOp empty() {
        return EMPTY;
    }

    <U> Op<U> map(Function<String, ? extends U> mapper);

    <U> ThrowOp<U> mapToThrow(SerFunc<String, ? extends U> mapper);

    <U> CollOp<U> mapToColl(SerFunc<String, ? extends Collection<U>> mapper);

    StrOp mapToStr(SerFunc<String, ? extends CharSequence> mapper);

    <U> Op<U> flatMap(Function<String, ? extends Op<? extends U>> mapper);

    <U> Optional<U> flatMapToOptional(Function<String, ? extends Optional<U>> mapper);

    StrOp filter(Predicate<String> predicate);

    <R> StrOp filterEqual(R value);

    StrOp ifPresent(Consumer<String> action);

    StrOp or(Supplier<StrOp> other);

    Steam<String> steam();

}
