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
package org.dromara.streamquery.stream.plugin.solon.engine.enumration;

/**
 * sql方法类型
 *
 * @author VampireAchao Cizai_
 */
public enum SqlMethodEnum {
  /** 插入 */
  SAVE_ONE_SQL("saveOneSql", "插入多条数据（mysql语法批量）", "INSERT INTO %s %s VALUES %s"),
  UPDATE_ONE_SQL("updateOneSql", "修改多条数据（mysql语法批量）", "\nUPDATE %s SET %s WHERE %s\n");

  private final String method;
  private final String desc;
  private final String sql;

  SqlMethodEnum(String method, String desc, String sql) {
    this.method = method;
    this.desc = desc;
    this.sql = sql;
  }

  /**
   * Getter for the field <code>method</code>.
   *
   * @return a {@link String} object
   */
  public String getMethod() {
    return method;
  }

  /**
   * Getter for the field <code>desc</code>.
   *
   * @return a {@link String} object
   */
  public String getDesc() {
    return desc;
  }

  /**
   * Getter for the field <code>sql</code>.
   *
   * @return a {@link String} object
   */
  public String getSql() {
    return sql;
  }
}
