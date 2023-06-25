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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.constant.PluginConst;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.enumration.SqlMethodEnum;

/**
 * 插入多条数据（mysql语法批量）
 *
 * @author VampireAchao Cizai_
 */
public class SaveOneSql extends AbstractMethod implements PluginConst {

  /**
   * Constructor for SaveOneSql.
   *
   * @param methodName a {@link java.lang.String} object
   */
  public SaveOneSql(String methodName) {
    super(methodName);
  }

  /** Constructor for SaveOneSql. */
  public SaveOneSql() {
    super(SqlMethodEnum.SAVE_ONE_SQL.getMethod());
  }

  /** 注入自定义 MappedStatement */
  @Override
  public MappedStatement injectMappedStatement(
      Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
    KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
    SqlMethodEnum sqlMethod = SqlMethodEnum.SAVE_ONE_SQL;
    // column script
    String columnScript =
        SqlScriptUtils.convertTrim(
            Steam.of(tableInfo.getFieldList())
                .map(TableFieldInfo::getColumn)
                .unshift(tableInfo.getKeyColumn())
                .join(COMMA),
            LEFT_BRACKET,
            RIGHT_BRACKET,
            null,
            COMMA);
    // value column script in loop
    String safeProperty = SqlScriptUtils.safeParam(ENTITY_DOT + tableInfo.getKeyProperty()) + COMMA;
    String propertyOrDefault =
        SqlScriptUtils.convertChoose(
            String.format(NON_NULL_CONDITION, ENTITY, ENTITY_DOT + tableInfo.getKeyProperty()),
            safeProperty,
            DEFAULT + COMMA);
    String valuesScript =
        SqlScriptUtils.convertTrim(
            Steam.of(tableInfo.getFieldList())
                .map(
                    i ->
                        SqlScriptUtils.convertChoose(
                            MpInjectHelper.updateCondition(i, TableFieldInfo::getInsertStrategy),
                            i.getInsertSqlProperty(ENTITY_DOT),
                            DEFAULT + COMMA))
                .unshift(tableInfo.getIdType() == IdType.AUTO ? propertyOrDefault : safeProperty)
                .nonNull()
                .join(NEWLINE),
            LEFT_BRACKET,
            RIGHT_BRACKET,
            null,
            COMMA);
    // value part into foreach
    valuesScript =
        SqlScriptUtils.convertForeach(valuesScript, COLLECTION_PARAM_NAME, null, ENTITY, COMMA);

    String keyProperty = null;
    String keyColumn = null;
    // 表包含主键处理逻辑,如果不包含主键当普通字段处理
    if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
      if (tableInfo.getIdType() == IdType.AUTO) {
        /* 自增主键 */
        keyGenerator = Jdbc3KeyGenerator.INSTANCE;
        keyProperty = tableInfo.getKeyProperty();
        keyColumn = tableInfo.getKeyColumn();
      } else if (null != tableInfo.getKeySequence()) {
        keyGenerator =
            TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
        keyProperty = tableInfo.getKeyProperty();
        keyColumn = tableInfo.getKeyColumn();
      }
    }
    String sql =
        String.format(
            SCRIPT_TAGS,
            SqlScriptUtils.convertIf(
                sqlMethod.getSql(),
                String.format(NON_EMPTY_CONDITION, COLLECTION_PARAM_NAME, COLLECTION_PARAM_NAME),
                true));
    sql = String.format(sql, tableInfo.getTableName(), columnScript, valuesScript);
    SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
    return this.addInsertMappedStatement(
        mapperClass, modelClass, sqlSource, keyGenerator, keyProperty, keyColumn);
  }
}
