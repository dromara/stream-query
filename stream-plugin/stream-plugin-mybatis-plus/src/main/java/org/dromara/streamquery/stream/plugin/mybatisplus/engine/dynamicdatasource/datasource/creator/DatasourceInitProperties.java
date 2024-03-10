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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator;

import lombok.Data;

/**
 * 动态数据源初始化脚本配置
 *
 * @author TaoYu
 * @since 3.5.0
 */
@Data
public class DatasourceInitProperties {

  /** 自动运行的建表脚本 */
  private String schema;
  /** 自动运行的数据脚本 */
  private String data;

  /** 错误是否继续 默认 true */
  private boolean continueOnError = true;
  /** 分隔符 默认 ; */
  private String separator = ";";
}
