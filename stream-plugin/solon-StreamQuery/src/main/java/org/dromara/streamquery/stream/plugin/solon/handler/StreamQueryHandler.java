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
package org.dromara.streamquery.stream.plugin.solon.handler;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.plugin.solon.Database;
import org.dromara.streamquery.stream.plugin.solon.engine.methods.SaveOneSql;
import org.dromara.streamquery.stream.plugin.solon.engine.methods.UpdateOneSql;
import org.dromara.streamquery.stream.plugin.solon.scanner.StreamScanConfig;

import java.util.Collection;
import java.util.List;

/**
 * DynamicMapperHandler
 *
 * @author VampireAchao
 * @since 2023/1/8
 */
public class StreamQueryHandler {
  private static final String CURRENT_NAMESPACE =
      LambdaHelper.getPropertyName(TableInfo::getCurrentNamespace);

  public StreamQueryHandler(
      SqlSessionFactory sqlSessionFactory, StreamScanConfig streamScanConfig) {
    Configuration configuration = sqlSessionFactory.getConfiguration();
    GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);
    globalConfig.setSqlInjector(
        new DefaultSqlInjector() {
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
            Class<?> modelClass =
                ReflectionKit.getSuperClassGenericType(mapperClass, Mapper.class, 0);
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
        });

    if (configuration instanceof MybatisConfiguration) {
      MybatisConfiguration mybatisConfiguration = (MybatisConfiguration) configuration;
      streamScanConfig.scan();
      Collection<Class<?>> entityClassList = streamScanConfig.getEntityClasses();
      entityClassList.forEach(
          entityClass -> Database.buildMapper(mybatisConfiguration, entityClass));
    }
  }
}
