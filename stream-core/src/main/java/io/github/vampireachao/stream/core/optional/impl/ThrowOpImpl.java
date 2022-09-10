package io.github.vampireachao.stream.core.optional.impl;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.*;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @Description: ThrowOpImpl
 * @DateTime: 2022/9/9 16:16
 **/
public class ThrowOpImpl<T> implements ThrowOp<T> {

    /**
     * 包裹里实际的元素
     */
    protected T value;
    protected Exception exception;

    @SafeVarargs
    public ThrowOpImpl(Callable<T> callable, Class<? extends Exception>... exceptionClazz) {
        try {
            this.value = Opp.of(callable.call()).get();
        } catch (Exception e) {
            if (Steam.of(exceptionClazz).noneMatch(clazz -> clazz.isInstance(e))) {
                throw new IllegalArgumentException(e);
            }
            this.exception = e;
        }
    }

    /**
     *
     * @return 这里只对ThrowOp中的exception进行判断存在为True否则为False
     */
    @Override
    public boolean isPresent() {
        return Objects.nonNull(this.exception);
    }

    public boolean isPresentV(){
        return Objects.nonNull(this.value);
    }

    public boolean isEmptyV(){
        return Objects.isNull(this.value);
    }

    /**
     *
     * @return 这里只对ThrowOp中的exception进行判断为空为True否则为False
     */
    @Override
    public boolean isEmpty() {
        return Objects.isNull(this.exception);
    }

    @Override
    public boolean isEqual(Object value) {

        return Objects.equals(this.value, value);
    }

    @Override
    public boolean is(Predicate<T> predicate) {
        return filter(predicate).isPresent();
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public Exception getException() {
        return this.exception;
    }

    @Override
    public T orElse(T other) {
        return isPresentV() ? value : other;
    }

    @Override
    public T orElseGet(Supplier<T> other) {
        return isPresentV() ? this.value : other.get();
    }

    @Override
    public T orElseRun(Runnable other) {
        if (isEmptyV()) {
            other.run();
        }
        return this.value;
    }

    @Override
    public T orElseThrow() {
        return orElseThrow(NoSuchElementException::new);
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            throw exceptionSupplier.get();

        }
        return this.value;
    }

    @Override
    public Optional<T> toOptional() {
        return  Optional.ofNullable(this.value);
    }

    public ThrowOp<T> orOptional(Optional<? extends T> other) {
        return ThrowOp.of(()->other.orElse(null));
    }

    @Override
    public ThrowOp<T> flatOptional(Function<? super T, Optional<T>> mapper) {
        if (isEmptyV()) {
            return ThrowOp.empty();
        }
        return ThrowOp.ofOptional(mapper.apply(this.value));
    }

    @Override
    public <U> Op<U> map(Function<? super T, ? extends U> mapper) {
        return isPresentV() ? Op.of(mapper.apply(this.value)) : Op.empty();
    }

    @Override
    public <U> CollOp<U> mapToColl(SerFunc<? super T, ? extends Collection<U>> callable) {
        return isPresentV() ? CollOp.of(callable.apply(this.value)) : CollOp.empty();

    }

    @Override
    public StrOp mapToStr(SerFunc<? super T, ? extends CharSequence> callable) {
        return isPresentV() ? StrOp.of(callable.apply(this.value).toString()) : StrOp.empty();
    }

    @Override
    public <U> Op<U> flatMap(Function<? super T, ? extends Op<? extends U>> mapper) {
        return isPresentV() ? Op.of(mapper.apply(this.value).get()) : Op.empty();
    }

    @Override
    public <U> Optional<U> flatMapToOptional(Function<T, ? extends Optional<U>> mapper) {
        return isPresentV() ? mapper.apply(this.value) : Optional.empty();
    }

    @Override
    public ThrowOp<T> filter(Predicate<? super T> predicate) {
        return isPresentV() && predicate.test(this.value) ? this : ThrowOp.empty();
    }


    @Override
    public ThrowOp<T> ifPresent(Consumer<? super T> action) {
        if (isPresentV()) {
            action.accept(this.value);
        }
        return this;
    }

    @Override
    public Steam<T> steam() {
        return Steam.of(this.value);
    }

    @Override
    public ThrowOp<T> or(Supplier<ThrowOp<T>> other) {
        return isPresent() ? this :  other.get();
    }

    @Override
    public ThrowOp<T> filterEqual(Object value) {
        return filter(v -> Objects.equals(this.value, value));
    }
}
