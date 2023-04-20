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
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.dromara.streamquery.stream.core.collection.Maps;
import org.dromara.streamquery.stream.core.lambda.function.SerCons;
import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.lambda.function.SerUnOp;
import org.dromara.streamquery.stream.core.optional.Sf;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.core.stream.collector.Collective;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * 一对多对一
 *
 * @param <T> 主表类型(关联表)
 * @param <K> 主表查询key
 * @param <V> 主表查询value/附表查询key
 * @param <U> 附表类型(关联主数据)
 * @param <A> 附表value
 * @author VampireAchao Cizai_
 * @since 2022/5/24 14:15
 */
@SuppressWarnings("unchecked")
public class OneToManyToOne<
    T,
    K extends Serializable & Comparable<? super K>,
    V extends Serializable & Comparable<? super V>,
    U,
    A> {

  private final SFunction<T, K> middleKey;
  private SFunction<T, V> middleValue;
  private SFunction<U, V> attachKey;
  private SFunction<U, A> attachValue;
  private LambdaQueryWrapper<T> middleWrapper;
  private UnaryOperator<LambdaQueryWrapper<U>> attachQueryOperator = SerUnOp.identity();

  private boolean isParallel = false;
  private SerCons<T> middlePeek = SerCons.nothing();
  private SerCons<U> attachPeek = SerCons.nothing();

  /**
   * Constructor for OneToManyToOne.
   *
   * @param middleKey a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   */
  public OneToManyToOne(SFunction<T, K> middleKey) {
    this.middleKey = middleKey;
    this.middleWrapper = Database.lambdaQuery(middleKey);
  }

  /**
   * of.
   *
   * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <T> a T class
   * @param <K> a K class
   * @param <V> a V class
   * @param <U> a U class
   * @return a {@link OneToManyToOne} object
   */
  public static <
          T,
          K extends Serializable & Comparable<? super K>,
          V extends Serializable & Comparable<V>,
          U>
      OneToManyToOne<T, K, V, U, T> of(SFunction<T, K> keyFunction) {
    return new OneToManyToOne<>(keyFunction);
  }

  /**
   * in.
   *
   * @param dataList a {@link java.util.Collection} object
   * @return a {@link OneToManyToOne} object
   */
  public OneToManyToOne<T, K, V, U, A> in(Collection<K> dataList) {
    middleWrapper =
        Sf.mayColl(dataList)
            .mayLet(HashSet::new)
            .mayLet(values -> middleWrapper.in(middleKey, values))
            .orGet(() -> Database.notActive(middleWrapper));
    return this;
  }

  /**
   * eq.
   *
   * @param data a K object
   * @return a {@link OneToManyToOne} object
   */
  public OneToManyToOne<T, K, V, U, A> eq(K data) {
    middleWrapper =
        Sf.of(data)
            .mayLet(value -> middleWrapper.eq(middleKey, value))
            .orGet(() -> Database.notActive(middleWrapper));
    return this;
  }

  /**
   * value.
   *
   * @param middleValue a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <VV> a VV class
   * @return a {@link OneToManyToOne} object
   */
  public <VV extends Serializable & Comparable<? super VV>> OneToManyToOne<T, K, VV, U, VV> value(
      SFunction<T, VV> middleValue) {
    this.middleValue = (SFunction<T, V>) middleValue;
    if (Objects.nonNull(middleWrapper)) {
      Database.select(middleWrapper, middleKey, middleValue);
    }
    return (OneToManyToOne<T, K, VV, U, VV>) this;
  }

  /**
   * attachKey.
   *
   * @param attachKey a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <UU> a UU class
   * @return a {@link OneToManyToOne} object
   */
  public <UU> OneToManyToOne<T, K, V, UU, UU> attachKey(SFunction<UU, V> attachKey) {
    this.attachKey = (SFunction<U, V>) attachKey;
    return (OneToManyToOne<T, K, V, UU, UU>) this;
  }

  /**
   * attachValue.
   *
   * @param attachValue a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <AA> a AA class
   * @return a {@link OneToManyToOne} object
   */
  public <AA> OneToManyToOne<T, K, V, T, AA> attachValue(SFunction<U, AA> attachValue) {
    this.attachValue = (SFunction<U, A>) attachValue;
    return (OneToManyToOne<T, K, V, T, AA>) this;
  }

  /**
   * condition.
   *
   * @param queryOperator a {@link java.util.function.UnaryOperator} object
   * @return a {@link OneToManyToOne} object
   */
  public OneToManyToOne<T, K, V, U, A> condition(
      UnaryOperator<LambdaQueryWrapper<T>> queryOperator) {
    middleWrapper =
        Sf.of(queryOperator.apply(middleWrapper)).orGet(() -> Database.notActive(middleWrapper));
    return this;
  }

  /**
   * attachCondition.
   *
   * @param attachQueryOperator a {@link java.util.function.UnaryOperator} object
   * @return a {@link OneToManyToOne} object
   */
  public OneToManyToOne<T, K, V, U, A> attachCondition(
      UnaryOperator<LambdaQueryWrapper<U>> attachQueryOperator) {
    this.attachQueryOperator = attachQueryOperator;
    return this;
  }

  /**
   * parallel.
   *
   * @param isParallel a boolean
   * @return a {@link OneToManyToOne} object
   */
  public OneToManyToOne<T, K, V, U, A> parallel(boolean isParallel) {
    this.isParallel = isParallel;
    return this;
  }

  /**
   * parallel.
   *
   * @return a {@link OneToManyToOne} object
   */
  public OneToManyToOne<T, K, V, U, A> parallel() {
    return parallel(true);
  }

  /**
   * sequential.
   *
   * @return a {@link OneToManyToOne} object
   */
  public OneToManyToOne<T, K, V, U, A> sequential() {
    return parallel(false);
  }

  /**
   * peek.
   *
   * @param middlePeek a {@link SerCons} object
   * @return a {@link OneToManyToOne} object
   */
  public OneToManyToOne<T, K, V, U, A> peek(SerCons<T> middlePeek) {
    this.middlePeek = this.middlePeek.andThen(middlePeek);
    return this;
  }

  /**
   * attachPeek.
   *
   * @param attachPeek a {@link SerCons} object
   * @return a {@link OneToManyToOne} object
   */
  public OneToManyToOne<T, K, V, U, A> attachPeek(SerCons<U> attachPeek) {
    this.attachPeek = this.attachPeek.andThen(attachPeek);
    return this;
  }

  /**
   * query.
   *
   * @param mapper a {@link java.util.function.BiFunction} object
   * @param <R> a R class
   * @return a R object
   */
  public <R> R query(BiFunction<Map<K, Collection<V>>, Map<V, A>, R> mapper) {
    Map<K, Collection<V>> middleKeyValuesMap =
        Steam.of(Database.list(middleWrapper))
            .parallel(isParallel)
            .peek(middlePeek)
            .group(
                middleKey,
                Collective.mapping(
                    Sf.of(middleValue).orGet(() -> SerFunc.<T, V>cast()::apply),
                    Collective.toCollection(TreeSet::new)));
    if (Objects.isNull(attachKey)) {
      return mapper.apply(middleKeyValuesMap, new HashMap<>(middleKeyValuesMap.size()));
    }
    Collection<V> relationDataList =
        Steam.of(middleKeyValuesMap.values()).flat(Function.identity()).toList();
    Map<V, A> attachKeyValue =
        OneToOne.of(attachKey)
            .in(relationDataList)
            .value(attachValue)
            .parallel(isParallel)
            .peek(attachPeek)
            .condition(attachQueryOperator)
            .query();
    return mapper.apply(middleKeyValuesMap, attachKeyValue);
  }

  /**
   * query.
   *
   * @return a {@link java.util.Map} object
   */
  public Map<K, Collection<A>> query() {
    return query(
        (middleKeyValuesMap, attachKeyValue) ->
            Maps.oneToManyToOne(middleKeyValuesMap, attachKeyValue, Steam::nonNull)
                .parallel(isParallel)
                .collect(Collective.entryToMap()));
  }
}
