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

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.WrapperKeyword;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.dromara.streamquery.stream.core.bean.BeanHelper;
import org.dromara.streamquery.stream.core.lambda.LambdaExecutable;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration.StreamPluginConfig;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.utils.SqlInjectionUtilSq;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * QueryCondition class.
 *
 * @author VampireAchao Cizai_
 * @since 2022/8/15 16:39
 */
public class QueryCondition<T> extends LambdaQueryWrapper<T> {

  String mapping;

  QueryCondition(
      T entity,
      Class<T> entityClass,
      AtomicInteger paramNameSeq,
      Map<String, Object> paramNameValuePairs,
      MergeSegments mergeSegments,
      SharedString paramAlias,
      SharedString lastSql,
      SharedString sqlComment,
      SharedString sqlFirst) {
    super.setEntity(entity);
    super.setEntityClass(entityClass);
    this.paramNameSeq = paramNameSeq;
    this.paramNameValuePairs = paramNameValuePairs;
    this.expression = mergeSegments;
    this.paramAlias = paramAlias;
    this.lastSql = lastSql;
    this.sqlComment = sqlComment;
    this.sqlFirst = sqlFirst;
  }

  QueryCondition() {}
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
   * @param entityClass a {@link Class} object
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
   * @param column a {@link SFunction} object
   * @param data a {@link String} object
   * @return a {@link QueryCondition} object
   * @deprecated because this method is superfluous
   */
  @Deprecated
  public QueryCondition<T> eq(SFunction<T, String> column, String data) {
    super.eq(StringUtils.isNotEmpty(data), column, data);
    return this;
  }

  /**
   * eq.
   *
   * @param column a {@link SFunction} object
   * @param data a R object
   * @param <R> a R class
   * @return a {@link QueryCondition} object
   */
  public <R> QueryCondition<T> eq(SFunction<T, R> column, R data) {
    super.eq(column, data);
    return this;
  }

  /**
   * like.
   *
   * @param column a {@link SFunction} object
   * @param data a {@link String} object
   * @return a {@link QueryCondition} object
   * @deprecated because this method is superfluous
   */
  @Deprecated
  public QueryCondition<T> like(SFunction<T, String> column, String data) {
    super.like(StringUtils.isNotEmpty(data), column, data);
    return this;
  }

  /**
   * in.
   *
   * @param column a {@link SFunction} object
   * @param dataList a {@link Collection} object
   * @param <R> a R class
   * @return a {@link QueryCondition} object
   */
  public <R> QueryCondition<T> in(SFunction<T, R> column, Collection<R> dataList) {
    this.mapping = getMapping(column);
    super.in(column, dataList);
    return this;
  }

  /**
   * activeEq.
   *
   * @param column a {@link SFunction} object
   * @param data a {@link String} object
   * @return a {@link QueryCondition} object
   * @deprecated because this method is superfluous
   */
  @Deprecated
  public QueryCondition<T> activeEq(SFunction<T, String> column, String data) {
    Opp.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
    return this;
  }

  /**
   * activeEq.
   *
   * @param column a {@link SFunction} object
   * @param data a R object
   * @param <R> a R class
   * @return a {@link QueryCondition} object
   * @deprecated because this method is optional
   */
  @Deprecated
  public <R> QueryCondition<T> activeEq(SFunction<T, R> column, R data) {
    Opp.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
    return this;
  }

  /**
   * activeLike.
   *
   * @param column a {@link SFunction} object
   * @param data a {@link String} object
   * @return a {@link QueryCondition} object
   * @deprecated because this method is superfluous
   */
  @Deprecated
  public QueryCondition<T> activeLike(SFunction<T, String> column, String data) {
    Opp.of(data).map(v -> super.like(column, v)).orElseRun(() -> Database.notActive(this));
    return this;
  }

  /**
   * activeIn.
   *
   * @param column a {@link SFunction} object
   * @param dataList a {@link Collection} object
   * @param <R> a R class
   * @return a {@link QueryCondition} object
   * @deprecated because this method is optional
   */
  @Deprecated
  public <R> QueryCondition<T> activeIn(SFunction<T, R> column, Collection<R> dataList) {
    this.mapping = getMapping(column);
    Opp.ofColl(dataList).map(v -> super.in(column, v)).orElseRun(() -> Database.notActive(this));
    return this;
  }

  @Override
  public QueryCondition<T> or(Consumer<LambdaQueryWrapper<T>> consumer) {
    super.or(consumer);
    return this;
  }

  @Override
  public QueryCondition<T> and(Consumer<LambdaQueryWrapper<T>> consumer) {
    super.and(consumer);
    return this;
  }

  @Override
  protected LambdaQueryWrapper<T> addCondition(
      boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, Object val) {

    this.mapping = getMapping(column);
    return this.maybeDo(
        condition,
        () ->
            this.appendSqlSegments(
                this.columnToSqlSegment(column),
                sqlKeyword,
                () -> this.formatParam(this.mapping, val)));
  }

  private String getMapping(SFunction<T, ?> column) {
    LambdaExecutable executable = LambdaHelper.resolve(column);
    String name = BeanHelper.getPropertyName(executable.getName());
    TableInfo tableInfo = TableInfoHelper.getTableInfo(executable.getClazz());

    return Opp.of(
            Steam.of(tableInfo.getFieldList())
                .findFirst(field -> field.getProperty().equals(name))
                .flatMap(field -> Optional.ofNullable(field.getTypeHandler())))
        .map(v -> "typeHandler = " + v.getName())
        .orElse(null);
  }

  @Override
  protected ISqlSegment inExpression(Collection<?> value) {
    return CollectionUtils.isEmpty(value)
        ? () -> "()"
        : () ->
            value.stream()
                .map(i -> this.formatParam(this.mapping, i))
                .collect(Collectors.joining(",", "(", ")"));
  }

  @Override
  protected LambdaQueryWrapper<T> addNestedCondition(
      boolean condition, Consumer<LambdaQueryWrapper<T>> consumer) {
    return this.maybeDo(
        condition,
        () -> {
          QueryCondition<T> instance = this.instance();
          consumer.accept(instance);
          this.appendSqlSegments(WrapperKeyword.APPLY, instance);
        });
  }

  @Override
  protected QueryCondition<T> instance() {
    return new QueryCondition<>(
        this.getEntity(),
        this.getEntityClass(),
        this.paramNameSeq,
        this.paramNameValuePairs,
        new MergeSegments(),
        this.paramAlias,
        SharedString.emptyString(),
        SharedString.emptyString(),
        SharedString.emptyString());
  }

  @Override
  public LambdaQueryWrapper<T> apply(boolean condition, String applySql, Object... values) {
    if (StreamPluginConfig.isSafeModeEnabled() && SqlInjectionUtilSq.check(applySql)) {
      throw new IllegalArgumentException("SQL Injection attempt detected in 'apply'");
    }
    return super.apply(condition, applySql, values);
  }

  @Override
  public LambdaQueryWrapper<T> having(boolean condition, String sqlHaving, Object... params) {
    if (StreamPluginConfig.isSafeModeEnabled() && SqlInjectionUtilSq.check(sqlHaving)) {
      throw new IllegalArgumentException("SQL Injection attempt detected in 'having'");
    }
    return super.having(condition, sqlHaving, params);
  }
}
