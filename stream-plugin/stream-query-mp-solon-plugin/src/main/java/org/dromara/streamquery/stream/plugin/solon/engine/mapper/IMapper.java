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
package org.dromara.streamquery.stream.plugin.solon.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.plugin.solon.engine.constant.PluginConst;

import java.util.Collection;

/**
 * 拓展BaseMapper
 *
 * @param <T> 实体
 * @author VampireAchao Cizai_
 */
public interface IMapper<T> extends BaseMapper<T> {

  /**
   * 插入多条数据（mysql语法批量）
   *
   * @param list 数据
   * @return 条数
   */
  long saveOneSql(@Param(PluginConst.COLLECTION_PARAM_NAME) Collection<T> list);

  /**
   * 更新多条数据（mysql语法批量）
   *
   * @param list 数据
   * @return 条数
   */
  long updateOneSql(@Param(PluginConst.COLLECTION_PARAM_NAME) Collection<T> list);

  /**
   * 批量插入
   *
   * @param list 集合
   * @param batchSize 分割量
   * @return 是否成功
   */
  default long updateFewSql(Collection<T> list, int batchSize) {
    return Steam.of(list).splitList(batchSize).mapToLong(this::updateOneSql).sum();
  }

  /**
   * 批量插入
   *
   * @param list 集合
   * @param batchSize 分割量
   * @return 是否成功
   */
  default long saveFewSql(Collection<T> list, int batchSize) {
    return Steam.of(list).splitList(batchSize).mapToLong(this::saveOneSql).sum();
  }
}
