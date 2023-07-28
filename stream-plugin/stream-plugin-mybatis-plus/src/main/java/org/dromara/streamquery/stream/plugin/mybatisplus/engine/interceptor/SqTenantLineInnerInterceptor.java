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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration.StreamPluginConfig;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.utils.SqlInjectionUtilSq;

/**
 * @author Cason
 * @since 2023-06-23 21:36
 */
public class SqTenantLineInnerInterceptor extends TenantLineInnerInterceptor {
  public SqTenantLineInnerInterceptor(TenantLineHandler tenantLineHandler) {
    super(tenantLineHandler);
  }

  @Override
  protected void processInsert(Insert insert, int index, String sql, Object obj) {
    checkTenantId();
    super.processInsert(insert, index, sql, obj);
  }

  @Override
  public Expression buildTableExpression(Table table, Expression where, String whereSegment) {
    checkTenantId();
    return super.buildTableExpression(table, where, whereSegment);
  }

  private void checkTenantId() {
    if (!StreamPluginConfig.isSafeModeEnabled()) {
      return;
    }
    Expression tenantId = this.getTenantLineHandler().getTenantId();
    if (tenantId instanceof StringValue) {
      StringValue stringValue = (StringValue) tenantId;
      String tenantIdStr = stringValue.getValue();
      if (SqlInjectionUtilSq.check(tenantIdStr)) {
        throw new IllegalArgumentException(
            "SQL Injection attempt detected in 'TenantLineInnerInterceptor'");
      }
    }
  }
}
