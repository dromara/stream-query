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
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.core.lambda.function.SerCons;
import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.optional.Sf;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.UnaryOperator;

/**
 * 公共方法抽象
 *
 * @param <S> value改变后的返回值
 * @param <T> 实体类型
 * @param <K> key值类型
 * @param <V> value或后续操作返回值类型
 * @author VampireAchao
 * @since 2022/9/16 16:30
 */
@SuppressWarnings("unchecked")
public abstract class BaseQueryHelper<
        S extends BaseQueryHelper<S, T, K, V>, T, K extends Serializable & Comparable<? super K>, V>
    extends BaseQuery<T, K, V> {

  /**
   * Constructor for BaseQueryHelper.
   *
   * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   */
  protected BaseQueryHelper(SFunction<T, K> keyFunction) {
    super(keyFunction);
  }

  /**
   * eq.
   *
   * @param data a K object
   * @return a S object
   */
  public S eq(K data) {
    wrapper =
        Sf.of(data)
            .mayLet(value -> wrapper.eq(keyFunction, value))
            .orGet(() -> Database.notActive(wrapper));
    return (S) this;
  }

  /**
   * in.
   *
   * @param dataList a {@link java.util.Collection} object
   * @return a S object
   */
  public S in(Collection<K> dataList) {
    wrapper =
        Sf.mayColl(dataList)
            .mayLet(HashSet::new)
            .mayLet(values -> wrapper.in(keyFunction, values))
            .orGet(() -> Database.notActive(wrapper));
    return (S) this;
  }

  /**
   * like.
   *
   * @param data a {@link java.lang.String} object
   * @return a S object
   */
  public S like(String data) {
    wrapper =
        Sf.ofStr(data)
            .mayLet(value -> wrapper.like(keyFunction, value))
            .orGet(() -> Database.notActive(wrapper));
    return (S) this;
  }

  /**
   * condition.
   *
   * @param queryOperator a {@link java.util.function.UnaryOperator} object
   * @return a S object
   */
  public S condition(UnaryOperator<LambdaQueryWrapper<T>> queryOperator) {
    wrapper = Sf.of(queryOperator.apply(wrapper)).orGet(() -> Database.notActive(wrapper));
    return (S) this;
  }

  /**
   * parallel.
   *
   * @param isParallel a boolean
   * @return a S object
   */
  public S parallel(boolean isParallel) {
    this.isParallel = isParallel;
    return (S) this;
  }

  /**
   * parallel.
   *
   * @return a S object
   */
  public S parallel() {
    return parallel(true);
  }

  /**
   * sequential.
   *
   * @return a S object
   */
  public S sequential() {
    return parallel(false);
  }

  /**
   * peek.
   *
   * @param peek a {@link SerCons} object
   * @return a S object
   */
  public S peek(SerCons<T> peek) {
    this.peekConsumer = this.peekConsumer.andThen(peek);
    return (S) this;
  }

  /**
   * attachSingle.
   *
   * @param valueFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <R> a R class
   */
  protected <R> void attachSingle(SFunction<T, R> valueFunction) {
    this.valueFunction = (SFunction<T, V>) valueFunction;
    WrapperHelper.select(
        wrapper, (w, col) -> w.select(Lists.of(col[1])), keyFunction, valueFunction);
  }

  /**
   * attachDouble.
   *
   * @param valueFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   * @param <R> a R class
   */
  protected <R> void attachDouble(SFunction<T, R> valueFunction) {
    this.valueFunction = (SFunction<T, V>) valueFunction;
    WrapperHelper.select(wrapper, keyFunction, valueFunction);
  }

  /**
   * valueOrIdentity.
   *
   * @return a {@link SerFunc} object
   */
  protected SerFunc<T, V> valueOrIdentity() {
    return t -> Sf.of(valueFunction).orGet(() -> SerFunc.<T, V>cast()::apply).apply(t);
  }
}
