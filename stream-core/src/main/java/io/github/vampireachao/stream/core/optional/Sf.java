package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Collection;
import java.util.NoSuchElementException;
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

    public static <E, T extends Collection<E>> Sf<T> ofColl(T value) {
        return of(value).$takeIf(c -> !c.isEmpty());
    }

    public static <E, T extends Collection<E>> Sf<T> $ofColl(T value) {
        return ofColl(value).$takeIf(c -> Steam.of(c).anyMatch(Objects::nonNull));
    }

    public static <T extends CharSequence> Sf<T> ofStr(T value) {
        return of(value).$takeIf(c -> !c.toString().trim().isEmpty());
    }

    public static <T extends CharSequence> Sf<T> $ofStr(T value) {
        return ofStr(value).$takeIf(c -> Steam.split(c, "").anyMatch(e -> !"".equals(e)));
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

    public Sf<T> takeIf(SerFunc<T, Boolean> mapper) {
        if (!Boolean.TRUE.equals(mapper.apply(value))) {
            return empty();
        }
        return this;
    }

    public Sf<T> $takeIf(SerFunc<T, Boolean> mapper) {
        if (isEmpty()) {
            return this;
        }
        return takeIf(mapper);
    }


    public Sf<T> takeUnless(SerFunc<T, Boolean> mapper) {
        return takeIf(v -> Boolean.TRUE.equals(mapper.apply(v)));
    }

    public Sf<T> $takeUnless(SerFunc<T, Boolean> mapper) {
        if (isEmpty()) {
            return this;
        }
        return takeUnless(mapper);
    }

    public boolean is(SerFunc<T, Boolean> mapper) {
        return isPresent() && Boolean.TRUE.equals(mapper.apply(value));
    }

    public <X extends Throwable> Sf<T> require(SerSupp<X> mapper) throws X {
        if (isPresent()) {
            return this;
        }
        throw mapper.get();
    }

    public Sf<T> require() {
        return require(NoSuchElementException::new);
    }

    public Sf<T> or(SerSupp<Sf<T>> mapper) {
        if (isPresent()) {
            return this;
        }
        return mapper.get();
    }

    public T orElse(T other) {
        if (isPresent()) {
            return value;
        }
        return other;
    }

    public T orGet(SerSupp<T> mapper) {
        if (isPresent()) {
            return value;
        }
        return mapper.get();
    }

    public <X extends Throwable> T orThrow(SerSupp<X> mapper) throws X {
        if (isPresent()) {
            return value;
        }
        throw mapper.get();
    }

    public T orThrow() {
        return orThrow(NoSuchElementException::new);
    }

}
