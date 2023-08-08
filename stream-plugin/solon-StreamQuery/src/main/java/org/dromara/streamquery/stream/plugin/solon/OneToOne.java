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
package org.dromara.streamquery.stream.plugin.solon;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.dromara.streamquery.stream.core.lambda.function.SerBiOp;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * 一对一
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/20
 */
@SuppressWarnings("unchecked")
public class OneToOne<T, K extends Serializable & Comparable<? super K>, V>
    extends BaseQueryHelper<OneToOne<T, K, V>, T, K, V> {

  /**
   * of.
   *
   * @param keyFunction a {@link SFunction} object
   * @param <T> a T class
   * @param <K> a K class
   * @return a {@link OneToOne} object
   */
  public static <T, K extends Serializable & Comparable<? super K>> OneToOne<T, K, T> of(
      SFunction<T, K> keyFunction) {
    return new OneToOne<>(keyFunction);
  }

  /**
   * Constructor for OneToOne.
   *
   * @param keyFunction a {@link SFunction} object
   */
  protected OneToOne(SFunction<T, K> keyFunction) {
    super(keyFunction);
  }

  /**
   * value.
   *
   * @param valueFunction a {@link SFunction} object
   * @param <R> a R class
   * @return a {@link OneToOne} object
   */
  public <R> OneToOne<T, K, R> value(SFunction<T, R> valueFunction) {
    attachDouble(valueFunction);
    return (OneToOne<T, K, R>) this;
  }

  /**
   * query.
   *
   * @return a {@link Map} object
   */
  public Map<K, V> query() {
    return query(HashMap::new);
  }

  /**
   * query.
   *
   * @param mapFactory a {@link IntFunction} object
   * @param <R> a R class
   * @return a R object
   */
  public <R extends Map<K, V>> R query(IntFunction<R> mapFactory) {
    List<T> list = Database.list(wrapper);
    return Steam.of(list)
        .parallel(isParallel)
        .peek(peekConsumer)
        .toMap(
            keyFunction,
            valueOrIdentity(),
            SerBiOp.justAfter(),
            () -> mapFactory.apply(list.size()));
  }
}
