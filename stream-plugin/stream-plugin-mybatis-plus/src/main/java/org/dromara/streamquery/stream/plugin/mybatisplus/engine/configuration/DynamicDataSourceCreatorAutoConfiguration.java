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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration;

import cn.beecp.BeeDataSource;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.transaction.TransactionFactory;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.atomikos.AtomikosDataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.basic.BasicDataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.beecp.BeeCpDataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.dbcp.Dbcp2DataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.druid.DruidConfig;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.druid.DruidDataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.hikaricp.HikariDataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.jndi.JndiDataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.toolkit.DsStrUtils;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.tx.AtomikosTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

/** @author TaoYu */
public class DynamicDataSourceCreatorAutoConfiguration {

  public static final int JNDI_ORDER = 1000;
  public static final int DRUID_ORDER = 2000;
  public static final int HIKARI_ORDER = 3000;
  public static final int BEECP_ORDER = 4000;
  public static final int DBCP2_ORDER = 5000;
  public static final int ATOMIKOS_ORDER = 6000;
  public static final int DEFAULT_ORDER = 7000;

  @Bean
  @Order(DEFAULT_ORDER)
  public BasicDataSourceCreator basicDataSourceCreator() {
    return new BasicDataSourceCreator();
  }

  @Bean
  @Order(JNDI_ORDER)
  public JndiDataSourceCreator jndiDataSourceCreator() {
    return new JndiDataSourceCreator();
  }

  /** 存在Druid数据源时, 加入创建器 */
  @ConditionalOnClass(DruidDataSource.class)
  @Configuration
  @Slf4j
  @EnableConfigurationProperties(DataSourceProperty.class)
  static class DruidDataSourceCreatorConfiguration {

    @Autowired(required = false)
    private ApplicationContext applicationContext;

    @Bean
    @Order(DRUID_ORDER)
    public DruidDataSourceCreator druidDataSourceCreator(DataSourceProperty properties) {
      DruidConfig druid = properties.getDruid();
      return new DruidDataSourceCreator(
          druid,
          proxyFilters -> {
            List<Filter> filters = new ArrayList<>();
            if (applicationContext != null && DsStrUtils.hasText(proxyFilters)) {
              for (String filterId : proxyFilters.split(",")) {
                try {
                  filters.add(applicationContext.getBean(filterId, Filter.class));
                } catch (Exception e) {
                  log.warn(
                      "dynamic-datasource cannot load druid filter with name [{}], will be ignored",
                      filterId);
                }
              }
            }
            return filters;
          });
    }
  }

  /** 存在Hikari数据源时, 加入创建器 */
  @ConditionalOnClass(HikariDataSource.class)
  @Configuration
  @EnableConfigurationProperties(DataSourceProperty.class)
  static class HikariDataSourceCreatorConfiguration {
    @Bean
    @Order(HIKARI_ORDER)
    public HikariDataSourceCreator hikariDataSourceCreator(DataSourceProperty properties) {
      return new HikariDataSourceCreator(properties.getHikari());
    }
  }

  /** 存在BeeCp数据源时, 加入创建器 */
  @ConditionalOnClass(BeeDataSource.class)
  @Configuration
  @EnableConfigurationProperties(DataSourceProperty.class)
  static class BeeCpDataSourceCreatorConfiguration {

    @Bean
    @Order(BEECP_ORDER)
    public BeeCpDataSourceCreator beeCpDataSourceCreator(DataSourceProperty properties) {
      return new BeeCpDataSourceCreator(properties.getBeecp());
    }
  }

  /** 存在Dbcp2数据源时, 加入创建器 */
  @ConditionalOnClass(BasicDataSource.class)
  @Configuration
  @EnableConfigurationProperties(DataSourceProperty.class)
  static class Dbcp2DataSourceCreatorConfiguration {

    @Bean
    @Order(DBCP2_ORDER)
    public Dbcp2DataSourceCreator dbcp2DataSourceCreator(DataSourceProperty properties) {
      return new Dbcp2DataSourceCreator(properties.getDbcp2());
    }
  }

  /** 存在Atomikos数据源时, 加入创建器 */
  @ConditionalOnClass({AtomikosDataSourceBean.class, TransactionFactory.class})
  @Configuration
  static class AtomikosDataSourceCreatorConfiguration {

    @Bean
    @Order(ATOMIKOS_ORDER)
    public AtomikosDataSourceCreator atomikosDataSourceCreator(DataSourceProperty properties) {
      return new AtomikosDataSourceCreator(properties.getAtomikos());
    }

    @Bean
    public TransactionFactory atomikosTransactionFactory() {
      return new AtomikosTransactionFactory();
    }
  }
}