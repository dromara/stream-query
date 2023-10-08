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
package org.dromara.streamquery.stream.core.optional;

import org.dromara.streamquery.stream.core.lambda.LambdaExecutable;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.lambda.function.SerCons;
import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.lambda.function.SerPred;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * 拓展hutool中的Opt
 *
 * @param <T> 包裹里元素的类型
 * @author VampireAchao Cizai_
 * @see java.util.Optional
 */
public class Opp<T> {

  /** 一个空的{@code Opp} */
  protected static final Opp<?> EMPTY = new Opp<>(null);

  /** 包裹里实际的元素 */
  protected final T value;

  protected Throwable throwable;

  /**
   * {@code Opp}的构造函数
   *
   * @param value 包裹里的元素
   */
  protected Opp(T value) {
    this.value = value;
  }

  /**
   * 返回一个空的{@code Opp}
   *
   * @param <T> 包裹里元素的类型
   * @return Opp
   */
  public static <T> Opp<T> empty() {
    @SuppressWarnings("unchecked")
    final Opp<T> t = (Opp<T>) EMPTY;
    return t;
  }

  /**
   * 返回一个包裹里元素不可能为空的{@code Opp}
   *
   * @param value 包裹里的元素
   * @param <T> 包裹里元素的类型
   * @return 一个包裹里元素不可能为空的 {@code Opp}
   * @throws java.lang.NullPointerException 如果传入的元素为空，抛出 {@code NPE}
   */
  public static <T> Opp<T> required(T value) {
    return new Opp<>(Objects.requireNonNull(value));
  }

  /**
   * 返回一个包裹里元素可能为空的{@code Opp}
   *
   * @param value 传入需要包裹的元素
   * @param <T> 包裹里元素的类型
   * @return 一个包裹里元素可能为空的 {@code Opp}
   */
  public static <T> Opp<T> of(T value) {
    return value == null ? empty() : new Opp<>(value);
  }

  /**
   * of.
   *
   * @param optional a {@link java.util.Optional} object
   * @param <T> a T class
   * @return a {@link Opp} object
   */
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static <T> Opp<T> of(Optional<T> optional) {
    return Opp.<Object>of(optional).flattedMap(SerFunc.cast());
  }

  /**
   * 返回一个包裹里元素可能为空的{@code Opp}，额外判断了空字符串的情况
   *
   * @param value 传入需要包裹的元素
   * @param <T> a T class
   * @return 一个包裹里元素可能为空，或者为空字符串的 {@code Opp}
   */
  public static <T extends CharSequence> Opp<T> ofStr(T value) {
    return Opp.of(value).filter(str -> !str.toString().trim().isEmpty());
  }

  /**
   * + 返回一个包裹里{@code List}集合可能为空的{@code Opp}，额外判断了集合内元素为空的情况
   *
   * @param <T> 包裹里元素的类型
   * @param <R> 集合值类型
   * @param value 传入需要包裹的元素
   * @return 一个包裹里元素可能为空的 {@code Opp}
   */
  public static <T, R extends Collection<T>> Opp<R> ofColl(R value) {
    if (value == null || value.isEmpty()) {
      return empty();
    }
    for (T t : value) {
      if (t != null) {
        return new Opp<>(value);
      }
    }
    // 集合中元素全部为空
    return empty();
  }

  /**
   * ofTry.
   *
   * @param callable 操作
   * @param <T> 类型
   * @return 操作执行后的值
   */
  public static <T> Opp<T> ofTry(Callable<T> callable) {
    return Opp.ofTry(callable, Throwable.class);
  }

  /**
   * 如果当前操作发生异常的话只抛出不在想要捕获的范围内的异常，如果在想要捕获的范围内就捕获到Opp对象中
   *
   * @param callable 操作
   * @param exceptionType 指定想要捕获的若干个异常类型
   * @param <T> 类型
   * @param exceptionTypes a {@link java.lang.Class} object
   * @return 操作执行后的值如果发生异常则返回一个空的Opp对象并且产生的异常保存在这个Opp对象中
   */
  @SafeVarargs
  public static <T> Opp<T> ofTry(
      Callable<T> callable,
      Class<? extends Throwable> exceptionType,
      Class<? extends Throwable>... exceptionTypes) {
    try {
      return Opp.of(callable.call());
    } catch (Throwable e) {
      if (Steam.of(exceptionTypes).unshift(exceptionType).noneMatch(clazz -> clazz.isInstance(e))) {
        throw new IllegalArgumentException(e);
      }
      Opp<T> empty = new Opp<>(null);
      empty.throwable = e;
      return empty;
    }
  }

