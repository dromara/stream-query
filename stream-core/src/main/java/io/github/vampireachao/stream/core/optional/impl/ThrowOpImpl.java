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
 * <p>ThrowOpImpl class.</p>
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/9 16:16
 */
public class ThrowOpImpl<T> implements ThrowOp<T> {

    /**
     * 包裹里实际的元素
     */
    protected T value;
    protected Exception exception;

    @SafeVarargs
    @SuppressWarnings("safevarargs")
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
     * {@inheritDoc}
     */
    @Override
    public boolean isPresent() {
        return Objects.nonNull(this.exception);
    }

    /**
     * <p>isPresentV.</p>
     *
     * @return a boolean
     */
    public boolean isPresentV() {
        return Objects.nonNull(this.value);
    }

    /**
     * <p>isEmptyV.</p>
     *
     * @return a boolean
     */
    public boolean isEmptyV() {
        return Objects.isNull(this.value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return Objects.isNull(this.exception);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqual(Object value) {

        return Objects.equals(this.value, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean is(Predicate<T> predicate) {
        return filter(predicate).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Exception getException() {
        return this.exception;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T orElse(T other) {
        return isPresentV() ? value : other;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T orElseGet(Supplier<T> other) {
        return isPresentV() ? this.value : other.get();
    }

    /** {@inheritDoc} */
    @Override
    public T orElseRun(Runnable other) {
        if (isEmptyV()) {
            other.run();
        }
        return this.value;
    }

    /** {@inheritDoc} */
    @Override
    public T orElseThrow() {
        return orElseThrow(NoSuchElementException::new);
    }

    /** {@inheritDoc} */
    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            throw exceptionSupplier.get();

        }
        return this.value;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<T> toOptional() {
        return  Optional.ofNullable(this.value);
    }

    /** {@inheritDoc} */
    public ThrowOp<T> orOptional(Optional<? extends T> other) {
        return ThrowOp.of(()->other.orElse(null));
    }

    /** {@inheritDoc} */
    @Override
    public ThrowOp<T> flatOptional(Function<? super T, Optional<T>> mapper) {
        if (isEmptyV()) {
            return ThrowOp.empty();
        }
        return ThrowOp.ofOptional(mapper.apply(this.value));
    }

    /** {@inheritDoc} */
    @Override
    public <U> Op<U> map(Function<? super T, ? extends U> mapper) {
        return isPresentV() ? Op.of(mapper.apply(this.value)) : Op.empty();
    }

    /** {@inheritDoc} */
    @Override
    public <U> CollOp<U> mapToColl(SerFunc<? super T, ? extends Collection<U>> callable) {
        return isPresentV() ? CollOp.of(callable.apply(this.value)) : CollOp.empty();

    }

    /** {@inheritDoc} */
    @Override
    public StrOp mapToStr(SerFunc<? super T, ? extends CharSequence> callable) {
        return isPresentV() ? StrOp.of(callable.apply(this.value).toString()) : StrOp.empty();
    }

    /** {@inheritDoc} */
    @Override
    public <U> Op<U> flatMap(Function<? super T, ? extends Op<? extends U>> mapper) {
        return isPresentV() ? Op.of(mapper.apply(this.value).get()) : Op.empty();
    }

    /** {@inheritDoc} */
    @Override
    public <U> Optional<U> flatMapToOptional(Function<T, ? extends Optional<U>> mapper) {
        return isPresentV() ? mapper.apply(this.value) : Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    public ThrowOp<T> filter(Predicate<? super T> predicate) {
        return isPresentV() && predicate.test(this.value) ? this : ThrowOp.empty();
    }


    /** {@inheritDoc} */
    @Override
    public ThrowOp<T> ifPresent(Consumer<? super T> action) {
        if (isPresentV()) {
            action.accept(this.value);
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Steam<T> steam() {
        return Steam.of(this.value);
    }

    /** {@inheritDoc} */
    @Override
    public ThrowOp<T> or(Supplier<ThrowOp<T>> other) {
        return isPresent() ? this :  other.get();
    }

    /** {@inheritDoc} */
    @Override
    public ThrowOp<T> filterEqual(Object value) {
        return filter(v -> Objects.equals(this.value, value));
    }
}
