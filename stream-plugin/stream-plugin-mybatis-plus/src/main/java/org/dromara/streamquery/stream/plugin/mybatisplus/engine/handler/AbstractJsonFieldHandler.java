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

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.dromara.streamquery.stream.core.collection.Maps;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author VampireAchao
 * @since 2023/4/14 13:45
 */
public abstract class AbstractJsonFieldHandler<T> extends BaseTypeHandler<T> {

  private final Map<String, TableFieldInfo> columnFieldInfoMap = Maps.of();

  private TableInfo tableInfo;

  public TableInfo getTableInfo() {
    return tableInfo;
  }

  public void setTableInfo(TableInfo tableInfo) {
    this.tableInfo = tableInfo;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setString(i, toJson(parameter));
  }

  protected abstract T parse(String json, TableInfo tableInfo, TableFieldInfo fieldInfo);

  protected abstract String toJson(T obj);

  protected TableFieldInfo getTableFieldInfo(String columnName) {
    if (columnFieldInfoMap.isEmpty()) {
      columnFieldInfoMap.putAll(
          Steam.of(getTableInfo().getFieldList()).toMap(TableFieldInfo::getColumn));
    }
    return columnFieldInfoMap.get(columnName);
  }

  /**
   * Gets the nullable result.
   *
   * @param rs the rs
   * @param columnName Column name, when configuration <code>useColumnLabel</code> is <code>false
   *                   </code>
   * @return the nullable result
   * @throws SQLException the SQL exception
   */
  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    final String json = rs.getString(columnName);
    return StringUtils.isBlank(json) ? null : parse(json, tableInfo, getTableFieldInfo(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String columnName = rs.getMetaData().getColumnName(columnIndex);
    return getNullableResult(rs, columnName);
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return getNullableResult(cs.getResultSet(), columnIndex);
  }
}
