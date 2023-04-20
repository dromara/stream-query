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

import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.core.variable.VariableHelper;

import java.util.*;
import java.util.function.UnaryOperator;

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
  public static <K, A, V> Steam<Map.Entry<K, Collection<V>>> oneToManyToOne(
      Map<K, Collection<A>> middleMap, Map<A, V> attachMap, UnaryOperator<Steam<V>>... unaryOperator) {
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
}
