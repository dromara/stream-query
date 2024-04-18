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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.handler;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.handlers.PostInitTableInfoHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.session.Configuration;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author VampireAchao
 * @since 2023/3/20 18:10
 */
public class JsonPostInitTableInfoHandler implements PostInitTableInfoHandler {

  /**
   * 参与 TableInfo 初始化
   *
   * @param tableInfo TableInfo
   * @param configuration Configuration
   */
  @Override
  public void postTableInfo(TableInfo tableInfo, Configuration configuration) {
    PostInitTableInfoHandler.super.postTableInfo(tableInfo, configuration);
    Annotation[] annotations = tableInfo.getEntityType().getAnnotations();
    for (Annotation annotation : annotations) {
      if (annotation.annotationType().isAnnotationPresent(TableName.class)) {
        Map<String, Object> annotationAttributes =
            AnnotationUtils.getAnnotationAttributes(annotation);
        TableName synthesizedTableName =
            AnnotationUtils.synthesizeAnnotation(
                annotationAttributes, TableName.class, tableInfo.getEntityType());
        String tableNamePropertyName = LambdaHelper.getPropertyName(TableInfo::getTableName);
        String tableNameValue = synthesizedTableName.value();
        ReflectHelper.setFieldValue(tableInfo, tableNamePropertyName, tableNameValue);
        break;
      }
    }
  }
}
