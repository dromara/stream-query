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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.methods;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.constant.PluginConst;

import java.util.function.Function;

import static com.baomidou.mybatisplus.core.toolkit.Constants.ENTITY;
import static com.baomidou.mybatisplus.core.toolkit.Constants.ENTITY_DOT;

/**
 * MpInjectHelper class.
 *
 * @author VampireAchao
 * @since 2022/10/21 17:42
 */
public class MpInjectHelper {

  private MpInjectHelper() {
    /* Do not new me! */
  }

  /**
   * updateCondition.
   *
   * @param fieldInfo a {@link com.baomidou.mybatisplus.core.metadata.TableFieldInfo} object
   * @param strategy a {@link java.util.function.Function} object
   * @return a {@link java.lang.String} object
   */
  public static String updateCondition(
      TableFieldInfo fieldInfo, Function<TableFieldInfo, FieldStrategy> strategy) {
    final FieldStrategy fieldStrategy = strategy.apply(fieldInfo);
    if (fieldStrategy == FieldStrategy.NEVER) {
      return null;
    }
    if (fieldInfo.isPrimitive() || fieldStrategy == FieldStrategy.IGNORED) {
      return Boolean.TRUE.toString();
    }
    if (fieldStrategy == FieldStrategy.NOT_EMPTY && fieldInfo.isCharSequence()) {
      return String.format(
          PluginConst.NON_BLANK_CONDITION,
          ENTITY,
          ENTITY_DOT + fieldInfo.getProperty(),
          ENTITY_DOT + fieldInfo.getProperty());
    }
    return String.format(
        PluginConst.NON_NULL_CONDITION, ENTITY, ENTITY_DOT + fieldInfo.getProperty());
  }
}
