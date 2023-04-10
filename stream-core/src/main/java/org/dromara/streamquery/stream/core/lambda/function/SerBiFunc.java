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
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * SerBiFunc
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @author VampireAchao Cizai_
 * @since 2022/6/8
 */
@FunctionalInterface
public interface SerBiFunc<T, U, R> extends BiFunction<T, U, R>, Serializable {

  /**
   * Applies this function to the given arguments.
   *
   * @param t the first function argument
   * @param u the second function argument
   * @return the function result
   * @throws java.lang.Exception if any.
   */
  @SuppressWarnings("all")
  R applying(T t, U u) throws Throwable;

  /** Applies this function to the given arguments. */
  @Override
  default R apply(T t, U u) {
    try {
      return this.applying(t, u);
    } catch (Throwable e) {
      throw new LambdaInvokeException(e);
    }
  }

  /**
   * Returns a composed function that first applies this function to its input, and then applies the
   * {@code after} function to the result. If evaluation of either function throws an exception, it
   * is relayed to the caller of the composed function.
   *
   * @param <V> the type of output of the {@code after} function, and of the composed function
   * @param after the function to apply after this function is applied
   * @return a composed function that first applies this function and then applies the {@code after}
   *     function
   * @throws java.lang.NullPointerException if after is null
   */
  default <V> SerBiFunc<T, U, V> andThen(SerFunc<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (T t, U u) -> after.apply(this.apply(t, u));
  }
}