  /**
   * 如果当前操作发生异常的话只抛出在想要抛出的范围内的异常，如果不在在想要抛出的范围内就捕获到Opp对象中
   *
   * @param callable 操作
   * @param exceptionTypes 不想捕获的异常...
   * @param <T> 类型
   * @return 操作执行后的值如果发生我们想抛出的异常则直接抛出，如果发生的异常不是我们想抛出的则直接捕获返回一个空的Opp对象并且产生的异常保存在这个Opp对象中
   */
  @SafeVarargs
  public static <T> Opp<T> notTry(
      Callable<T> callable, Class<? extends Throwable>... exceptionTypes) {
    try {
      return Opp.of(callable.call());
    } catch (Throwable e) {
      if (Steam.of(exceptionTypes).anyMatch(clazz -> clazz.isInstance(e))) {
        throw new IllegalArgumentException(e);
      }
      Opp<T> empty = new Opp<>(null);
      empty.throwable = e;
      return empty;
    }
  }

  /**
   * 返回包裹里的元素，取不到则为{@code null}，注意！！！此处和{@link java.util.Optional#get()}不同的一点是本方法并不会抛出{@code
   * NoSuchElementException} 如果元素为空，则返回{@code null}，如果需要一个绝对不能为{@code null}的值，则使用{@link
   * #orElseThrow()}
   *
   * <p>如果需要一个绝对不能为 {@code null}的值，则使用{@link #orElseThrow()}
   * 做此处修改的原因是，有时候我们确实需要返回一个null给前端，并且这样的时候并不少见 而使用 {@code .orElse(null)}需要写整整12个字符，用{@code
   * .get()}就只需要6个啦
   *
   * @return 包裹里的元素，有可能为{@code null}
   */
  public T get() {
    return this.value;
  }

  /**
   * get.
   *
   * @param mapper a {@link java.util.function.Function} object
   * @param <R> a R class
   * @return a R object
   */
  public <R> R get(Function<T, R> mapper) {
    return map(mapper).orElse(null);
  }

  /**
   * 判断包裹里元素的值是否不存在，不存在为 {@code true}，否则为{@code false}
   *
   * @return 包裹里元素的值不存在 则为 {@code true}，否则为{@code false}
   */
  public boolean isEmpty() {
    return value == null;
  }

  /**
   * 获取异常<br>
   * 当调用 {@link #ofTry(Callable)}时，异常信息不会抛出，而是保存，调用此方法获取抛出的异常
   *
   * @return 异常
   */
  public Throwable getThrowable() {
    return this.throwable;
  }

  /**
   * 是否失败<br>
   * 当调用 {@link #ofTry(Callable)}时，抛出异常则表示失败
   *
   * @return 是否失败
   */
  public boolean isFail() {
    return null != this.throwable;
  }

  /**
   * 判断包裹里元素的值是否存在，存在为 {@code true}，否则为{@code false}
   *
   * @return 包裹里元素的值存在为 {@code true}，否则为{@code false}
   */
  public boolean isPresent() {
    return value != null;
  }

  /**
   * 如果包裹里的值存在，就执行传入的操作({@link java.util.function.Consumer#accept})
   *
   * <p>例如如果值存在就打印结果
   *
   * <pre>{@code
   * Opp.ofNullable("Hello Hutool!").ifPresent(Console::log);
   * }</pre>
   *
   * @param action 你想要执行的操作
   * @return this
   * @throws java.lang.NullPointerException 如果包裹里的值存在，但你传入的操作为{@code null}时抛出
   */
  public Opp<T> ifPresent(Consumer<? super T> action) {
    if (isPresent()) {
      action.accept(value);
    }
    return this;
  }

