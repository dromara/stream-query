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
import org.dromara.streamquery.stream.core.lambda.function.SerCons;

import java.util.Objects;

/**
 * Abstract BaseQuery class.
 *
 * @author VampireAchao
 * @since 2022/9/26 17:50
 */
public abstract class BaseQuery<T, K, V> {

  protected final SFunction<T, K> keyFunction;
  protected SFunction<T, V> valueFunction;
  protected LambdaQueryWrapper<T> wrapper;
  protected boolean isParallel = false;
  protected SerCons<T> peekConsumer = SerCons.nothing();

  /**
   * Constructor for BaseQuery.
   *
   * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
   */
  protected BaseQuery(SFunction<T, K> keyFunction) {
    this.keyFunction = Objects.requireNonNull(keyFunction);
    this.wrapper = Database.lambdaQuery(keyFunction);
  }
}
