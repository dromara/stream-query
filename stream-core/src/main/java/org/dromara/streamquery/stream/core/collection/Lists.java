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
 * Lists class.
 *
 * @author VampireAchao 请我吃早饭
 * @since 2022/10/21 17:08
 */
public class Lists {

  /** */
  private Lists() {
    /* Do not new me! */
  }

  /**
   * of.
   *
   * @param values 集合元素
   * @param <T> a T class
   * @return {@link java.util.List}<{@code T}> 集合元素类型
   */
  @SafeVarargs
  public static <T> List<T> of(T... values) {
    if (Objects.isNull(values) || values.length == 0) {
      return new ArrayList<>();
    }
    return new ArrayList<>(Arrays.asList(values));
  }

  /**
   * ofColl.
   *
   * @param values 集合元素
   * @param <T> a T class
   * @return {@link java.util.List}<{@code T}> 集合元素类型
   */
  public static <T> List<T> ofColl(Collection<T> values) {
    if (Objects.isNull(values)) {
      return new ArrayList<>();
    }
    return new ArrayList<>(values);
  }

  /**
   * first.
   *
   * @param values a {@link java.util.List} object
   * @param <T> a T class
   * @return a T object
   */
  public static <T> T first(List<T> values) {
    if (isEmpty(values)) {
      return null;
    }
    return values.get(0);
  }

  /**
   * last.
   *
   * @param values a {@link java.util.List} object
   * @param <T> a T class
   * @return a T object
   */
  public static <T> T last(List<T> values) {
    if (isEmpty(values)) {
      return null;
    }
    return values.get(values.size() - 1);
  }

  /**
   * 升序排序
   *
   * @param list 需排序集合
   * @param <T> a T class
   * @return {@link java.util.List}<{@code T}>
   */
  public static <T> List<T> ascend(List<T> list) {
    list.sort(Collections.reverseOrder().reversed());
    return list;
  }

  /**
   * 降序排序
   *
   * @param list 需排序集合
   * @param <T> a T class
   * @return {@link java.util.List}<{@code T}>
   */
  public static <T> List<T> descend(List<T> list) {
    list.sort(Collections.reverseOrder());
    return list;
  }

  /**
   * 翻转集合元素
   *
   * @param list 需翻转集合元素
   * @param <T> a T class
   * @return {@link java.util.List}<{@code T}>
   */
  public static <T> List<T> reverse(List<T> list) {
    Collections.reverse(list);
    return list;
  }

  /**
   * binarySearch.
   *
   * @param list 要查找的集合
   * @param key 想要查找的数据
   * @param <T> a T class
   * @return int 坐标
   */
  public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
    return Collections.binarySearch(ascend(list), key);
  }

  /**
   * isEmpty.
   *
   * @param list a {@link java.util.Collection} object
   * @return a boolean
   */
  public static boolean isEmpty(Collection<?> list) {
    return Objects.isNull(list) || list.isEmpty();
  }

  /**
   * isNotEmpty.
   *
   * @param list a {@link java.util.Collection} object
   * @return a boolean
   */
  public static boolean isNotEmpty(Collection<?> list) {
    return Objects.nonNull(list) && !list.isEmpty();
  }

  /**
   * empty.
   *
   * @param <T> a T class
   * @return a {@link java.util.List} object
   */
  public static <T> List<T> empty() {
    return Collections.emptyList();
  }
}
