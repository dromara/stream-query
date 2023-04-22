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
package org.dromara.streamquery.stream.core.stream;

import org.dromara.streamquery.stream.core.collection.Maps;
import org.dromara.streamquery.stream.core.lambda.function.SerBiCons;
import org.dromara.streamquery.stream.core.optional.Opp;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.logging.Logger;
import java.util.stream.*;

/**
 * 对Stream的封装和拓展，作者经对比了vavr、eclipse-collection、stream-ex以及其他语言的api，结合日常使用习惯，进行封装和拓展
 * Stream为集合提供了一些易用api，它让开发人员能使用声明式编程的方式去编写代码 它分为中间操作和结束操作 中间操作分为
 *
 * <ul>
 *   <li>无状态中间操作: 表示不用等待 所有元素的当前操作执行完 就可以执行的操作，不依赖之前历史操作的流的状态
 *   <li>有状态中间操作: 表示需要等待 所有元素的当前操作执行完 才能执行的操作,依赖之前历史操作的流的状态
 * </ul>
 *
 * <p>结束操作分为
 *
 * <ul>
 *   <li>短路结束操作: 表示不用等待 所有元素的当前操作执行完 就可以执行的操作
 *   <li>非短路结束操作: 表示需要等待 所有元素的当前操作执行完 才能执行的操作
 * </ul>
 *
 * <p>流只有在 结束操作 时才会真正触发执行以往的 中间操作
 *
 * <p>它分为串行流和并行流 并行流会使用拆分器{@link java.util.Spliterator}将操作拆分为多个异步任务{@link
 * java.util.concurrent.ForkJoinTask}执行 这些异步任务默认使用{@link java.util.concurrent.ForkJoinPool}线程池进行管理
 *
 * @author VampireAchao Cizai_
 * @see java.util.stream.Stream
 */