  /**
   * 判断包裹里的值存在并且与给定的条件是否满足 ({@link SerFunc#apply}执行结果是否为true) 如果满足条件则返回本身
   * 不满足条件或者元素本身为空时返回一个返回一个空的{@code Opp}
   *
   * @param predicate 给定的条件
   * @return 如果满足条件则返回本身, 不满足条件或者元素本身为空时返回一个返回一个空的{@code Opp}
   * @throws java.lang.NullPointerException 如果给定的条件为 {@code null}，抛出{@code NPE}
   */
  public Opp<T> filter(SerPred<? super T> predicate) {
    Objects.requireNonNull(predicate);
    if (isEmpty()) {
      return empty();
    } else {
      return predicate.test(value) ? this : empty();
    }
  }

  /**
   * 如果包裹里的值存在，就执行传入的操作({@link java.util.function.Function#apply})并返回一个包裹了该操作返回值的{@code Opp}
   * 如果不存在，返回一个空的{@code Opp}
   *
   * @param mapper 值存在时执行的操作
   * @param <U> 操作返回值的类型
   * @return 如果包裹里的值存在，就执行传入的操作({@link java.util.function.Function#apply})并返回一个包裹了该操作返回值的{@code
   *     Opp}， 如果不存在，返回一个空的{@code Opp}
   * @throws java.lang.NullPointerException 如果给定的操作为 {@code null}，抛出 {@code NPE}
   */
  public <U> Opp<U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper);
    if (isEmpty()) {
      return empty();
    } else {
      return Opp.of(mapper.apply(value));
    }
  }

  /**
   * 如果包裹里的值存在，就执行传入的操作({@link java.util.function.Function#apply})并返回该操作返回值 如果不存在，返回一个空的{@code Opp}
   * 和 {@link Opp#map}的区别为 传入的操作返回值必须为 Opp
   *
   * @param mapper 值存在时执行的操作
   * @param <U> 操作返回值的类型
   * @return 如果包裹里的值存在，就执行传入的操作({@link java.util.function.Function#apply})并返回该操作返回值
   *     如果不存在，返回一个空的{@code Opp}
   * @throws java.lang.NullPointerException 如果给定的操作为 {@code null}或者给定的操作执行结果为 {@code null}，抛出 {@code
   *     NPE}
   */
  public <U> Opp<U> flatMap(Function<? super T, ? extends Opp<? extends U>> mapper) {
    Objects.requireNonNull(mapper);
    if (isEmpty()) {
      return empty();
    } else {
      @SuppressWarnings("unchecked")
      final Opp<U> r = (Opp<U>) mapper.apply(value);
      return Objects.requireNonNull(r);
    }
  }

  /**
   * 如果包裹里的值存在，就执行传入的操作({@link java.util.function.Function#apply})并返回该操作返回值 如果不存在，返回一个空的{@code Opp}
   * 和 {@link Opp#map}的区别为 传入的操作返回值必须为 {@link java.util.Optional}
   *
   * @param mapper 值存在时执行的操作
   * @param <U> 操作返回值的类型
   * @return 如果包裹里的值存在，就执行传入的操作({@link java.util.function.Function#apply})并返回该操作返回值
   *     如果不存在，返回一个空的{@code Opp}
   * @throws java.lang.NullPointerException 如果给定的操作为 {@code null}或者给定的操作执行结果为 {@code null}，抛出 {@code
   *     NPE}
   * @see Optional#flatMap(Function)
   */
  public <U> Opp<U> flattedMap(Function<? super T, Optional<? extends U>> mapper) {
    Objects.requireNonNull(mapper);
    if (isEmpty()) {
      return empty();
    } else {
      return of(mapper.apply(value).orElse(null));
    }
  }

  /**
   * 如果包裹里的值存在，则返回该值，如果不存在返回默认的值
   *
   * @param f 存在时执行的操作
   * @param supplier 如果不存在则返回的默认值
   * @return 如果包裹里的值存在，则返回该值，如果不存在返回默认的值
   */
  public <U> U fold(Function<? super T, ? extends U> f, Supplier<? extends U> supplier) {
    return this.<U>map(f).orElseGet(supplier);
  }

  /**
   * 如果包裹里元素的值存在，就执行对应的操作，并返回本身 如果不存在，返回一个空的{@code Opp}
   *
   * <p>属于 {@link #ifPresent}的链式拓展
   *
   * @param action 值存在时执行的操作
   * @return this
   * @throws java.lang.NullPointerException 如果值存在，并且传入的操作为 {@code null}
   */
  public Opp<T> peek(Consumer<T> action) throws NullPointerException {
    Objects.requireNonNull(action);
    if (isEmpty()) {
      return Opp.empty();
    }
    action.accept(value);
    return this;
  }

  /**
   * 如果包裹里元素的值存在，就执行对应的操作集，并返回本身 如果不存在，返回一个空的{@code Opp}
   *
   * <p>属于 {@link #ifPresent}的链式拓展
   *
   * <p>属于 {@link #peek(Consumer)}的动态拓展
   *
   * @param actions 值存在时执行的操作，动态参数，可传入数组，当数组为一个空数组时并不会抛出 {@code NPE}
   * @return this
   * @throws java.lang.NullPointerException 如果值存在，并且传入的操作集中的元素为 {@code null}
   */
  @SafeVarargs
  public final Opp<T> peeks(Consumer<T>... actions) throws NullPointerException {
    return peek(Stream.of(actions).reduce(Consumer::andThen).orElseGet(() -> o -> {}));
  }

  /**
   * 如果传入的lambda入参类型一致，或者是父类，就执行，目前不支持子泛型
   *
   * @param action 入参类型一致，或者是父类，就执行的操作
   * @param <U> 操作入参类型
   * @return 如果传入的lambda入参类型一致，就执行对应的操作，并返回本身
   */
  public <U> Opp<T> typeOfPeek(SerCons<U> action) {
    return ofTry(
            () -> {
              LambdaExecutable resolve = LambdaHelper.resolve(action);
              Type[] types = resolve.getParameterTypes();
              return types[types.length - 1];
            })
        .flatMap(type -> typeOfPeek(type, action));
  }

  /**
   * 如果传入的lambda入参类型一致，或者是父类，就执行并获取返回值，目前不支持子泛型
   *
   * @param mapper 入参类型一致，或者是父类，就执行的操作
   * @param <U> 操作入参类型
   * @param <R> 操作返回值类型
   * @return 如果传入的lambda入参类型一致，就执行并获取返回值
   */
  public <U, R> Opp<R> typeOfMap(SerFunc<U, R> mapper) {
    return ofTry(
            () -> {
              Type[] types = LambdaHelper.resolve(mapper).getParameterTypes();
              return types[types.length - 1];
            })
        .flatMap(type -> typeOfMap(type, mapper));
  }

  /**
   * 判断如果传入的类型一致，或者是父类，并且包裹里的值存在，并且与给定的条件是否满足 ({@link java.util.function.Predicate#test}执行结果是否为true)
   * 如果满足条件则返回本身 不满足条件或者元素本身为空时返回一个返回一个空的{@code Opp}
   *
   * @param predicate 给定的条件
   * @param <U> a U class
   * @return 如果满足条件则返回本身, 不满足条件或者元素本身为空时返回一个返回一个空的{@code Opp}
   * @throws java.lang.NullPointerException 如果给定的条件为 {@code null}，抛出{@code NPE}
   */
  public <U> Opp<T> typeOfFilter(SerPred<U> predicate) {
    return ofTry(
            () -> {
              Type[] types = LambdaHelper.resolve(predicate).getParameterTypes();
              return types[types.length - 1];
            })
        .flatMap(type -> typeOfFilter(type, predicate));
  }

  /**
   * 如果传入的类型一致，或者是父类，就执行，支持子泛型
   *
   * @param type 类型
   * @param action 入参类型一致，或者是父类，就执行的操作
   * @param <U> 操作入参类型
   * @return 如果传入的lambda入参类型一致，就执行对应的操作，并返回本身
   */
  @SuppressWarnings("unchecked")
  public <U> Opp<T> typeOfPeek(Type type, SerCons<U> action) {
    return of(type)
        .flatMap(
            t -> filter(obj -> ReflectHelper.isInstance(obj, t)).peek(v -> action.accept((U) v)));
  }

  /**
   * 如果传入的类型一致，或者是父类，就执行并获取返回值，目前不支持子泛型
   *
   * @param type 类型
   * @param mapper 入参类型一致，或者是父类，就执行的操作
   * @param <U> 操作入参类型
   * @param <R> 操作返回值类型
   * @return 如果传入的lambda入参类型一致，就执行并获取返回值
   */
  @SuppressWarnings("unchecked")
  public <U, R> Opp<R> typeOfMap(Type type, SerFunc<U, R> mapper) {
    return of(type)
        .flatMap(
            t -> filter(obj -> ReflectHelper.isInstance(obj, t)).map(v -> mapper.apply((U) v)));
  }

  /**
   * 判断如果传入的类型一致，或者是父类，并且包裹里的值存在，并且与给定的条件是否满足 ({@link java.util.function.Predicate#test}执行结果是否为true)
   * 如果满足条件则返回本身 不满足条件或者元素本身为空时返回一个返回一个空的{@code Opp}
   *
   * @param type 类型
   * @param predicate 给定的条件
   * @param <U> a U class
   * @return 如果满足条件则返回本身, 不满足条件或者元素本身为空时返回一个返回一个空的{@code Opp}
   * @throws java.lang.NullPointerException 如果给定的条件为 {@code null}，抛出{@code NPE}
   */
  @SuppressWarnings("unchecked")
  public <U> Opp<T> typeOfFilter(Type type, SerPred<U> predicate) {
    return of(type)
        .flatMap(
            t ->
                filter(obj -> ReflectHelper.isInstance(obj, t)).filter(v -> predicate.test((U) v)));
  }

  /**
   * 如果包裹里元素的值存在，就返回本身，如果不存在，则使用传入的操作执行后获得的 {@code Opp}
   *
   * @param supplier 不存在时的操作
   * @return 如果包裹里元素的值存在，就返回本身，如果不存在，则使用传入的函数执行后获得的 {@code Opp}
   * @throws java.lang.NullPointerException 如果传入的操作为空，或者传入的操作执行后返回值为空，则抛出 {@code NPE}
   */
  public Opp<T> or(Supplier<? extends Opp<? extends T>> supplier) {
    Objects.requireNonNull(supplier);
    if (isPresent()) {
      return this;
    } else {
      @SuppressWarnings("unchecked")
      final Opp<T> r = (Opp<T>) supplier.get();
      return Objects.requireNonNull(r);
    }
  }

  /**
   * 如果包裹里元素的值存在，就返回一个包含该元素的 {@link java.util.stream.Stream}, 否则返回一个空元素的 {@link
   * java.util.stream.Stream}
   *
   * <p>该方法能将 Opp 中的元素传递给 {@link java.util.stream.Stream}
   *
   * <pre>{@code
   * Stream<Opp<T>> os = ..
   * Stream<T> s = os.flatMap(Opp::stream)
   * }</pre>
   *
   * @return 返回一个包含该元素的 {@link java.util.stream.Stream}或空的 {@link java.util.stream.Stream}
   */
  public Steam<T> steam() {
    if (isEmpty()) {
      return Steam.empty();
    } else {
      return Steam.of(Stream.of(value));
    }
  }

  /**
   * 如果包裹里元素的值存在，则返回该值，否则返回传入的{@code other}
   *
   * @param other 元素为空时返回的值，有可能为 {@code null}.
   * @return 如果包裹里元素的值存在，则返回该值，否则返回传入的{@code other}
   */
  public T orElse(T other) {
    return isPresent() ? value : other;
  }

  /**
   * 如果包裹里元素的值存在，则返回该值，否则执行传入的操作
   *
   * @param action 值不存在时执行的操作
   * @param <R> a R class
   * @return 如果包裹里元素的值存在，则返回该值，否则执行传入的操作
   * @throws java.lang.NullPointerException 如果值不存在，并且传入的操作为 {@code null}
   */
  public <R extends Runnable> T orElseRun(R action) {
    if (isPresent()) {
      return value;
    } else {
      action.run();
      return null;
    }
  }

  /**
   * 异常则返回另一个可选值
   *
   * @param other 可选值
   * @return 如果未发生异常，则返回该值，否则返回传入的{@code other}
   */
  public T failOrElse(T other) {
    return isFail() ? other : value;
  }

  /**
   * 如果包裹里元素的值存在，则返回该值，否则返回传入的操作执行后的返回值
   *
   * @param supplier 值不存在时需要执行的操作，返回一个类型与 包裹里元素类型 相同的元素
   * @return 如果包裹里元素的值存在，则返回该值，否则返回传入的操作执行后的返回值
   * @throws java.lang.NullPointerException 如果之不存在，并且传入的操作为空，则抛出 {@code NPE}
   */
  public T orElseGet(Supplier<? extends T> supplier) {
    return isPresent() ? value : supplier.get();
  }

  /**
   * 如果包裹里的值存在，则返回该值，否则抛出 {@code NoSuchElementException}
   *
   * @return 返回一个不为 {@code null} 的包裹里的值
   */
  public T orElseThrow() {
    return orElseThrow(
        () ->
            Objects.nonNull(throwable)
                ? new RuntimeException(throwable)
                : new NoSuchElementException("No value present"));
  }

  /**
   * 如果包裹里的值存在，则返回该值，否则执行传入的操作，获取异常类型的返回值并抛出
   *
   * <p>往往是一个包含无参构造器的异常 例如传入{@code IllegalStateException::new}
   *
   * @param <X> 异常类型
   * @param exceptionSupplier 值不存在时执行的操作，返回值继承 {@link java.lang.Throwable}
   * @return 包裹里不能为空的值
   * @throws X 如果值不存在
   * @throws java.lang.NullPointerException 如果值不存在并且 传入的操作为 {@code null}或者操作执行后的返回值为{@code null}
   */
  public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    if (isPresent()) {
      return value;
    } else {
      throw exceptionSupplier.get();
    }
  }

  /**
   * 转换为 {@link java.util.Optional}对象
   *
   * @return {@link java.util.Optional}对象
   */
  public Optional<T> toOptional() {
    return Optional.ofNullable(this.value);
  }

  /**
   * 判断传入参数是否与 {@code Opp}相等 在以下情况下返回true
   *
   * <ul>
   *   <li>它也是一个 {@code Opp} 并且
   *   <li>它们包裹住的元素都为空 或者
   *   <li>它们包裹住的元素之间相互 {@code equals()}
   * </ul>
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Opp)) {
      return false;
    }

    final Opp<?> other = (Opp<?>) obj;
    return Objects.equals(value, other.value);
  }

  /** 如果包裹内元素为空，则返回0，否则返回元素的 {@code hashcode} */
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  /** 返回包裹内元素调用{@code toString()}的结果，不存在则返回{@code null} */
  @Override
  public String toString() {
    return isPresent() ? value.toString() : null;
  }

  /**
   * filterEqual.
   *
   * @param value a R object
   * @param <R> a R class
   * @return a {@link Opp} object
   */
  public <R> Opp<T> filterEqual(R value) {
    return filter(SerPred.isEqual(value));
  }

  /**
   * isEqual.
   *
   * @param value a R object
   * @param <R> a R class
   * @return a boolean
   */
  public <R> boolean isEqual(R value) {
    return filterEqual(value).isPresent();
  }

  /**
   * is.
   *
   * @param predicate a {@link java.util.function.Predicate} object
   * @return a boolean
   */
  public boolean is(SerPred<T> predicate) {
    return filter(predicate).isPresent();
  }

  /**
   * zip.
   *
   * @param other a {@link Opp} object
   * @param mapper a {@link java.util.function.BiFunction} object
   * @param <R> a R class
   * @return a {@link Opp} object
   */
  public <R> Opp<R> zip(Opp<R> other, BiFunction<T, R, R> mapper) {
    Objects.requireNonNull(mapper);
    if (isEmpty() || other.isEmpty()) {
      return empty();
    } else {
      return Opp.of(mapper.apply(value, other.value));
    }
  }

  /**
   * zipOrSelf.
   *
   * @param other a {@link Opp} object
   * @param mapper a {@link java.util.function.BinaryOperator} object
   * @return a {@link Opp} object
   */
  public Opp<T> zipOrSelf(Opp<T> other, BinaryOperator<T> mapper) {
    Objects.requireNonNull(mapper);
    if (isEmpty()) {
      return empty();
    } else if (other.isEmpty()) {
      return this;
    } else {
      return Opp.of(mapper.apply(value, other.value));
    }
  }
}
