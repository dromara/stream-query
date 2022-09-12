package io.github.vampireachao.stream.core.optional.impl;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.CollOp;
import io.github.vampireachao.stream.core.optional.Op;
import io.github.vampireachao.stream.core.optional.StrOp;
import io.github.vampireachao.stream.core.optional.ThrowOp;
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

    @SuppressWarnings("safevarargs")
    public ThrowOpImpl(T value) {
        this.value = value;
    }

    /**
     * <p>of.</p>
     *
     * @param callable       a {@link java.util.concurrent.Callable} object
     * @param exceptionClazz a {@link java.lang.Class} object
     * @param <T>            a T class
     * @return a {@link io.github.vampireachao.stream.core.optional.ThrowOp} object
     */
    @SafeVarargs
    public static <T> ThrowOp<T> of(Callable<T> callable, Class<? extends Exception>... exceptionClazz) {
        try {
            return new ThrowOpImpl<>(callable.call());
        } catch (Exception e) {
            if (Steam.of(exceptionClazz).noneMatch(clazz -> clazz.isInstance(e))) {
                throw new IllegalArgumentException(e);
            }
            ThrowOpImpl<T> op = new ThrowOpImpl<>(null);
            op.exception = e;
            return op;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPresent() {
        return Objects.nonNull(this.value);
    }

    /**
     * <p>hasException</p>
     *
     * @return a boolean
     */
    public boolean hasException() {
        return Objects.nonNull(this.exception);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return Objects.isNull(this.value);
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
        return isPresent() ? value : other;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T orElseGet(Supplier<T> other) {
        return isPresent() ? this.value : other.get();
    }

    /** {@inheritDoc} */
    @Override
    public T orElseRun(Runnable other) {
        if (isEmpty()) {
            other.run();
        }
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <X extends Throwable> T orElseThrow() throws X {
        return orElseThrow(() -> hasException() ?
                SerFunc.<Exception, X>castingIdentity().apply(this.exception) :
                SerFunc.<Exception, X>castingIdentity().apply(new NoSuchElementException()));
    }

    /** {@inheritDoc} */
    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isEmpty()) {
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
        if (isEmpty()) {
            return ThrowOp.empty();
        }
        return ThrowOp.ofOptional(mapper.apply(this.value));
    }

    /** {@inheritDoc} */
    @Override
    public <U> Op<U> map(Function<? super T, ? extends U> mapper) {
        return isPresent() ? Op.of(mapper.apply(this.value)) : Op.empty();
    }

    /** {@inheritDoc} */
    @Override
    public <U> CollOp<U> mapToColl(SerFunc<? super T, ? extends Collection<U>> callable) {
        return isPresent() ? CollOp.of(callable.apply(this.value)) : CollOp.empty();

    }

    /** {@inheritDoc} */
    @Override
    public StrOp mapToStr(SerFunc<? super T, ? extends CharSequence> callable) {
        return isPresent() ? StrOp.of(callable.apply(this.value).toString()) : StrOp.empty();
    }

    /** {@inheritDoc} */
    @Override
    public <U> Op<U> flatMap(Function<? super T, ? extends Op<? extends U>> mapper) {
        return isPresent() ? Op.of(mapper.apply(this.value).get()) : Op.empty();
    }

    /** {@inheritDoc} */
    @Override
    public <U> Optional<U> flatMapToOptional(Function<T, ? extends Optional<U>> mapper) {
        return isPresent() ? mapper.apply(this.value) : Optional.empty();
    }

    /** {@inheritDoc} */
    @Override
    public ThrowOp<T> filter(Predicate<? super T> predicate) {
        return isPresent() && predicate.test(this.value) ? this : ThrowOp.empty();
    }


    /** {@inheritDoc} */
    @Override
    public ThrowOp<T> ifPresent(Consumer<? super T> action) {
        if (isPresent()) {
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
