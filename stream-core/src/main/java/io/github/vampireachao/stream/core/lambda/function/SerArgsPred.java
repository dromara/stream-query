package io.github.vampireachao.stream.core.lambda.function;

import io.github.vampireachao.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 可序列化的Predicate
 *
 * @author VampireAchao ZVerify
 * @see Predicate
 */
@FunctionalInterface
public interface SerArgsPred<T> extends Serializable {

    /**
     * multi
     *
     * @param predicates lambda
     * @param <T>        类型
     * @return lambda
     */
    @SafeVarargs
    static <T> SerArgsPred<T> multiAnd(SerArgsPred<T>... predicates) {
        return Stream.of(predicates).reduce(SerArgsPred::and).orElseGet(() -> o -> true);
    }

    /**
     * multi
     *
     * @param predicates lambda
     * @param <T>        类型
     * @return lambda
     */
    @SafeVarargs
    static <T> SerArgsPred<T> multiOr(SerArgsPred<T>... predicates) {
        return Stream.of(predicates).reduce(SerArgsPred::or).orElseGet(() -> o -> false);
    }

    /**
     * Returns a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}.
     *
     * @param <T>       the type of arguments to the predicate
     * @param targetRef the object reference with which to compare for equality,
     *                  which may be {@code null}
     * @return a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}
     */
    static <T> SerArgsPred<T> isEqual(Object... targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : object -> Stream.of(targetRef).allMatch(target -> target.equals(object));
    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @SuppressWarnings("unchecked")
    boolean testing(T... t) throws Exception;

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @SuppressWarnings("unchecked")
    default boolean test(T... t) {
        try {
            return testing(t);
        } catch (Exception e) {
            throw new LambdaInvokeException(e);
        }
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code false}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default SerArgsPred<T> and(SerArgsPred<? super T> other) {
        Objects.requireNonNull(other);
        return t -> test(t) && other.test(t);
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     * predicate
     */
    default SerArgsPred<T> negate() {
        return t -> !test(t);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default SerArgsPred<T> or(SerArgsPred<? super T> other) {
        Objects.requireNonNull(other);
        return t -> test(t) || other.test(t);
    }

}