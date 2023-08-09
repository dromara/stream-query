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

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.lambda.function.SerBiCons;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author VampireAchao
 * @since 2023/4/13 17:54
 */
public class WrapperHelper {

  private WrapperHelper() {
    /* Do not new me! */
  }

  /**
   * in查询
   *
   * @param wrapper 条件构造器
   * @param dataList 数据
   * @param <T> 类型
   * @return 条件构造器
   */
  @SuppressWarnings("unchecked")
  public static <T> LambdaQueryWrapper<T> multiIn(LambdaQueryWrapper<T> wrapper, List<T> dataList) {
    if (Lists.isEmpty(dataList)) {
      return wrapper;
    }
    final Class<T> entityClass = Database.getEntityClass(dataList);
    final List<TableFieldInfo> fieldList = TableInfoHelper.getTableInfo(entityClass).getFieldList();
    multi(
        wrapper,
        fieldList,
        (w, tableField) -> {
          SFunction<T, ?> getterFunction =
              LambdaHelper.getGetter(entityClass, tableField.getProperty(), SFunction.class);
          final List<?> list = Steam.of(dataList).map(getterFunction).nonNull().toList();
          w.or().in(Lists.isNotEmpty(list), getterFunction, list);
        });
    return wrapper;
  }

  /**
   * or 查询 只会拼接第一个条件
   *
   * @param wrapper 条件构造器
   * @param dataList 数据
   * @param biConsumer 逻辑处理
   * @param <W> 条件构造器
   * @param <T> 实体类型
   * @param <R> 数据类型
   * @return 条件构造器
   * @deprecated 请使用 {@link #multi(AbstractWrapper, Collection, BiConsumer)},该API将在v2.0废弃
   */
  @Deprecated
  public static <W extends AbstractWrapper<T, ?, W>, T, R> W multiOr(
      W wrapper, Collection<R> dataList, BiConsumer<W, R> biConsumer) {
    return multi(wrapper, dataList, (w, data) -> biConsumer.accept(w.or(), data));
  }

  /**
   * 多个条件查询
   *
   * @param wrapper 条件构造器
   * @param dataList 数据
   * @param biConsumer 逻辑处理
   * @param <W> 条件构造器
   * @param <T> 实体类型
   * @param <R> 数据类型
   * @return 条件构造器
   */
  public static <W extends AbstractWrapper<T, ?, W>, T, R> W multi(
      W wrapper, Collection<R> dataList, BiConsumer<W, R> biConsumer) {
    if (Lists.isEmpty(dataList)) {
      return Database.notActive(wrapper);
    }
    return wrapper.nested(w -> dataList.forEach(data -> biConsumer.accept(w, data)));
  }

  /**
   * select.
   *
   * @param wrapper a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
   *     object
   * @param columns a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @return a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
   */
  @SafeVarargs
  public static <T> LambdaQueryWrapper<T> select(
      LambdaQueryWrapper<T> wrapper, SFunction<T, ?>... columns) {
    return select(wrapper, (w, cols) -> w.select(Lists.of(cols)), columns);
  }

  /**
   * select.
   *
   * @param wrapper a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
   *     object
   * @param whenAllMatchColumn a {@link SerBiCons} object
   * @param columns a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @return a {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper} object
   */
  @SafeVarargs
  public static <T> LambdaQueryWrapper<T> select(
      LambdaQueryWrapper<T> wrapper,
      SerBiCons<LambdaQueryWrapper<T>, SFunction<T, ?>[]> whenAllMatchColumn,
      SFunction<T, ?>... columns) {
    if (Steam.of(columns)
        .allMatch(
            func ->
                Objects.nonNull(func)
                    && PropertyNamer.isGetter(
                        LambdaHelper.resolve(func).getLambda().getImplMethodName()))) {
      whenAllMatchColumn.accept(wrapper, columns);
    }
    return wrapper;
  }

  /**
   * lambdaQuery.
   *
   * @param data a E object
   * @param condition a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @param <E> a E class
   * @return a {@link Opp} object
   */
  public static <T, E extends Serializable> Opp<LambdaQueryWrapper<T>> lambdaQuery(
      E data, SFunction<T, E> condition) {
    return Opp.of(data)
        .map(
            value ->
                Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition)))
                    .eq(condition, value))
        .filter(Database::isActive);
  }

  /**
   * lambdaQuery.
   *
   * @param dataList a {@link java.util.Collection} object
   * @param condition a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @param <E> a E class
   * @return a {@link Opp} object
   */
  public static <T, E extends Serializable> Opp<LambdaQueryWrapper<T>> lambdaQuery(
      Collection<E> dataList, SFunction<T, E> condition) {
    return Opp.ofColl(dataList)
        .map(
            value ->
                Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition)))
                    .in(condition, new HashSet<>(value)))
        .filter(Database::isActive);
  }
}
