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

import javax.sql.DataSource;

import cn.beecp.BeeDataSource;
import cn.beecp.BeeDataSourceConfig;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration.DataSourceProperty;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.DataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.enums.DdConstants;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.toolkit.ConfigMergeCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.toolkit.DsStrUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * BeeCp数据源创建器
 *
 * @author TaoYu
 * @since 2020/5/14
 */
@NoArgsConstructor
@AllArgsConstructor
public class BeeCpDataSourceCreator implements DataSourceCreator {

  private static final ConfigMergeCreator<BeeCpConfig, BeeDataSourceConfig> MERGE_CREATOR =
      new ConfigMergeCreator<>("BeeCp", BeeCpConfig.class, BeeDataSourceConfig.class);

  private static Method copyToMethod = null;

  static {
    try {
      copyToMethod =
          BeeDataSourceConfig.class.getDeclaredMethod("copyTo", BeeDataSourceConfig.class);
      copyToMethod.setAccessible(true);
    } catch (NoSuchMethodException ignored) {
    }
  }

  private BeeCpConfig gConfig;

  @Override
  public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
    BeeDataSourceConfig config = MERGE_CREATOR.create(gConfig, dataSourceProperty.getBeecp());
    config.setUsername(dataSourceProperty.getUsername());
    config.setPassword(dataSourceProperty.getPassword());
    config.setJdbcUrl(dataSourceProperty.getUrl());
    config.setPoolName(dataSourceProperty.getPoolName());
    String driverClassName = dataSourceProperty.getDriverClassName();
    if (DsStrUtils.hasText(driverClassName)) {
      config.setDriverClassName(driverClassName);
    }
    if (Boolean.FALSE.equals(dataSourceProperty.getLazy())) {
      return new BeeDataSource(config);
    }
    BeeDataSource beeDataSource = new BeeDataSource();
    try {
      copyToMethod.invoke(config, beeDataSource);
    } catch (InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return beeDataSource;
  }

  @Override
  public boolean support(DataSourceProperty dataSourceProperty) {
    Class<? extends DataSource> type = dataSourceProperty.getType();
    return type == null || DdConstants.BEECP_DATASOURCE.equals(type.getName());
  }
}