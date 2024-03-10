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

import javax.sql.DataSource;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.plugin.mybatisplus.Database;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.DynamicRoutingDataSource;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.DataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.creator.DefaultDataSourceCreator;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.event.DataSourceInitEvent;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.event.EncDataSourceInitEvent;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.tx.DsTxEventListenerFactory;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.handler.JsonPostInitTableInfoHandler;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.mapper.DynamicMapperHandler;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.methods.SaveOneSql;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.methods.UpdateOneSql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * stream-query自动装配
 *
 * @author VampireAchao Cizai_
 */
@EnableConfigurationProperties({StreamPluginProperties.class, DataSourceProperty.class})
public class StreamPluginAutoConfiguration {
  private final DataSourceProperty properties;

  public StreamPluginAutoConfiguration(
      StreamPluginProperties properties, DataSourceProperty dataProperties) {
    StreamPluginConfig.setSafeModeEnabled(properties.isSafeMode());
    this.properties = dataProperties;
  }

  private static final String CURRENT_NAMESPACE =
      LambdaHelper.getPropertyName(TableInfo::getCurrentNamespace);

  /**
   * defaultSqlInjector.
   *
   * @return a {@link DefaultSqlInjector} object
   */
  @Bean
  @Order
  @ConditionalOnMissingBean(DefaultSqlInjector.class)
  public DefaultSqlInjector defaultSqlInjector() {
    return new DefaultSqlInjector() {
      @Override
      public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new SaveOneSql());
        methodList.add(new UpdateOneSql());
        return methodList;
      }

      @Override
      public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        super.inspectInject(builderAssistant, mapperClass);
        Class<?> modelClass = ReflectionKit.getSuperClassGenericType(mapperClass, Mapper.class, 0);
        if (modelClass == null) {
          return;
        }
        TableInfo tableInfo = TableInfoHelper.initTableInfo(builderAssistant, modelClass);
        if (Database.isDynamicMapper(tableInfo.getCurrentNamespace())
            && !mapperClass.getName().equals(tableInfo.getCurrentNamespace())) {
          // 降低动态mapper优先级
          ReflectHelper.setFieldValue(tableInfo, CURRENT_NAMESPACE, mapperClass.getName());
        }
        if (!Database.isDynamicMapper(mapperClass.getName())) {
          Database.getEntityMapperClassCache().put(modelClass, mapperClass);
        }
      }
    };
  }

  @Bean
  @ConditionalOnMissingBean(DynamicMapperHandler.class)
  public DynamicMapperHandler dynamicMapperHandler(
      SqlSessionFactory sqlSessionFactory, StreamScannerConfigurer streamScannerConfigurer) {
    return new DynamicMapperHandler(sqlSessionFactory, streamScannerConfigurer.getEntityClasses());
  }

  @Bean
  @ConditionalOnMissingBean(JsonPostInitTableInfoHandler.class)
  public JsonPostInitTableInfoHandler jsonPostInitTableInfoHandler() {
    return new JsonPostInitTableInfoHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public DataSourceInitEvent dataSourceInitEvent() {
    return new EncDataSourceInitEvent();
  }

  @Bean
  @ConditionalOnMissingBean
  public DefaultDataSourceCreator dataSourceCreator(
      List<DataSourceCreator> dataSourceCreators, DataSourceInitEvent dataSourceInitEvent) {
    DefaultDataSourceCreator creator = new DefaultDataSourceCreator();
    creator.setCreators(dataSourceCreators);
    creator.setDataSourceInitEvent(dataSourceInitEvent);
    creator.setPublicKey(properties.getPublicKey());
    creator.setLazy(properties.getLazy());
    creator.setP6spy(properties.getP6spy());
    creator.setSeata(properties.getSeata());
    return creator;
  }

  @Bean
  @ConditionalOnMissingBean(DataSource.class)
  public DataSource dataSource(
      DefaultDataSourceCreator defaultDataSourceCreator, DataSourceProperty properties) {
    DynamicRoutingDataSource dataSource =
        new DynamicRoutingDataSource(defaultDataSourceCreator, properties);
    dataSource.setPrimary("master");
    dataSource.setP6spy(properties.getP6spy());
    dataSource.setSeata(properties.getSeata());
    Database.setRoutingDataSource(dataSource);
    return dataSource;
  }

  @Configuration
  static class DsTxEventListenerFactoryConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DsTxEventListenerFactory dsTxEventListenerFactory() {
      return new DsTxEventListenerFactory();
    }
  }
}
