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
package org.dromara.streamquery.stream.core.collection;

import org.dromara.streamquery.stream.core.enums.JreEnum;
import org.dromara.streamquery.stream.core.lambda.function.SerThiFunc;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.core.stream.collector.Collective;
import org.dromara.streamquery.stream.core.variable.VariableHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;

/**
 * Maps class.
 *
 * @author VampireAchao
 * @since 2022/10/21 16:35
 */
public class Maps {

  private Maps() {
    /* Do not new me! */
  }

  /**
   * of.
   *
   * @param <K> a K class
   * @param <V> a V class
   * @return a {@link java.util.Map} object
   */
  public static <K, V> Map<K, V> of() {
    return new HashMap<>(1 << 4);
  }

  /**
   * of.
   *
   * @param initialCapacity a int
   * @param <K> a K class
   * @param <V> a V class
   * @return a {@link java.util.Map} object
   */
  public static <K, V> Map<K, V> ofSize(int initialCapacity) {
    return new HashMap<>(initialCapacity);
  }

  /**
   * of.
   *
   * @param k a K object
   * @param v a V object
   * @param <K> a K class
   * @param <V> a V class
   * @return a {@link java.util.Map} object
   */
  public static <K, V> Map<K, V> of(K k, V v) {
    final Map<K, V> map = ofSize(1);
    map.put(k, v);
    return map;
  }

  /**
   * of.
   *
   * @param k a K object
   * @param v a V object
   * @param k1 a K object
   * @param v1 a V object
   * @param <K> a K class
   * @param <V> a V class
   * @return a {@link java.util.Map} object
   */
  public static <K, V> Map<K, V> of(K k, V v, K k1, V v1) {
    final Map<K, V> map = ofSize(1 << 1);
    map.put(k, v);
    map.put(k1, v1);
    return map;
  }

  /**
   * of.
   *
   * @param k a K object
   * @param v a V object
   * @param k1 a K object
   * @param v1 a V object
   * @param k2 a K object
   * @param v2 a V object
   * @param <K> a K class
   * @param <V> a V class
   * @return a {@link java.util.Map} object
   */
  public static <K, V> Map<K, V> of(K k, V v, K k1, V v1, K k2, V v2) {
    final Map<K, V> map = ofSize(1 << 2);
    map.put(k, v);
    map.put(k1, v1);
    map.put(k2, v2);
    return map;
  }

  /**
   * entry.
   *
   * @param key a K object
   * @param value a V object
   * @param <K> a K class
   * @param <V> a V class
   * @return a {@link java.util.Map.Entry} object
   */
  public static <K, V> Map.Entry<K, V> entry(K key, V value) {
    return new AbstractMap.SimpleImmutableEntry<>(key, value);
  }

  /**
   * oneToManyToOne.
   *
   * @param middleMap a {@link java.util.Map} object
   * @param attachMap a {@link java.util.Map} object
   * @param unaryOperator a {@link java.util.function.UnaryOperator} object
   * @param <K> a K class
   * @param <A> a A class
   * @param <V> a V class
   * @return a {@link Steam} object
   */
  @SafeVarargs
  public static <K, A, V, C extends Collection<A>> Steam<Map.Entry<K, List<V>>> oneToManyToOne(
      Map<K, C> middleMap, Map<A, V> attachMap, UnaryOperator<Steam<V>>... unaryOperator) {
    return Steam.of(middleMap.entrySet())
        .map(
            e ->
                entry(
                    e.getKey(),
                    VariableHelper.<UnaryOperator<Steam<V>>>first(
                            unaryOperator, UnaryOperator::identity)
                        .apply(Steam.of(e.getValue()).map(attachMap::get))
                        .toList()));
  }

  /**
   * oneToOneToOne.
   *
   * @param middleMap a {@link java.util.Map} object
   * @param attachMap a {@link java.util.Map} object
   * @param unaryOperator a {@link java.util.function.UnaryOperator} object
   * @param <K> a K class
   * @param <A> a A class
   * @param <V> a V class
   * @return a {@link Steam} object
   */
  @SafeVarargs
  public static <K, A, V> Steam<Map.Entry<K, V>> oneToOneToOne(
      Map<K, A> middleMap, Map<A, V> attachMap, UnaryOperator<V>... unaryOperator) {
    return Steam.of(middleMap.entrySet())
        .map(
            e ->
                entry(
                    e.getKey(),
                    VariableHelper.<UnaryOperator<V>>first(unaryOperator, UnaryOperator::identity)
                        .apply(attachMap.get(e.getValue()))));
  }

