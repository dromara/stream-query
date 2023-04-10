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
package org.dromara.streamquery.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.core.stream.collector.Collective;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collector;

/**
 * 一对多
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/23 17:40
 */
@SuppressWarnings("unchecked")
public class OneToMany<T, K extends Serializable & Comparable<? super K>, V>
    extends BaseQueryHelper<OneToMany<T, K, V>, T, K, V> {

  /**
   * Constructor for OneToMany.
   *
   * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   */
  public OneToMany(SFunction<T, K> keyFunction) {
    super(keyFunction);
  }

  /**
   * of.
   *
   * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @param <K> a K class
   * @return a {@link OneToMany} object
   */
  public static <T, K extends Serializable & Comparable<? super K>> OneToMany<T, K, T> of(
      SFunction<T, K> keyFunction) {
    return new OneToMany<>(keyFunction);
  }

  /**
   * value.
   *
   * @param valueFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <R> a R class
   * @return a {@link OneToMany} object
   */
  public <R> OneToMany<T, K, R> value(SFunction<T, R> valueFunction) {
    attachDouble(valueFunction);
    return (OneToMany<T, K, R>) this;
  }

  /**
   * query.
   *
   * @return a {@link java.util.Map} object
   */
  public Map<K, List<V>> query() {
    return query(HashMap::new, Collective.toList());
  }

  /**
   * query.
   *
   * @param mapFactory a {@link java.util.function.IntFunction} object
   * @param downstream a {@link java.util.stream.Collector} object
   * @param <A> a A class
   * @param <R> a R class
   * @param <M> a M class
   * @return a M object
   */
  public <A, R, M extends Map<K, R>> M query(
      IntFunction<M> mapFactory, Collector<? super V, A, R> downstream) {
    List<T> list = Database.list(wrapper);
    return Steam.of(list)
        .parallel(isParallel)
        .peek(peekConsumer)
        .group(
            keyFunction,
            () -> mapFactory.apply(list.size()),
            Collective.mapping(valueOrIdentity(), downstream));
  }
}
