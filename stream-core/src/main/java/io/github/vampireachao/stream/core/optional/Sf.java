package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerPred;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;

import java.util.Objects;

/**
 * scope-functions
 *
 * @author VampireAchao
 * @see <a href="https://www.kotlincn.net/docs/reference/scope-functions.html"/>
 * @since 2022/9/15
 */
public class Sf<T> {

    protected static final Sf<?> EMPTY = new Sf<>(null);

    protected final T value;

    public Sf(T value) {
        this.value = value;
    }

    public static <T> Sf<T> of(T value) {
        return new Sf<>(value);
    }

    private <R> Sf<R> empty() {
        @SuppressWarnings("unchecked")
        Sf<R> empty = (Sf<R>) EMPTY;
        return empty;
    }

    public boolean isEmpty() {
        return Objects.isNull(value);
    }

    public boolean isPresent() {
        return Objects.nonNull(value);
    }

    public T get() {
        return value;
    }

    public <R> Sf<R> let(SerFunc<T, R> mapper) {
        return of(mapper.apply(value));
    }

    public <R> Sf<R> $let(SerFunc<T, R> mapper) {
        if (isEmpty()) {
            return empty();
        }
        return let(mapper);
    }

    public Sf<T> also(SerCons<T> mapper) {
        mapper.accept(value);
        return this;
    }

    public Sf<T> $also(SerCons<T> mapper) {
        if (isEmpty()) {
            return this;
        }
        return also(mapper);
    }

    public Sf<T> takeIf(SerPred<T> mapper) {
        if (!mapper.test(value)) {
            return empty();
        }
        return this;
    }

    public Sf<T> $takeIf(SerPred<T> mapper) {
        if (isEmpty()) {
            return this;
        }
        return takeIf(mapper);
    }


    public Sf<T> takeUnless(SerPred<T> mapper) {
        return takeIf(mapper.negate());
    }

    public Sf<T> $takeUnless(SerPred<T> mapper) {
        if (isEmpty()) {
            return this;
        }
        return takeUnless(mapper);
    }

    public Sf<T> $_(SerSupp<Sf<T>> mapper) {
        if (isPresent()) {
            return this;
        }
        return mapper.get();
    }


}
