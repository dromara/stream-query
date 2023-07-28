/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.streamquery.stream.core.lambda.function;

import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 可序列化的Predicate
 *
 * @param <T> the type of the input to the operation
 * @author VampireAchao Cizai_
 * @see Predicate
 */
@FunctionalInterface
public interface SerArgsPred<T> extends Serializable {

  /**
   * multi
   *
   * @param predicates lambda
   * @param <T> 类型
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
   * @param <T> 类型
   * @return lambda
   */
  @SafeVarargs
  static <T> SerArgsPred<T> multiOr(SerArgsPred<T>... predicates) {
    return Stream.of(predicates).reduce(SerArgsPred::or).orElseGet(() -> o -> false);
  }

  /**
   * Returns a predicate that tests if two arguments are equal according to {@link
   * java.util.Objects#equals(Object, Object)}.
   *
   * @param <T> the type of arguments to the predicate
   * @param targetRef the object reference with which to compare for equality, which may be {@code
   *     null}
   * @return a predicate that tests if two arguments are equal according to {@link
   *     java.util.Objects#equals(Object, Object)}
   */
  static <T> SerArgsPred<T> isEqual(Object... targetRef) {
    return (null == targetRef) ? Objects::isNull : objects -> Arrays.deepEquals(objects, targetRef);
  }

  /**
   * Evaluates this predicate on the given argument.
   *
   * @param t the input argument
   * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
   * @throws java.lang.Exception maybe throw exception
   */
  @SuppressWarnings("unchecked")
  boolean testing(T... t) throws Throwable;

  /**
   * Evaluates this predicate on the given argument.
   *
   * @param t the input argument
   * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
   */
  @SuppressWarnings("unchecked")
  default boolean test(T... t) {
    try {
      return testing(t);
    } catch (Throwable e) {
      throw new LambdaInvokeException(e);
    }
  }

  /**
   * Returns a composed predicate that represents a short-circuiting logical AND of this predicate
   * and another. When evaluating the composed predicate, if this predicate is {@code false}, then
   * the {@code other} predicate is not evaluated.
   *
   * <p>Any exceptions thrown during evaluation of either predicate are relayed to the caller; if
   * evaluation of this predicate throws an exception, the {@code other} predicate will not be
   * evaluated.
   *
   * @param other a predicate that will be logically-ANDed with this predicate
   * @return a composed predicate that represents the short-circuiting logical AND of this predicate
   *     and the {@code other} predicate
   * @throws java.lang.NullPointerException if other is null
   */
  default SerArgsPred<T> and(SerArgsPred<? super T> other) {
    Objects.requireNonNull(other);
    return t -> test(t) && other.test(t);
  }

  /**
   * Returns a predicate that represents the logical negation of this predicate.
   *
   * @return a predicate that represents the logical negation of this predicate
   */
  default SerArgsPred<T> negate() {
    return t -> !test(t);
  }

  /**
   * Returns a composed predicate that represents a short-circuiting logical OR of this predicate
   * and another. When evaluating the composed predicate, if this predicate is {@code true}, then
   * the {@code other} predicate is not evaluated.
   *
   * <p>Any exceptions thrown during evaluation of either predicate are relayed to the caller; if
   * evaluation of this predicate throws an exception, the {@code other} predicate will not be
   * evaluated.
   *
   * @param other a predicate that will be logically-ORed with this predicate
   * @return a composed predicate that represents the short-circuiting logical OR of this predicate
   *     and the {@code other} predicate
   * @throws java.lang.NullPointerException if other is null
   */
  default SerArgsPred<T> or(SerArgsPred<? super T> other) {
    Objects.requireNonNull(other);
    return t -> test(t) || other.test(t);
  }
}
