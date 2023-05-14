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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.constant.PluginConst;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.enumration.SqlMethodEnum;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.IN;

/**
 * 修改多条数据（mysql语法批量）
 *
 * @author sikadai VampireAchao
 */
public class UpdateOneSql extends AbstractMethod implements PluginConst {

  /**
   * Constructor for UpdateOneSql.
   *
   * @param methodName a {@link java.lang.String} object
   */
  public UpdateOneSql(String methodName) {
    super(methodName);
  }

  /** Constructor for UpdateOneSql. */
  public UpdateOneSql() {
    super(SqlMethodEnum.UPDATE_ONE_SQL.getMethod());
  }

  /** 注入自定义 MappedStatement */
  @Override
  public MappedStatement injectMappedStatement(
      Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
    SqlMethodEnum sqlMethod = SqlMethodEnum.UPDATE_ONE_SQL;
    String caseWhenScript = buildCaseWhen(tableInfo);
    StringBuilder whereScript = buildWhereSql(tableInfo);
    String sql =
        String.format(
            SCRIPT_TAGS,
            SqlScriptUtils.convertIf(
                sqlMethod.getSql(),
                String.format(NON_EMPTY_CONDITION, COLLECTION_PARAM_NAME, COLLECTION_PARAM_NAME),
                true));
    sql = String.format(sql, tableInfo.getTableName(), caseWhenScript, whereScript);
    SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
    return this.addUpdateMappedStatement(mapperClass, modelClass, sqlSource);
  }

  /**
   * 构建caseWhen的更新语句
   *
   * @param tableInfo 表信息
   * @return caseWhen的更新语句
   * @author VampireAchao sikadai
   * @since 2022/8/24 18:42
   */
  private String buildCaseWhen(TableInfo tableInfo) {
    String safeKeyProperty = SqlScriptUtils.safeParam(ENTITY_DOT + tableInfo.getKeyProperty());
    return Steam.of(tableInfo.getFieldList())
            .filter(i -> !i.isLogicDelete())
            .map(
                i ->
                    i.getColumn()
                        + EQUALS
                        + CASE
                        + SPACE
                        + tableInfo.getKeyColumn()
                        + NEWLINE
                        + SqlScriptUtils.convertForeach(
                            SqlScriptUtils.convertChoose(
                                MpInjectHelper.updateCondition(
                                    i, TableFieldInfo::getUpdateStrategy),
                                String.format(
                                    WHEN_THEN,
                                    safeKeyProperty,
                                    SqlScriptUtils.safeParam(ENTITY_DOT + i.getProperty())),
                                String.format(WHEN_THEN, safeKeyProperty, i.getColumn())),
                            COLLECTION_PARAM_NAME,
                            null,
                            ENTITY,
                            null)
                        + END)
            .join(COMMA + NEWLINE)
        + NEWLINE;
  }

  /**
   * 构建where的批量更新
   *
   * @param tableInfo 表信息
   * @return java.lang.StringBuilder
   * @author VampireAchao sikadai
   * @since 2022/8/24 18:43
   */
  private StringBuilder buildWhereSql(TableInfo tableInfo) {
    return new StringBuilder()
        .append(NEWLINE)
        .append(tableInfo.getKeyColumn())
        .append(SPACE)
        .append(IN.getSqlSegment())
        .append(NEWLINE)
        .append(LEFT_BRACKET)
        .append(
            SqlScriptUtils.convertForeach(
                SqlScriptUtils.safeParam(ENTITY_DOT + tableInfo.getKeyProperty()),
                COLLECTION_PARAM_NAME,
                null,
                ENTITY,
                COMMA))
        .append(RIGHT_BRACKET)
        .append(NEWLINE)
        .append(tableInfo.getLogicDeleteSql(true, true));
  }
}
