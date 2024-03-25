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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.destroyer;

import javax.sql.DataSource;

import lombok.SneakyThrows;

/**
 * Description DHCP2 data source pool active detector.
 *
 * @author alvinkwok
 * @since 2023/10/18
 */
public class Dhcp2DataSourceActiveDetector implements DataSourceActiveDetector {
  @Override
  @SneakyThrows(ReflectiveOperationException.class)
  public boolean containsActiveConnection(DataSource dataSource) {
    int activeCount = (int) dataSource.getClass().getMethod("getNumActive").invoke(dataSource);
    return activeCount != 0;
  }

  @Override
  public boolean support(DataSource dataSource) {
    return "org.apache.commons.dbcp2.BasicDataSource".equals(dataSource.getClass().getName());
  }
}