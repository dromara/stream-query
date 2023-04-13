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

import java.util.*;

/**
 * Sets class.
 *
 * @author VampireAchao
 * @since 2022/10/21 17:10
 */
public class Sets {

  private Sets() {
    /* Do not new me! */
  }

  /**
   * of.
   *
   * @param values a T object
   * @param <T> a T class
   * @return a {@link java.util.Set} object
   */
  @SafeVarargs
  public static <T> Set<T> of(T... values) {
    if (Objects.isNull(values) || values.length == 0) {
      return new HashSet<>();
    }
    return new HashSet<>(Arrays.asList(values));
  }

  /**
   * ofColl.
   *
   * @param values a {@link java.util.Collection} object
   * @param <T> a T class
   * @return a {@link java.util.Set} object
   */
  public static <T> Set<T> ofColl(Collection<T> values) {
    if (Objects.isNull(values)) {
      return new HashSet<>();
    }
    return new HashSet<>(values);
  }

  /**
   * ofSize.
   *
   * @param initialCapacity initialCapacity
   * @param <T> a T class
   * @return Set
   */
  public static <T> Set<T> ofSize(int initialCapacity) {
    return new HashSet<>(initialCapacity);
  }

  /**
   * 获取一个空的Set
   *
   * @param <T> 元素类型
   * @return 一个空的set
   */
  public static <T> Set<T> empty() {
    return Collections.emptySet();
  }
}
