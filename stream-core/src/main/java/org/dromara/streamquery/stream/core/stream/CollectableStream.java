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

import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.stream.collector.Collective;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * 允许实现类直接跳过{@link java.util.stream.Stream#collect(Collector)}使用{@link
 * java.util.stream.Collector}中一些常用方法
 *
 * @param <T> 元素类型
 * @author huangchengxing
 */
public interface CollectableStream<T> extends Stream<T> {

  // region ============ join ============

  /**
   * 返回拼接后的字符串
   *
   * @return 拼接后的字符串
   */
  default String join() {
    return join("");
  }

  /**
   * 返回拼接后的字符串
   *
   * @param delimiter 分隔符
   * @return 拼接后的字符串
   */
  default String join(CharSequence delimiter) {
    return join(delimiter, "", "");
  }

  /**
   * 返回拼接后的字符串
   *
   * @param delimiter 分隔符
   * @param prefix 前缀
   * @param suffix 后缀
   * @return 拼接后的字符串
   */
  default String join(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
    return map(String::valueOf).collect(Collective.joining(delimiter, prefix, suffix));
  }

  // endregion

  // region ============ to collection ============

  /**
   * 转换成集合
   *
   * @param collectionFactory 集合工厂(可以是集合构造器)
   * @param <C> 集合类型
   * @return 集合
   */
  default <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory) {
    return collect(Collective.toCollection(collectionFactory));
  }

  /**
   * 转换为{@link java.util.ArrayList}
   *
   * @return 集合
   */
  default List<T> toList() {
    return collect(Collective.toList());
  }

  /**
   * 换为不可变集合
   *
   * @return 集合
   */
  default List<T> toUnmodifiableList() {
    List<T> result = toList();
    return Collections.unmodifiableList(result);
  }

  /**
   * 转换为HashSet
   *
   * @return 集合
   */
  default Set<T> toSet() {
    return collect(Collective.toSet());
  }

  /**
   * 换为不可变集合
   *
   * @return 集合
   */
  default Set<T> toUnmodifiableSet() {
    return Collections.unmodifiableSet(toSet());
  }

  // endregion

  // region ============ to map ============

  /**
   * 转换为map，key为给定操作执行后的返回值,value为当前元素
   *
   * @param keyMapper 指定的key操作
   * @param <K> key类型
   * @return map
   */
  default <K> Map<K, T> toMap(Function<? super T, ? extends K> keyMapper) {
    return toMap(keyMapper, Function.identity());
  }

  /**
   * 转换为map，key,value为给定操作执行后的返回值
   *
   * @param keyMapper 指定的key操作
   * @param valueMapper 指定value操作
   * @param <K> key类型
   * @param <U> value类型
   * @return map
   */
  default <K, U> Map<K, U> toMap(
      Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
    return toMap(keyMapper, valueMapper, (l, r) -> r);
  }

  /**
   * 转换为map，key为下标,value为给定操作执行后的返回值
   *
   * @param valueMapper 指定value操作
   * @param <U> value类型
   * @return map
   */
  default <U> Map<Integer, U> toIdxMap(Function<? super T, ? extends U> valueMapper) {
    AtomicInteger index = new AtomicInteger(Steam.NOT_FOUND_INDEX);
    return Steam.of(toList()).toMap(e -> index.incrementAndGet(), valueMapper, (l, r) -> r);
  }

  /**
   * 转换为map，key为下标,value为元素
   *
   * @return map
   */
  default Map<Integer, T> toIdxMap() {
    return toIdxMap(Function.identity());
  }

  /**
   * 转换为不可变map，key,value为给定操作执行后的返回值
   *
   * @param keyMapper 指定的key操作
   * @param valueMapper 指定value操作
   * @param <K> key类型
   * @param <U> value类型
   * @return map
   */
  default <K, U> Map<K, U> toUnmodifiableMap(
      Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
    Map<K, U> result = toMap(keyMapper, valueMapper);
    return Collections.unmodifiableMap(result);
  }

  /**
   * 转换为map，key,value为给定操作执行后的返回值
   *
   * @param keyMapper 指定的key操作
   * @param valueMapper 指定value操作
   * @param mergeFunction 合并操作
   * @param <K> key类型
   * @param <U> value类型
   * @return map
   */
  default <K, U> Map<K, U> toMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper,
      BinaryOperator<U> mergeFunction) {
    return toMap(keyMapper, valueMapper, mergeFunction, HashMap::new);
  }

  /**
   * 转换为不可变map，key,value为给定操作执行后的返回值
   *
   * @param keyMapper 指定的key操作
   * @param valueMapper 指定value操作
   * @param mergeFunction 合并操作
   * @param <K> key类型
   * @param <U> value类型
   * @return map
   */
  default <K, U> Map<K, U> toUnmodifiableMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper,
      BinaryOperator<U> mergeFunction) {
    Map<K, U> result = toMap(keyMapper, valueMapper, mergeFunction, HashMap::new);
    return Collections.unmodifiableMap(result);
  }

  /**
   * 转换为map，key,value为给定操作执行后的返回值
   *
   * @param keyMapper 指定的key操作
   * @param valueMapper 指定value操作
   * @param mergeFunction 合并操作
   * @param mapSupplier map工厂
   * @param <K> key类型
   * @param <U> value类型
   * @param <M> map类型
   * @return map
   */
  default <K, U, M extends Map<K, U>> M toMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper,
      BinaryOperator<U> mergeFunction,
      Supplier<M> mapSupplier) {
    return collect(Collective.toMap(keyMapper, valueMapper, mergeFunction, mapSupplier));
  }

  // endregion

  // region ============ to zip ============

  /**
   * 与给定的可迭代对象转换成map，key为现有元素，value为给定可迭代对象迭代的元素<br>
   * 至少包含全部的key，如果对应位置上的value不存在，则为null
   *
   * @param other 可迭代对象
   * @param <R> 可迭代对象迭代的元素类型
   * @return map，key为现有元素，value为给定可迭代对象迭代的元素;<br>
   *     至少包含全部的key，如果对应位置上的value不存在，则为null;<br>
   *     如果key重复, 则保留最后一个关联的value;<br>
   */
  default <R> Map<T, R> toZip(Iterable<R> other) {
    // value对象迭代器
    final Iterator<R> iterator =
        Opp.of(other).map(Iterable::iterator).orElseGet(Collections::emptyIterator);
    if (isParallel()) {
      List<T> keyList = toList();
      final Map<T, R> map = new HashMap<>(keyList.size());
      for (T key : keyList) {
        map.put(key, iterator.hasNext() ? iterator.next() : null);
      }
      return map;
    } else {
      return toMap(Function.identity(), e -> iterator.hasNext() ? iterator.next() : null);
    }
  }

  // endregion

  // region ============ group ============

  /**
   * 通过给定分组依据进行分组
   *
   * @param classifier 分组依据
   * @param <K> 实体中的分组依据对应类型，也是Map中key的类型
   * @return map
   */
  default <K> Map<K, List<T>> group(Function<? super T, ? extends K> classifier) {
    return group(classifier, Collective.toList());
  }

  /**
   * 通过给定分组依据进行分组
   *
   * @param classifier 分组依据
   * @param downstream 下游操作
   * @param <K> 实体中的分组依据对应类型，也是Map中key的类型
   * @param <D> 下游操作对应返回类型，也是Map中value的类型
   * @param <A> 下游操作在进行中间操作时对应类型
   * @return map
   */
  default <K, A, D> Map<K, D> group(
      Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
    return group(classifier, HashMap::new, downstream);
  }

  /**
   * 通过给定分组依据进行分组
   *
   * @param classifier 分组依据
   * @param mapFactory 提供的map
   * @param downstream 下游操作
   * @param <K> 实体中的分组依据对应类型，也是Map中key的类型
   * @param <D> 下游操作对应返回类型，也是Map中value的类型
   * @param <A> 下游操作在进行中间操作时对应类型
   * @param <M> 最后返回结果Map类型
   * @return map
   */
  default <K, D, A, M extends Map<K, D>> M group(
      Function<? super T, ? extends K> classifier,
      Supplier<M> mapFactory,
      Collector<? super T, A, D> downstream) {
    return collect(Collective.groupingBy(classifier, mapFactory, downstream));
  }

  /**
   * 根据给定判断条件分组
   *
   * @param predicate 判断条件
   * @return map
   */
  default Map<Boolean, List<T>> partition(Predicate<T> predicate) {
    return partition(predicate, ArrayList::new);
  }

  /**
   * 根据给定判断条件分组
   *
   * @param predicate 判断条件
   * @param collFactory 提供的集合
   * @param <C> a C class
   * @return map
   */
  default <C extends Collection<T>> Map<Boolean, C> partition(
      Predicate<T> predicate, Supplier<C> collFactory) {
    return partition(predicate, Collective.toCollection(collFactory));
  }

  /**
   * 根据给定判断条件分组
   *
   * @param predicate 判断条件
   * @param downstream 下游操作
   * @param <R> 返回值类型
   * @return map
   */
  default <R> Map<Boolean, R> partition(Predicate<T> predicate, Collector<T, ?, R> downstream) {
    return collect(Collective.partitioningBy(predicate, downstream));
  }

  // endregion

}
