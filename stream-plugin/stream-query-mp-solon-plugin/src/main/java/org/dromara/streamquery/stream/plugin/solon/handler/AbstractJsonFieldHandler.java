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

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.solon.handlers.AbstractJsonTypeHandler;


/**
 * @author VampireAchao
 * @since 2023/4/14 13:45
 */
public abstract class AbstractJsonFieldHandler<T> extends AbstractJsonTypeHandler<T> {

  private TableInfo tableInfo;

  private TableFieldInfo fieldInfo;

  public TableInfo getTableInfo() {
    return tableInfo;
  }

  public void setTableInfo(TableInfo tableInfo) {
    this.tableInfo = tableInfo;
  }

  public TableFieldInfo getFieldInfo() {
    return fieldInfo;
  }

  public void setFieldInfo(TableFieldInfo fieldInfo) {
    this.fieldInfo = fieldInfo;
  }

  @Override
  protected T parse(String json) {
    return parse(json, tableInfo, fieldInfo);
  }

  @Override
  protected String toJson(T obj) {
    return toJson(obj, tableInfo, fieldInfo);
  }

  protected abstract T parse(String json, TableInfo tableInfo, TableFieldInfo fieldInfo);

  protected abstract String toJson(T obj, TableInfo tableInfo, TableFieldInfo fieldInfo);
}
