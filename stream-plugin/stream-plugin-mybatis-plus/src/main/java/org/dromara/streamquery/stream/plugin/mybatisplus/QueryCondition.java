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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.dromara.streamquery.stream.core.optional.Opp;

import java.util.Collection;
import java.util.Objects;

/**
 * QueryCondition class.
 *
 * @author VampireAchao Cizai_
 * @since 2022/8/15 16:39
 */
public class QueryCondition<T> extends LambdaQueryWrapper<T> {

  /**
   * query.
   *
   * @param entity a T object
   * @param <T> a T class
   * @return a {@link QueryCondition} object
   */
  public static <T> QueryCondition<T> query(T entity) {
    QueryCondition<T> condition = new QueryCondition<>();
    condition.setEntity(entity);
    return condition;
  }

  /**
   * query.
   *
   * @param entityClass a {@link java.lang.Class} object
   * @param <T> a T class
   * @return a {@link QueryCondition} object
   */
  public static <T> QueryCondition<T> query(Class<T> entityClass) {
    QueryCondition<T> condition = new QueryCondition<>();
    condition.setEntityClass(entityClass);
    return condition;
  }

  /**
   * eq.
   *
   * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param data a {@link java.lang.String} object
   * @return a {@link QueryCondition} object
   */
  public QueryCondition<T> eq(SFunction<T, String> column, String data) {
    super.eq(StringUtils.isNotEmpty(data), column, data);
    return this;
  }

  /**
   * eq.
   *
   * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param data a R object
   * @param <R> a R class
   * @return a {@link QueryCondition} object
   */
  public <R extends Comparable<? super R>> QueryCondition<T> eq(SFunction<T, R> column, R data) {
    super.eq(Objects.nonNull(data), column, data);
    return this;
  }

  /**
   * like.
   *
   * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param data a {@link java.lang.String} object
   * @return a {@link QueryCondition} object
   */
  public QueryCondition<T> like(SFunction<T, String> column, String data) {
    super.like(StringUtils.isNotEmpty(data), column, data);
    return this;
  }

  /**
   * in.
   *
   * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param dataList a {@link java.util.Collection} object
   * @param <R> a R class
   * @return a {@link QueryCondition} object
   */
  public <R extends Comparable<? super R>> QueryCondition<T> in(
      SFunction<T, R> column, Collection<R> dataList) {
    super.in(CollectionUtils.isNotEmpty(dataList), column, dataList);
    return this;
  }

  /**
   * activeEq.
   *
   * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param data a {@link java.lang.String} object
   * @return a {@link QueryCondition} object
   */
  public QueryCondition<T> activeEq(SFunction<T, String> column, String data) {
    Opp.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
    return this;
  }

  /**
   * activeEq.
   *
   * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param data a R object
   * @param <R> a R class
   * @return a {@link QueryCondition} object
   */
  public <R extends Comparable<? super R>> QueryCondition<T> activeEq(
      SFunction<T, R> column, R data) {
    Opp.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
    return this;
  }

  /**
   * activeLike.
   *
   * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param data a {@link java.lang.String} object
   * @return a {@link QueryCondition} object
   */
  public QueryCondition<T> activeLike(SFunction<T, String> column, String data) {
    Opp.of(data).map(v -> super.like(column, v)).orElseRun(() -> Database.notActive(this));
    return this;
  }

  /**
   * activeIn.
   *
   * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param dataList a {@link java.util.Collection} object
   * @param <R> a R class
   * @return a {@link QueryCondition} object
   */
  public <R extends Comparable<? super R>> QueryCondition<T> activeIn(
      SFunction<T, R> column, Collection<R> dataList) {
    Opp.ofColl(dataList).map(v -> super.in(column, v)).orElseRun(() -> Database.notActive(this));
    return this;
  }
}
