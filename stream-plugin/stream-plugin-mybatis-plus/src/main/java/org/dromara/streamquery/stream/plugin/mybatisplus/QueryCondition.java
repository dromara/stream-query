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
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.dromara.streamquery.stream.core.bean.BeanHelper;
import org.dromara.streamquery.stream.core.lambda.LambdaExecutable;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.handler.AbstractJsonFieldHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import java.util.Optional;

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
    if (Objects.isNull(data)) {
      return this;
    }

//    Optional<String> optionalJson = convertIfNeed(column, data);
//    if (optionalJson.isPresent()) {
//      String json = optionalJson.get();
//      super.eq(column, json);
//    } else {
//      super.eq(column,data);
//    }
    Opp.of(convertIfNeed(column, data))
            .ifPresent( v -> super.eq(column, v))
            .orElseRun(()->super.eq(column, data));

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

    if (CollectionUtils.isEmpty(dataList)) {
      super.in(false, column, dataList);
    }
    Collection<String> jsonList = new ArrayList<>();
    dataList.forEach(v -> convertIfNeed(column, v).ifPresent(jsonList::add));
    Opp.of(jsonList).
            ifPresent(list -> super.in( column, list))
            .orElseRun(() ->super.in( column, dataList));
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
    Opp.of(data)
            .map(v ->     Opp.of(convertIfNeed(column, data))
                    .ifPresent( json -> super.eq(column, json))
                    .orElseRun(()->super.eq(column, data)))
            .orElseRun(() -> Database.notActive(this));
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
    Opp.ofColl(dataList).map(list -> {
      Collection<String> jsonList = new ArrayList<>();
      dataList.forEach(v -> convertIfNeed(column, v).ifPresent(jsonList::add));
      return Opp.of(jsonList)
              .ifPresent(vList -> super.in( column, vList))
              .orElseRun(() -> super.in( column, dataList));
    }).orElseRun(() -> Database.notActive(this));
    return this;
  }

  private <R extends Comparable<? super R>> Optional<String> convertIfNeed(SFunction<T, R> column, R data) {
    LambdaExecutable executable = LambdaHelper.resolve(column);
    String name = BeanHelper.getPropertyName(executable.getName());
    TableInfo tableInfo = TableInfoHelper.getTableInfo(executable.getClazz());
    Configuration configuration = tableInfo.getConfiguration();
    ResultMap resultMap =configuration.getResultMap(tableInfo.getResultMap());

    Optional<TableFieldInfo> fieldInfo = Steam.of(tableInfo.getFieldList()).findFirst(field -> field.getProperty().equals(name));
    return fieldInfo.flatMap(field -> {
      Optional<ResultMapping> resultMappingOpt = Steam.of(resultMap.getResultMappings())
              .findFirst(resultMapping -> field.getProperty().equals(resultMapping.getProperty()));

      return resultMappingOpt.flatMap(resultMapping -> {
        TypeHandler<?> typeHandler = resultMapping.getTypeHandler();
        if (typeHandler instanceof AbstractJsonFieldHandler) {
          AbstractJsonFieldHandler<R> handler = (AbstractJsonFieldHandler<R>) typeHandler;
          return Optional.of(handler.toJson(data, tableInfo, field));
        }
        return Optional.empty();
      });
    });
  }
}
