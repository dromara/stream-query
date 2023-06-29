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
package org.dromara.streamquery.stream.plugin.mybatisplus.safe;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import lombok.val;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.exceptions.PersistenceException;
import org.dromara.streamquery.stream.plugin.mybatisplus.Database;
import org.dromara.streamquery.stream.plugin.mybatisplus.MybatisPlusTestApplication;
import org.dromara.streamquery.stream.plugin.mybatisplus.QueryCondition;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.interceptor.SqTenantLineInnerInterceptor;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.ProductInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Cason
 * @since 2023-06-27
 */
@MybatisPlusTest
@ContextConfiguration(
    classes = {MybatisPlusTestApplication.class, SqlInjectionTenantTest.TenantPluginConfig.class})
public class SqlInjectionTenantTest {
  static class TenantPluginConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor2() {
      MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
      interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
      interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
      interceptor.addInnerInterceptor(
          new SqTenantLineInnerInterceptor(
              new TenantLineHandler() {
                @Override
                public Expression getTenantId() {
                  return new StringValue("' or 1=1 and '123'='123");
                }

                @Override
                public String getTenantIdColumn() {
                  return "tenant_id";
                }

                @Override
                public boolean ignoreTable(String tableName) {
                  return "user_info".equalsIgnoreCase(tableName);
                }
              }));
      return interceptor;
    }
  }

  @Test
  void TenantTest() {
    Throwable exception =
        Assertions.assertThrows(
            PersistenceException.class,
            () -> {
              QueryCondition<ProductInfo> wrapper =
                  QueryCondition.query(ProductInfo.class).eq(ProductInfo::getId, 1L);
              val list = Database.list(wrapper);
            });

    Assertions.assertTrue(exception.getCause() instanceof IllegalArgumentException);
    Assertions.assertEquals(
        "SQL Injection attempt detected in 'TenantLineInnerInterceptor'",
        exception.getCause().getMessage());
  }
}
