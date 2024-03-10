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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicDataSource.event;

import javax.sql.DataSource;

import org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration.DataSourceProperty;

/**
 * 多数据源连接池创建事件
 *
 * @author TaoYu
 * @since 3.5.0
 */
public interface DataSourceInitEvent {

  /**
   * 连接池创建前执行（可用于参数解密）
   *
   * @param dataSourceProperty 数据源基础信息
   */
  void beforeCreate(DataSourceProperty dataSourceProperty);

  /**
   * 连接池创建后执行
   *
   * @param dataSource 连接池
   */
  void afterCreate(DataSource dataSource);
}
