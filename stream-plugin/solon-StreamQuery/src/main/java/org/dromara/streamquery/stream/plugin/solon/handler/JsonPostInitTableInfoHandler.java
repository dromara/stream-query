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


import com.baomidou.mybatisplus.core.handlers.PostInitTableInfoHandler;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

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

    for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
      if (fieldInfo.getTypeHandler() == null) {
        continue;
      }
      if (tableInfo.getResultMap() == null) {
        return;
      }
      ResultMap resultMap = configuration.getResultMap(tableInfo.getResultMap());
      for (ResultMapping resultMapping : resultMap.getResultMappings()) {
        TypeHandler<?> handler = resultMapping.getTypeHandler();
        if (handler instanceof AbstractJsonFieldHandler) {
          AbstractJsonFieldHandler<?> typeHandler = (AbstractJsonFieldHandler<?>) handler;
          typeHandler.setTableInfo(tableInfo);
          typeHandler.setFieldInfo(fieldInfo);
        }
      }
    }
  }
}