public class Steam<T> extends AbstractStreamWrapper<T, Steam<T>>
    implements Stream<T>, Iterable<T>, CollectableStream<T> {
  /** 代表不存在的下标, 一般用于并行流的下标, 或者未找到元素时的下标 */
  protected static final int NOT_FOUND_INDEX = -1;

  Steam(Stream<T> stream) {
    super(stream);
  }

  // --------------------------------------------------------------- Static method start

  /**
   * 返回{@code Steam}的建造器
   *
   * @param <T> 元素的类型
   * @return a stream builder
   */
  public static <T> Builder<T> builder() {
    return new Builder<T>() {
      private final Stream.Builder<T> builder = Stream.builder();

      @Override
      public void accept(T t) {
        builder.accept(t);
      }

      @Override
      public Steam<T> build() {
        return new Steam<>(builder.build());
      }
    };
  }

  /**
   * 返回空的串行流
   *
   * @param <T> 元素类型
   * @return 一个空的串行流
   */
  public static <T> Steam<T> empty() {
    return new Steam<>(Stream.empty());
  }

  /**
   * 返回包含单个元素的串行流
   *
   * @param t 单个元素
   * @param <T> 元素类型
   * @return 包含单个元素的串行流
   */
  public static <T> Steam<T> of(T t) {
    return new Steam<>(Stream.of(t));
  }

  /**
   * 返回包含指定元素的串行流
   *
   * @param values 指定元素
   * @param <T> 元素类型
   * @return 包含指定元素的串行流 从一个安全数组中创建流
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  public static <T> Steam<T> of(T... values) {
    return (values == null || values.length == 0) ? empty() : new Steam<>(Stream.of(values));
  }

  /**
   * 返回无限有序流 该流由 初始值 以及执行 迭代函数 进行迭代获取到元素
   *
   * <p>例如 {@code Steam.iterate(0, i -> i + 1)} 就可以创建从0开始，每次+1的无限流，使用{@link
   * Steam#limit(long)}可以限制元素个数
   */
  public static <T> Steam<T> iterate(final T seed, final UnaryOperator<T> f) {
    return new Steam<>(Stream.iterate(seed, f));
  }

  /**
   * 返回无限有序流 该流由 初始值 然后判断条件 以及执行 迭代函数 进行迭代获取到元素
   *
   * <p>例如 {@code Steam.iterate(0, i -> i < 3, i -> ++i)} 就可以创建包含元素0,1,2的流，使用{@link
   * Steam#limit(long)}可以限制元素个数
   *
   * @param <T> 元素类型
   * @param seed 初始值
   * @param hasNext 条件值
   * @param next 用上一个元素作为参数执行并返回一个新的元素
   * @return 无限有序流
   */
  public static <T> Steam<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next) {
    Objects.requireNonNull(next);
    Objects.requireNonNull(hasNext);
    Spliterator<T> spliterator =
        new Spliterators.AbstractSpliterator<T>(
            Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.IMMUTABLE) {
          T prev;
          boolean started;
          boolean finished;

          @Override
          public boolean tryAdvance(Consumer<? super T> action) {
            Objects.requireNonNull(action);
            if (finished) {
              return false;
            }
            T t;
            if (started) {
              t = next.apply(prev);
            } else {
              t = seed;
              started = true;
            }
            if (!hasNext.test(t)) {
              prev = null;
              finished = true;
              return false;
            }
            prev = t;
            action.accept(prev);
            return true;
          }

          @Override
          public void forEachRemaining(Consumer<? super T> action) {
            Objects.requireNonNull(action);
            if (finished) {
              return;
            }
            finished = true;
            T t = started ? next.apply(prev) : seed;
            prev = null;
            while (hasNext.test(t)) {
              action.accept(t);
              t = next.apply(t);
            }
          }
        };
    return new Steam<>(StreamSupport.stream(spliterator, false));
  }

  /** 返回无限串行无序流 其中每一个元素都由给定的{@code Supplier}生成 适用场景在一些生成常量流、随机元素等 */
  public static <T> Steam<T> generate(Supplier<T> s) {
    return new Steam<>(Stream.generate(s));
  }

  /**
   * 创建一个惰性拼接流，其元素是第一个流的所有元素，然后是第二个流的所有元素。 如果两个输入流都是有序的，则结果流是有序的，如果任一输入流是并行的，则结果流是并行的。
   * 当结果流关闭时，两个输入流的关闭处理程序都会被调用。
   *
   * <p>从重复串行流进行拼接时可能会导致深度调用链甚至抛出 {@code StackOverflowException}
   */
  public static <T> Steam<T> concat(Stream<? extends T> a, Stream<? extends T> b) {
    return new Steam<>(Stream.concat(a, b));
  }

  /**
   * 创建一个惰性拼接流，其元素是第一个流的所有元素，然后是第二个流的所有元素。 如果两个输入流都是有序的，则结果流是有序的，如果任一输入流是并行的，则结果流是并行的。
   * 当结果流关闭时，两个输入流的关闭处理程序都会被调用。
   *
   * <p>从重复串行流进行拼接时可能会导致深度调用链甚至抛出 {@code StackOverflowException}
   *
   * @param a 第一个流
   * @param b 第二个流
   * @param <T> 元素类型
   * @return 拼接后的流
   */
  public static <T> Steam<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
    return new Steam<>(Stream.concat(Steam.of(a), Steam.of(b)));
  }

  /**
   * 通过实现了{@link java.lang.Iterable}接口的对象创建串行流
   *
   * @param iterable 实现了{@link java.lang.Iterable}接口的对象
   * @param <T> 元素类型
   * @return 流
   */
  public static <T> Steam<T> of(Iterable<T> iterable) {
    return of(iterable, false);
  }

  /**
   * 通过传入的{@link java.lang.Iterable}创建流
   *
   * @param iterable {@link java.lang.Iterable}
   * @param parallel 是否并行
   * @param <T> 元素类型
   * @return 流
   */
  public static <T> Steam<T> of(Iterable<T> iterable, boolean parallel) {
    return Opp.of(iterable)
        .map(Iterable::spliterator)
        .map(spliterator -> StreamSupport.stream(spliterator, parallel))
        .map(Steam::new)
        .orElseGet(Steam::empty);
  }

  /**
   * 通过传入的{@link java.util.stream.Stream}创建流
   *
   * @param stream {@link java.util.stream.Stream}
   * @param <T> 元素类型
   * @return 流
   */
  public static <T> Steam<T> of(Stream<T> stream) {
    return new Steam<>(Objects.requireNonNull(stream));
  }

  /**
   * 拆分字符串，转换为串行流
   *
   * @param str 字符串
   * @param regex 正则
   * @param <T> a T class
   * @return 拆分后元素组成的流
   */
  public static <T extends CharSequence> Steam<String> split(T str, String regex) {
    return Opp.of(str)
        .map(CharSequence::toString)
        .map(s -> s.split(regex))
        .map(Steam::of)
        .orElseGet(Steam::empty);
  }

  // --------------------------------------------------------------- Static method end

  /**
   * 过滤元素，返回与 指定操作结果 匹配 指定值 的元素组成的流 这是一个无状态中间操作
   *
   * @param <R> 返回类型
   * @param mapper 操作
   * @param value 用来匹配的值
   * @return 与 指定操作结果 匹配 指定值 的元素组成的流
   */
  public <R> Steam<T> filter(Function<? super T, ? extends R> mapper, R value) {
    Objects.requireNonNull(mapper);
    return filter(e -> Objects.equals(Opp.of(e).map(mapper).get(), value));
  }

  /**
   * 过滤元素，返回与指定断言匹配的元素组成的流，断言带下标
   *
   * @param predicate 断言
   * @return 返回叠加过滤操作后的流
   */
  public Steam<T> filterIdx(BiPredicate<? super T, Integer> predicate) {
    Objects.requireNonNull(predicate);
    if (isParallel()) {
      return of(toIdxMap().entrySet())
          .parallel(isParallel())
          .filter(e -> predicate.test(e.getValue(), e.getKey()))
          .map(Map.Entry::getValue);
    } else {
      AtomicInteger index = new AtomicInteger(NOT_FOUND_INDEX);
      return filter(e -> predicate.test(e, index.incrementAndGet()));
    }
  }

  /**
   * 过滤掉空元素
   *
   * @return 过滤后的流
   */
  public Steam<T> nonNull() {
    return new Steam<>(stream.filter(Objects::nonNull));
  }

  /** 返回与指定函数将元素作为参数执行的结果组成的流 这是一个无状态中间操作 */
  @Override
  public <R> Steam<R> map(Function<? super T, ? extends R> mapper) {
    return new Steam<>(stream.map(mapper));
  }

  /**
   * 返回与指定函数将元素作为参数执行的结果组成的流，操作带下标
   *
   * @param mapper 指定的函数
   * @param <R> 函数执行后返回的类型
   * @return 返回叠加操作后的流
   */
  public <R> Steam<R> mapIdx(BiFunction<? super T, Integer, ? extends R> mapper) {
    Objects.requireNonNull(mapper);
    if (isParallel()) {
      return of(toIdxMap().entrySet())
          .parallel(isParallel())
          .map(e -> mapper.apply(e.getValue(), e.getKey()));
    } else {
      AtomicInteger index = new AtomicInteger(NOT_FOUND_INDEX);
      return map(e -> mapper.apply(e, index.incrementAndGet()));
    }
  }

  /**
   * peekIdx.
   *
   * @param action a {@link SerBiCons} object
   * @return a {@link Steam} object
   */
  public Steam<T> peekIdx(SerBiCons<? super T, Integer> action) {
    Objects.requireNonNull(action);
    if (isParallel()) {
      return of(toIdxMap().entrySet())
          .parallel(isParallel())
          .peek(e -> action.accept(e.getValue(), e.getKey()))
          .map(Map.Entry::getValue);
    } else {
      AtomicInteger index = new AtomicInteger(NOT_FOUND_INDEX);
      return peek(e -> action.accept(e, index.incrementAndGet()));
    }
  }

  /**
   * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流 这是一个无状态中间操作
   * 例如，将users里所有user的id和parentId组合在一起，形成一个新的流:
   *
   * <pre>{@code
   * Steam<Long> ids = Steam.of(users).flatMap(user -> Steam.of(user.getId(), user.getParentId()));
   * }</pre>
   */
  @Override
  public <R> Steam<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
    return new Steam<>(stream.flatMap(mapper));
  }

  /**
   * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流，操作带下标，并行流时下标永远为-1 这是一个无状态中间操作
   *
   * @param mapper 操作，返回流
   * @param <R> 拆分后流的元素类型
   * @return 返回叠加拆分操作后的流
   */
  public <R> Steam<R> flatIdx(
      BiFunction<? super T, Integer, ? extends Iterable<? extends R>> mapper) {
    Objects.requireNonNull(mapper);
    if (isParallel()) {
      return of(toIdxMap().entrySet())
          .parallel(isParallel())
          .flat(e -> mapper.apply(e.getValue(), e.getKey()));
    } else {
      AtomicInteger index = new AtomicInteger(NOT_FOUND_INDEX);
      return flat(e -> mapper.apply(e, index.incrementAndGet()));
    }
  }

  /**
   * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流 这是一个无状态中间操作
   * 例如，将users里所有user的id和parentId组合在一起，形成一个新的流:
   *
   * <pre>{@code
   * Steam<Long> ids = Steam.of(users).flatMap(user -> Steam.of(user.getId(), user.getParentId()));
   * }</pre>
   *
   * @param mapper 操作，返回可迭代对象
   * @param <R> 拆分后流的元素类型
   * @return 返回叠加拆分操作后的流
   */
  public <R> Steam<R> flat(Function<? super T, ? extends Iterable<? extends R>> mapper) {
    Objects.requireNonNull(mapper);
    return flatMap(w -> Opp.of(w).map(mapper).map(Steam::of).orElseGet(Steam::empty));
  }

  /**
   * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流，操作带一个方法，调用该方法可增加元素 这是一个无状态中间操作
   *
   * @param <R> 拆分后流的元素类型
   * @param mapper 操作，返回流
   * @return 返回叠加拆分操作后的流
   */
  public <R> Steam<R> mapMulti(BiConsumer<? super T, ? super Consumer<R>> mapper) {
    Objects.requireNonNull(mapper);
    return flatMap(
        e -> {
          Builder<R> buffer = Steam.builder();
          mapper.accept(e, buffer);
          return buffer.build();
        });
  }

  /**
   * 返回一个具有去重特征的流 非并行流(顺序流)下对于重复元素，保留遇到顺序中最先出现的元素，并行流情况下不能保证具体保留哪一个 这是一个有状态中间操作
   *
   * @param keyExtractor 去重依据
   * @param <F> a F class
   * @return 一个具有去重特征的流
   */
  public <F> Steam<T> distinct(Function<? super T, F> keyExtractor) {
    Objects.requireNonNull(keyExtractor);
    if (isParallel()) {
      Set<F> seenKeys = ConcurrentHashMap.newKeySet();
      // 标记是否出现过null值，用于保留第一个出现的null
      // 由于ConcurrentHashMap的key不能为null，所以用此变量来标记
      AtomicBoolean hasNull = new AtomicBoolean(false);
      return of(stream.filter(e -> {
        F key = keyExtractor.apply(e);
        if (key == null) {
          return !hasNull.getAndSet(true);
        } else {
          return seenKeys.add(key);
        }
      })).parallel();
    } else {
      Set<F> exists = new HashSet<>();
      return of(stream.filter(e -> exists.add(keyExtractor.apply(e))));
    }
  }

  /**
   * 返回叠加调用{@link java.io.PrintStream#println(Object)}打印出结果的流
   *
   * @return 返回叠加操作后的Steam
   */
  public Steam<T> log() {
    return peek(s -> Logger.getGlobal().info(String.valueOf(s)));
  }

  /**
   * 对流里面的每一个元素执行一个操作，操作带下标，并行流时下标永远为-1 这是一个终端操作
   *
   * @param action 操作
   */
  public void forEachIdx(BiConsumer<? super T, Integer> action) {
    Objects.requireNonNull(action);
    if (isParallel()) {
      of(toIdxMap().entrySet())
          .parallel(isParallel())
          .forEach(e -> action.accept(e.getValue(), e.getKey()));
    } else {
      AtomicInteger index = new AtomicInteger(NOT_FOUND_INDEX);
      stream.forEach(e -> action.accept(e, index.incrementAndGet()));
    }
  }

  /**
   * 对流里面的每一个元素按照顺序执行一个操作，操作带下标，并行流时下标永远为-1 这是一个终端操作
   *
   * @param action 操作
   */
  public void forEachOrderedIdx(BiConsumer<? super T, Integer> action) {
    Objects.requireNonNull(action);
    if (isParallel()) {
      stream.forEachOrdered(e -> action.accept(e, NOT_FOUND_INDEX));
    } else {
      AtomicInteger index = new AtomicInteger(NOT_FOUND_INDEX);
      stream.forEachOrdered(e -> action.accept(e, index.incrementAndGet()));
    }
  }

  /**
   * 获取与给定断言匹配的第一个元素
   *
   * @param predicate 断言
   * @return 与给定断言匹配的第一个元素
   */
  public Optional<T> findFirst(Predicate<? super T> predicate) {
    return stream.filter(predicate).findFirst();
  }

  /**
   * 获取与给定断言匹配的第一个元素的下标，并行流下标永远为-1
   *
   * @param predicate 断言
   * @return 与给定断言匹配的第一个元素的下标，如果不存在则返回-1
   */
  public Integer findFirstIdx(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate);
    return this.mapIdx((e, i) -> Maps.entry(i, e))
        .filter(e -> predicate.test(e.getValue()))
        .findFirst()
        .map(Map.Entry::getKey)
        .orElse(NOT_FOUND_INDEX);
  }

  /**
   * 获取最后一个元素
   *
   * @return 最后一个元素
   */
  public Optional<T> findLast() {
    if (isParallel()) {
      return Optional.of(toList()).filter(l -> !l.isEmpty()).map(l -> l.get(l.size() - 1));
    } else {
      AtomicReference<T> last = new AtomicReference<>(null);
      forEach(last::set);
      return Optional.ofNullable(last.get());
    }
  }

  /**
   * 获取与给定断言匹配的最后一个元素
   *
   * @param predicate 断言
   * @return 与给定断言匹配的最后一个元素
   */
  public Optional<T> findLast(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate);
    if (isParallel()) {
      return filter(predicate).findLast();
    } else {
      AtomicReference<T> last = new AtomicReference<>(null);
      forEach(
          e -> {
            if (predicate.test(e)) {
              last.set(e);
            }
          });
      return Optional.ofNullable(last.get());
    }
  }

  /**
   * 获取与给定断言匹配的最后一个元素的下标，并行流下标永远为-1
   *
   * @param predicate 断言
   * @return 与给定断言匹配的最后一个元素的下标，如果不存在则返回-1
   */
  public Integer findLastIdx(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate);
    return this.mapIdx((e, i) -> Maps.entry(i, e))
        .filter(e -> predicate.test(e.getValue()))
        .findLast()
        .map(Map.Entry::getKey)
        .orElse(NOT_FOUND_INDEX);
  }

  /**
   * 反转顺序
   *
   * @param comparator a {@link java.util.Comparator} object
   * @return 反转排序顺序
   */
  public Steam<T> reverseSorted(Comparator<T> comparator) {
    return sorted(comparator.reversed());
  }

  /**
   * 更改流的并行状态
   *
   * @param parallel 是否并行
   * @return 流
   */
  public Steam<T> parallel(boolean parallel) {
    return parallel ? parallel() : sequential();
  }

  /**
   * 与给定元素组成的流合并，成为新的流
   *
   * @param obj 元素
   * @return 流
   */
  public Steam<T> push(T obj) {
    return Steam.concat(this.stream, Stream.of(obj));
  }

  /**
   * 与给定元素组成的流合并，成为新的流
   *
   * @param obj 元素
   * @return 流
   */
  @SuppressWarnings("unchecked")
  public Steam<T> push(T... obj) {
    return concat(this.stream, of(obj));
  }

  /**
   * 给定元素组成的流与当前流合并，成为新的流
   *
   * @param obj 元素
   * @return 流
   */
  public Steam<T> unshift(T obj) {
    return Steam.concat(Stream.of(obj), this.stream);
  }

  /**
   * 给定元素组成的流与当前流合并，成为新的流
   *
   * @param obj 元素
   * @return 流
   */
  @SafeVarargs
  public final Steam<T> unshift(T... obj) {
    return concat(of(obj), this.stream);
  }

  /**
   * 获取流中指定下标的元素，如果是负数，则从最后一个开始数起
   *
   * @param idx 下标
   * @return 指定下标的元素
   */
  public Optional<T> at(Integer idx) {
    if (Objects.isNull(idx)) {
      return Optional.empty();
    }
    Optional<List<T>> listOpt = Optional.of(toList());
    if (idx > -1) {
      return listOpt.filter(l -> idx < l.size()).map(l -> l.get(idx));
    }
    return listOpt.filter(l -> -idx <= l.size()).map(l -> l.get(l.size() + idx));
  }

  /** 构建一个{@link Steam}实例 */
  @Override
  protected Steam<T> wrap(Stream<T> stream) {
    return new Steam<>(stream);
  }

  /** hashcode */
  @Override
  public int hashCode() {
    return stream.hashCode();
  }

  /** equals */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Stream) {
      return stream.equals(obj);
    }
    return false;
  }

  /** toString */
  @Override
  public String toString() {
    return stream.toString();
  }

  /**
   * 将 现有元素 与 给定迭代器中对应位置的元素 使用 zipper 转换为新的元素，并返回新元素组成的流<br>
   * 新流的数量等于旧流元素的数量<br>
   * 使用 zipper 转换时, 如果对应位置上已经没有other元素，则other元素为null<br>
   *
   * @param other 给定的迭代器
   * @param zipper 两个元素的合并器
   * @param <U> 给定的迭代对象类型
   * @param <R> 合并后的结果对象类型
   * @return 合并后的结果对象的流
   */
  public <U, R> Steam<R> zip(
      Iterable<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
    Objects.requireNonNull(zipper);
    Map<Integer, T> idxIdentityMap = toIdxMap();
    Map<Integer, U> idxOtherMap = Steam.of(other).toIdxMap();
    if (idxIdentityMap.size() <= idxOtherMap.size()) {
      return Steam.of(idxIdentityMap.keySet(), isParallel())
          .map(k -> zipper.apply(idxIdentityMap.get(k), idxOtherMap.get(k)));
    }
    return Steam.of(idxOtherMap.keySet(), isParallel())
        .map(k -> zipper.apply(idxIdentityMap.get(k), idxOtherMap.get(k)));
  }

  /**
   * 过滤同类型集合中某一操作相同值的数据 {@link Steam#filter(Function, Object)}
   *
   * @param others 另一可迭代对象
   * @param mapper 操作
   * @param <R> 操作返回值类型
   * @return 过滤同类型集合中某一操作相同值的数据
   */
  public <R> Steam<T> filterContains(Function<? super T, ? extends R> mapper, Iterable<T> others) {
    List<? extends R> otherList = Steam.of(others).map(mapper).toList();
    return filter(a -> Objects.nonNull(a) && otherList.contains(mapper.apply(a)));
  }

  /**
   * 类似js的<a
   * href="https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Array/splice">splice</a>函数
   *
   * @param start 起始下标
   * @param deleteCount 删除个数，正整数
   * @param items 放入值
   * @return 操作后的流
   */
  @SuppressWarnings("unchecked")
  public Steam<T> splice(int start, int deleteCount, T... items) {
    List<T> list = toList();
    final int size = list.size();
    // 从后往前查找
    if (start < 0) {
      start += size;
    } else if (start >= size) {
      // 直接在尾部追加，不删除
      start = size;
      deleteCount = 0;
    }
    // 起始位置 加上 删除的数量 超过 数据长度，需要重新计算需要删除的数量
    if (start + deleteCount > size) {
      deleteCount = size - start;
    }

    // 新列表的长度
    final int newSize = size - deleteCount + items.length;
    List<T> resList = list;
    // 新列表的长度 大于 旧列表，创建新列表
    if (newSize > size) {
      resList = new ArrayList<>(newSize);
      resList.addAll(list);
    }
    // 需要删除的部分
    if (deleteCount > 0) {
      resList.subList(start, start + deleteCount).clear();
    }
    // 新增的部分
    if (items.length > 0) {
      resList.addAll(start, Arrays.asList(items));
    }
    return Steam.of(resList);
  }

  /**
   * 按指定长度切分为双层流
   *
   * <p>形如：[1,2,3,4,5] -> [[1,2], [3,4], [5,6]]
   *
   * @param batchSize 指定长度, 正整数
   * @return 切好的流
   */
  public Steam<Steam<T>> split(final int batchSize) {
    List<T> list = toList();
    final int size = list.size();
    // 指定长度 大于等于 列表长度
    if (size <= batchSize) {
      // 返回第一层只有单个元素的双层流，形如：[[1,2,3,4,5]]
      return Steam.<Steam<T>>of(of(list, isParallel()));
    }
    return Steam.iterate(0, i -> i < size, i -> i + batchSize)
        .map(skip -> of(list.subList(skip, Math.min(size, skip + batchSize)), isParallel()))
        .parallel(isParallel());
  }

  /**
   * 按指定长度切分为元素为list的流
   *
   * <p>形如：[1,2,3,4,5] -> [[1,2], [3,4], [5,6]]
   *
   * @param batchSize 指定长度, 正整数
   * @return 切好的流
   */
  public Steam<List<T>> splitList(final int batchSize) {
    return split(batchSize).map(Steam::toList);
  }

  /**
   * Steam Builder
   *
   * @param <T> the type of stream elements
   */
  public interface Builder<T> extends Consumer<T> {

    /**
     * Adds an element to the stream being built.
     *
     * @param t the element to add
     * @throws IllegalStateException if the builder has already transitioned to the built state
     */
    @Override
    void accept(T t);

    /**
     * Adds an element to the stream being built.
     *
     * @param t the element to add
     * @return {@code this} builder
     * @throws IllegalStateException if the builder has already transitioned to the built state
     * @implSpec The default implementation behaves as if:
     *     <pre>{@code
     * accept(t)
     * return this;
     * }</pre>
     */
    default Builder<T> add(T t) {
      accept(t);
      return this;
    }

    /**
     * Builds the stream, transitioning this builder to the built state. An {@code
     * IllegalStateException} is thrown if there are further attempts to operate on the builder
     * after it has entered the built state.
     *
     * @return the built stream
     * @throws IllegalStateException if the builder has already transitioned to the built state
     */
    Steam<T> build();
  }

  /** 过滤元素，返回与指定断言匹配的元素组成的流 这是一个无状态中间操作 */
  @Override
  public Steam<T> filter(Predicate<? super T> predicate) {
    return super.filter(predicate);
  }

  /** 和{@link Stream#map(Function)}一样，只不过函数的返回值必须为int类型 这是一个无状态中间操作 */
  @Override
  public IntStream mapToInt(ToIntFunction<? super T> mapper) {
    return super.mapToInt(mapper);
  }

  /** 和{@link Stream#map(Function)}一样，只不过函数的返回值必须为long类型 这是一个无状态中间操作 */
  @Override
  public LongStream mapToLong(ToLongFunction<? super T> mapper) {
    return super.mapToLong(mapper);
  }

  /** 和{@link Stream#map(Function)}一样，只不过函数的返回值必须为double类型 这是一个无状态中间操作 */
  @Override
  public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
    return super.mapToDouble(mapper);
  }

  /** 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流 这是一个无状态中间操作 */
  @Override
  public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
    return super.flatMapToInt(mapper);
  }

  /** 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流 这是一个无状态中间操作 */
  @Override
  public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
    return super.flatMapToLong(mapper);
  }

  /** 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流 这是一个无状态中间操作 */
  @Override
  public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
    return super.flatMapToDouble(mapper);
  }

  /**
   * 返回一个元素按指定的{@link Comparator}排序的流 如果此流的元素不是{@code Comparable} ，则在执行终端操作时可能会抛出{@code
   * java.lang.ClassCastException} 对于顺序流，排序是稳定的。 对于无序流，没有稳定性保证。 这是一个有状态中间操作
   */
  @Override
  public Steam<T> sorted(Comparator<? super T> comparator) {
    return super.sorted(comparator);
  }

  /**
   * 返回与指定函数将元素作为参数执行后组成的流。 这是一个无状态中间操作
   *
   * @apiNote 该方法存在的意义主要是用来调试 当你需要查看经过操作管道某处的元素，可以执行以下操作:
   *     <pre>{@code
   * .of("one", "two", "three", "four")
   *     .filter(e -> e.length() > 3)
   *     .peek(e -> log.info("Filtered value: " + e))
   *     .map(String::toUpperCase)
   *     .peek(e -> log.info("Mapped value: " + e))
   *     .collect(Collectors.toList());
   * }</pre>
   */
  @Override
  public Steam<T> peek(Consumer<? super T> action) {
    return super.peek(action);
  }

  /** 对流里面的每一个元素执行一个操作 这是一个终端操作 */
  @Override
  public void forEach(Consumer<? super T> action) {
    super.forEach(action);
  }

  /** 对流里面的每一个元素按照顺序执行一个操作 这是一个终端操作 */
  @Override
  public void forEachOrdered(Consumer<? super T> action) {
    super.forEachOrdered(action);
  }

  /**
   * 返回一个包含此流元素的指定的数组，例如以下代码编译正常，但运行时会抛出 {@link ArrayStoreException}
   *
   * <pre>
   * {@code String[] strings = Stream.<Integer>builder().add(1).build().toArray(String[]::new); }
   * </pre>
   */
  @Override
  public <A> A[] toArray(IntFunction<A[]> generator) {
    return super.toArray(generator);
  }

  /**
   * 对元素进行聚合，并返回聚合后的值，相当于在for循环里写sum=sum+ints[i] 这是一个终端操作<br>
   * 求和、最小值、最大值、平均值和转换成一个String字符串均为聚合操作 例如这里对int进行求和可以写成：
   *
   * <pre>{@code
   * Integer sum = integers.reduce(0, (a, b) -> a+b);
   * }</pre>
   *
   * <p>或者写成:
   *
   * <pre>{@code
   * Integer sum = integers.reduce(0, Integer::sum);
   * }</pre>
   */
  @Override
  public T reduce(T identity, BinaryOperator<T> accumulator) {
    return super.reduce(identity, accumulator);
  }

  /**
   * 对元素进行聚合，并返回聚合后用 {@link Optional}包裹的值，相当于在for循环里写sum=sum+ints[i] 该操作相当于：
   *
   * <pre>{@code
   * boolean foundAny = false;
   * T result = null;
   * for (T element : this stream) {
   *     if (!foundAny) {
   *         foundAny = true;
   *         result = element;
   *     }
   *     else
   *         result = accumulator.apply(result, element);
   * }
   * return foundAny ? Optional.of(result) : Optional.empty();
   * }</pre>
   *
   * <p>但它不局限于顺序执行，例如并行流等情况下 这是一个终端操作<br>
   * 例如以下场景抛出 NPE ：
   *
   * <pre>{@code
   * Optional<Integer> reduce = Stream.<Integer>builder().add(1).add(1).build().reduce((a, b) -> null);
   * }</pre>
   *
   * @see #reduce(Object, BinaryOperator)
   * @see #min(Comparator)
   * @see #max(Comparator)
   */
  @Override
  public Optional<T> reduce(BinaryOperator<T> accumulator) {
    return super.reduce(accumulator);
  }

  /**
   * 对元素进行聚合，并返回聚合后的值，并行流时聚合拿到的初始值不稳定 这是一个终端操作
   *
   * @see #reduce(BinaryOperator)
   * @see #reduce(Object, BinaryOperator)
   */
  @Override
  public <U> U reduce(
      U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
    return super.reduce(identity, accumulator, combiner);
  }

  /** 对元素进行收集，并返回收集后的容器 这是一个终端操作 */
  @Override
  public <R> R collect(
      Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
    return super.collect(supplier, accumulator, combiner);
  }

  /** 对元素进行收集，并返回收集后的元素 这是一个终端操作 */
  @Override
  public <R, A> R collect(Collector<? super T, A, R> collector) {
    return super.collect(collector);
  }

  /** 获取最小值 */
  @Override
  public Optional<T> min(Comparator<? super T> comparator) {
    return super.min(comparator);
  }

  /** 获取最大值 */
  @Override
  public Optional<T> max(Comparator<? super T> comparator) {
    return super.max(comparator);
  }

  /** 判断是否有任何一个元素满足给定断言 */
  @Override
  public boolean anyMatch(Predicate<? super T> predicate) {
    return super.anyMatch(predicate);
  }

  /** 判断是否所有元素满足给定断言 */
  @Override
  public boolean allMatch(Predicate<? super T> predicate) {
    return super.allMatch(predicate);
  }

  /** 判断是否没有元素满足给定断言 */
  @Override
  public boolean noneMatch(Predicate<? super T> predicate) {
    return super.noneMatch(predicate);
  }

  /** 在流关闭时执行操作 */
  @Override
  public Steam<T> onClose(Runnable closeHandler) {
    return super.onClose(closeHandler);
  }
}