  /**
   * oneToOneToMany.
   *
   * @param middleMap a {@link java.util.Map} object
   * @param attachMap a {@link java.util.Map} object
   * @param unaryOperator a {@link java.util.function.UnaryOperator} object
   * @param <K> a K class
   * @param <A> a A class
   * @param <V> a V class
   * @return a {@link Steam} object
   */
  @SafeVarargs
  public static <K, A, V> Steam<Map.Entry<K, List<V>>> oneToOneToMany(
      Map<K, A> middleMap, Map<A, List<V>> attachMap, UnaryOperator<Steam<V>>... unaryOperator) {
    return Steam.of(middleMap.entrySet())
        .map(
            e ->
                entry(
                    e.getKey(),
                    VariableHelper.<UnaryOperator<Steam<V>>>first(
                            unaryOperator, UnaryOperator::identity)
                        .apply(Steam.of(e.getValue()).flat(attachMap::get))
                        .toList()));
  }

  /**
   * 获取一个空的Map
   *
   * @param <K> key
   * @param <V> value
   * @return 空的Map
   */
  public static <K, V> Map<K, V> empty() {
    return Collections.emptyMap();
  }

  /**
   * merge 合并两个Map得到一个新的Map，如果key相同，使用mergeFunction处理value
   *
   * @param m1 map1
   * @param m2 map2
   * @param mergeFunction 合并操作
   * @param <K> a K object
   * @param <V> a V object
   * @return 合并后的map
   */
  public static <K, V> Map<K, V> merge(
      Map<K, V> m1, Map<K, V> m2, SerThiFunc<K, V, V, V> mergeFunction) {
    Map<K, V> result = new HashMap<>(m1);
    m2.forEach(
        (key, value) -> result.merge(key, value, (v1, v2) -> mergeFunction.apply(key, v1, v2)));
    return result;
  }

  /**
   * 根据value是否符合条件过滤出符合条件的key
   *
   * @param map map
   * @param biPredicate 条件操作
   * @param <K> a K object
   * @param <V> a V object
   * @return 过滤后的map
   */
  public static <K, V> Map<K, V> filter(Map<K, V> map, BiPredicate<K, V> biPredicate) {
    return Steam.of(map.entrySet())
        .filter(e -> biPredicate.test(e.getKey(), e.getValue()))
        .collect(Collective.entryToMap());
  }

  /**
   * 将具有多个级别的嵌套 {@link java.util.Map} 平展为单级 {@link java.util.Map} 使用指定分隔符从原始键值连接。
   *
   * @param nestedMap a {@link java.util.Map} object
   * @param delimiter a {@link java.lang.String} object
   * @param <K> a K object
   * @param <V> a V object
   * @return a {@link java.util.Map} object
   */
  public static <K, V> Map<String, V> flatten(
      Map<String, Map<String, V>> nestedMap, String delimiter) {
    return Steam.of(nestedMap.entrySet())
        .flat(
            entry ->
                Steam.of(entry.getValue().entrySet())
                    .map(
                        innerEntry ->
                            Maps.entry(
                                entry.getKey() + delimiter + innerEntry.getKey(),
                                innerEntry.getValue())))
        .collect(Collective.entryToMap());
  }

  /**
   * computeIfAbsent.
   *
   * @param map a {@link java.util.Map} object
   * @param key a K object
   * @param mappingFunction a {@link java.util.function.Function} object
   * @param <K> a K class
   * @param <V> a V class
   * @return a V object
   */
  public static <K, V> V computeIfAbsent(
      Map<K, V> map, K key, Function<? super K, ? extends V> mappingFunction) {
    if (JreEnum.JAVA_8.isCurrentVersion() && map instanceof ConcurrentHashMap) {
      return Opp.of(map.get(key))
          .orElseGet(
              () -> {
                V value = mappingFunction.apply(key);
                map.put(key, value);
                return value;
              });
    }
    return map.computeIfAbsent(key, mappingFunction);
  }
}
