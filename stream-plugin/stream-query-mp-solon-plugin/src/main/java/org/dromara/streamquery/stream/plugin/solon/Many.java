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
import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.List;

/**
 * 多条
 *
 * @author VampireAchao Cizai_
 * @since 2022/6/18 21:21
 */
@SuppressWarnings("unchecked")
public class Many<T, K extends Serializable & Comparable<? super K>, V>
    extends BaseQueryHelper<Many<T, K, V>, T, K, V> {

  /**
   * Constructor for Many.
   *
   * @param keyFunction a {@link SFunction} object
   */
  public Many(SFunction<T, K> keyFunction) {
    super(keyFunction);
  }

  /**
   * of.
   *
   * @param keyFunction a {@link SFunction} object
   * @param <T> a T class
   * @param <K> a K class
   * @return a {@link Many} object
   */
  public static <T, K extends Serializable & Comparable<? super K>> Many<T, K, T> of(
      SFunction<T, K> keyFunction) {
    return new Many<>(keyFunction);
  }

  /**
   * value.
   *
   * @param valueFunction a {@link SFunction} object
   * @param <R> a R class
   * @return a {@link Many} object
   */
  public <R> Many<T, K, R> value(SFunction<T, R> valueFunction) {
    attachSingle(valueFunction);
    return (Many<T, K, R>) this;
  }

  /**
   * query.
   *
   * @param mapper a {@link SerFunc} object
   * @param <R> a R class
   * @return a R object
   */
  public <R> R query(SerFunc<Steam<V>, R> mapper) {
    return mapper.apply(
        Steam.of(Database.list(wrapper))
            .peek(peekConsumer)
            .parallel(isParallel)
            .nonNull()
            .map(valueOrIdentity()));
  }

  /**
   * query.
   *
   * @return a {@link List} object
   */
  public List<V> query() {
    return query(Steam::toList);
  }
}
