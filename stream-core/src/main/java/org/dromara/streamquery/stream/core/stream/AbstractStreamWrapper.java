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

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * {@link Stream}的包装类，用于基于一个已有的流实例进行扩展
 *
 * @author huangchengxing
 */
abstract class AbstractStreamWrapper<T, I extends Stream<T>> implements Stream<T>, Iterable<T> {

  /** 原始的流实例 */
  protected Stream<T> stream;

  /**
   * 创建一个流包装器
   *
   * @param stream 包装的流对象
   */
  protected AbstractStreamWrapper(Stream<T> stream) {
    Objects.requireNonNull(stream, "stream must not null");
    this.stream = stream;
  }

  /** 过滤元素，返回与指定断言匹配的元素组成的流 这是一个无状态中间操作 */
  @Override
  public I filter(Predicate<? super T> predicate) {
    return wrap(stream.filter(predicate));
  }

  /** 和{@link Stream#map(Function)}一样，只不过函数的返回值必须为int类型 这是一个无状态中间操作 */
  @Override
  public IntStream mapToInt(ToIntFunction<? super T> mapper) {
    return stream.mapToInt(mapper);
  }

  /** 和{@link Stream#map(Function)}一样，只不过函数的返回值必须为long类型 这是一个无状态中间操作 */
  @Override
  public LongStream mapToLong(ToLongFunction<? super T> mapper) {
    return stream.mapToLong(mapper);
  }

  /** 和{@link Stream#map(Function)}一样，只不过函数的返回值必须为double类型 这是一个无状态中间操作 */
  @Override
  public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
    return stream.mapToDouble(mapper);
  }

  /** 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流 这是一个无状态中间操作 */
  @Override
  public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
    return stream.flatMapToInt(mapper);
  }

  /** 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流 这是一个无状态中间操作 */
  @Override
  public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
    return stream.flatMapToLong(mapper);
  }

  /** 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流 这是一个无状态中间操作 */
  @Override
  public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
    return stream.flatMapToDouble(mapper);
  }

  /** 返回一个具有去重特征的流 非并行流(顺序流)下对于重复元素，保留遇到顺序中最先出现的元素，并行流情况下不能保证具体保留哪一个 这是一个有状态中间操作 */
  @Override
  public I distinct() {
    return wrap(stream.distinct());
  }

  /**
   * 返回一个元素按自然顺序排序的流 如果此流的元素不是{@code Comparable} ，则在执行终端操作时可能会抛出 {@code
   * java.lang.ClassCastException} 对于顺序流，排序是稳定的。 对于无序流，没有稳定性保证。 这是一个有状态中间操作
   */
  @Override
  public I sorted() {
    return wrap(stream.sorted());
  }

  /**
   * 返回一个元素按指定的{@link Comparator}排序的流 如果此流的元素不是{@code Comparable} ，则在执行终端操作时可能会抛出{@code
   * java.lang.ClassCastException} 对于顺序流，排序是稳定的。 对于无序流，没有稳定性保证。 这是一个有状态中间操作
   */
  @Override
  public I sorted(Comparator<? super T> comparator) {
    return wrap(stream.sorted(comparator));
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
  public I peek(Consumer<? super T> action) {
    return wrap(stream.peek(action));
  }

  /** 返回截取后面一些元素的流 这是一个短路状态中间操作 */
  @Override
  public I limit(long maxSize) {
    return wrap(stream.limit(maxSize));
  }

  /** 返回丢弃前面n个元素后的剩余元素组成的流，如果当前元素个数小于n，则返回一个元素为空的流 这是一个有状态中间操作 */
  @Override
  public I skip(long n) {
    return wrap(stream.skip(n));
  }

  /** 对流里面的每一个元素执行一个操作 这是一个终端操作 */
  @Override
  public void forEach(Consumer<? super T> action) {
    stream.forEach(action);
  }

  /** 对流里面的每一个元素按照顺序执行一个操作 这是一个终端操作 */
  @Override
  public void forEachOrdered(Consumer<? super T> action) {
    stream.forEachOrdered(action);
  }

  /** 返回一个包含此流元素的数组 这是一个终端操作 */
  @Override
  public Object[] toArray() {
    return stream.toArray();
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
    return stream.toArray(generator);
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
    return stream.reduce(identity, accumulator);
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
    return stream.reduce(accumulator);
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
    return stream.reduce(identity, accumulator, combiner);
  }

  /** 对元素进行收集，并返回收集后的容器 这是一个终端操作 */
  @Override
  public <R> R collect(
      Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
    return stream.collect(supplier, accumulator, combiner);
  }

  /** 对元素进行收集，并返回收集后的元素 这是一个终端操作 */
  @Override
  public <R, A> R collect(Collector<? super T, A, R> collector) {
    return stream.collect(collector);
  }

  /** 获取最小值 */
  @Override
  public Optional<T> min(Comparator<? super T> comparator) {
    return stream.min(comparator);
  }

  /** 获取最大值 */
  @Override
  public Optional<T> max(Comparator<? super T> comparator) {
    return stream.max(comparator);
  }

  /** 返回流元素个数 */
  @Override
  public long count() {
    return stream.count();
  }

  /** 判断是否有任何一个元素满足给定断言 */
  @Override
  public boolean anyMatch(Predicate<? super T> predicate) {
    return stream.anyMatch(predicate);
  }

  /** 判断是否所有元素满足给定断言 */
  @Override
  public boolean allMatch(Predicate<? super T> predicate) {
    return stream.allMatch(predicate);
  }

  /** 判断是否没有元素满足给定断言 */
  @Override
  public boolean noneMatch(Predicate<? super T> predicate) {
    return stream.noneMatch(predicate);
  }

  /** 获取第一个元素 */
  @Override
  public Optional<T> findFirst() {
    return stream.findFirst();
  }

  /** 考虑性能，随便取一个，这里不是随机取一个，是随便取一个 */
  @Override
  public Optional<T> findAny() {
    return stream.findAny();
  }

  /** 返回流的迭代器 */
  @Override
  public Iterator<T> iterator() {
    return stream.iterator();
  }

  /** 返回流的拆分器 */
  @Override
  public Spliterator<T> spliterator() {
    return stream.spliterator();
  }

  /** 返回流的并行状态 */
  @Override
  public boolean isParallel() {
    return stream.isParallel();
  }

  /** 返回一个串行流，该方法可以将并行流转换为串行流 */
  @Override
  public I sequential() {
    return wrap(stream.sequential());
  }

  /** 将流转换为并行 */
  @Override
  public I parallel() {
    return wrap(stream.parallel());
  }

  /**
   * 返回一个无序流(无手动排序)
   *
   * <p>标记一个流是不在意元素顺序的, 在并行流的某些情况下可以提高性能
   */
  @Override
  public I unordered() {
    return wrap(stream.unordered());
  }

  /** 在流关闭时执行操作 */
  @Override
  public I onClose(Runnable closeHandler) {
    return wrap(stream.onClose(closeHandler));
  }

  /**
   * 关闭流
   *
   * @see AutoCloseable#close()
   */
  @Override
  public void close() {
    stream.close();
  }

  /**
   * 根据一个原始的流，返回一个新包装类实例
   *
   * @param stream 流
   * @return 实现类
   */
  protected abstract I wrap(Stream<T> stream);
}
