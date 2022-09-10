package io.github.vampireachao.stream.core.optional.impl;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.*;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * StrOpImpl
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/5 10:23
 **/
public class StrOpImpl implements StrOp {

    protected final String value;

    public StrOpImpl(String value) {
        this.value = Opp.of(value).filter(str -> !str.trim().isEmpty()).get();
    }


    @Override
    public boolean isPresent() {
        return Objects.nonNull(this.value);
    }

    @Override
    public boolean isEmpty() {
        return Objects.isNull(this.value);
    }

    @Override
    public boolean isEqual(String value) {
        return Objects.equals(this.value, value);
    }

    @Override
    public boolean is(Predicate<String> predicate) {
        return filter(predicate).isPresent();
    }

    @Override
    public String get() {
        return this.value;
    }

    @Override
    public String orElse(String other) {

        return isPresent() ? value : other;
    }

    @Override
    public String orElseGet(Supplier<String> other) {
        return isPresent() ? this.value : other.get();
    }

    @Override
    public String orElseRun(Runnable other) {
        if (isEmpty()) {
            other.run();
        }
        return this.value;
    }

    @Override
    public String orElseThrow() {
        return orElseThrow(NoSuchElementException::new);
    }

    @Override
    public <X extends Throwable> String orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return this.value;
        }
        throw exceptionSupplier.get();
    }


    @Override
    public <U> Op<U> map(Function<String, ? extends U> mapper) {
        return isPresent() ? Op.of(mapper.apply(this.value)) : Op.empty();
    }

    @Override
    public <U> ThrowOp<U> mapToThrow(SerFunc<String, ? extends U> mapper) {
        return isPresent() ? ThrowOp.of(() -> mapper.apply(this.value)) : ThrowOp.empty();
    }

    @Override
    public <U> CollOp<U> mapToColl(SerFunc<String, ? extends Collection<U>> mapper) {
        return isPresent() ? CollOp.of(mapper.apply(this.value)) : CollOp.empty();

    }

    @Override
    public <U> Op<U> flatMap(Function<String, ? extends Op<? extends U>> mapper) {
        return isPresent() ? Op.of(mapper.apply(this.value).get()) : Op.empty();

    }

    @Override
    public <U> Optional<U> flatMapToOptional(Function<String, ? extends Optional<U>> mapper) {
        return isPresent() ? mapper.apply(this.value) : Optional.empty();

    }

    @Override
    public StrOp filter(Predicate<String> predicate) {
        return isPresent() && predicate.test(this.value) ? this : StrOp.empty();
    }

    @Override
    public <R> StrOp filterEqual(R value) {
        return filter(v -> Objects.equals(this.value, value));
    }

    @Override
    public StrOp ifPresent(Consumer<String> action) {
        if (isPresent()) {
            action.accept(this.value);
        }
        return this;
    }

    @Override
    public StrOp or(Supplier<StrOp> other) {
        return isPresent() ? this : other.get();
    }

    @Override
    public Steam<String> steam() {
        return Steam.of(this.value);
    }


}
