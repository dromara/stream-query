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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.beecp;

import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

/**
 * BeeCp参数配置
 *
 * @author TaoYu
 * @since 3.3.4
 */
@Getter
@Setter
public class BeeCpConfig {

  private String defaultCatalog;
  private String defaultSchema;
  private Boolean defaultReadOnly;
  private Boolean defaultAutoCommit;
  private Integer defaultTransactionIsolationCode;
  private String defaultTransactionIsolationName;

  private Boolean fairMode;
  private Integer initialSize;
  private Integer maxActive;
  private Integer borrowSemaphoreSize;
  private Long maxWait;
  private Long idleTimeout;
  private Long holdTimeout;
  private String connectionTestSql;
  private Integer connectionTestTimeout;
  private Long connectionTestIntegererval;
  private Long idleCheckTimeIntegererval;
  private Boolean forceCloseUsingOnClear;
  private Long delayTimeForNextClear;

  private String connectionFactoryClassName;
  private String xaConnectionFactoryClassName;
  private Properties connectProperties;
  private String poolImplementClassName;
  private Boolean enableJmx;
}