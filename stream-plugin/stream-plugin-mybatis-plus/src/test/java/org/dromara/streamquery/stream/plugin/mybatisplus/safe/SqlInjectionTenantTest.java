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

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.exceptions.PersistenceException;
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.plugin.mybatisplus.Database;
import org.dromara.streamquery.stream.plugin.mybatisplus.InterceptorTest;
import org.dromara.streamquery.stream.plugin.mybatisplus.QueryCondition;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.interceptor.SqTenantLineInnerInterceptor;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.ProductInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Cason
 * @since 2023-06-27
 */
@MybatisPlusTest
class SqlInjectionTenantTest extends InterceptorTest {

  @Override
  public List<InnerInterceptor> interceptors() {
    return Lists.of(
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
            }));
  }

  @Test
  void queryTest() {
    QueryCondition<ProductInfo> wrapper =
        QueryCondition.query(ProductInfo.class).eq(ProductInfo::getId, 1L);
    Throwable exception =
        Assertions.assertThrows(PersistenceException.class, () -> Database.list(wrapper));
    Assertions.assertTrue(exception.getCause() instanceof IllegalArgumentException);
    Assertions.assertEquals(
        "SQL Injection attempt detected in 'TenantLineInnerInterceptor'",
        exception.getCause().getMessage());
  }

  @Test
  void insertTest() {
    ProductInfo product = new ProductInfo();
    product.setId(2L);
    product.setProductName("Product 2");
    Throwable exception =
        Assertions.assertThrows(PersistenceException.class, () -> Database.save(product));
    Assertions.assertTrue(exception.getCause() instanceof IllegalArgumentException);
    Assertions.assertEquals(
        "SQL Injection attempt detected in 'TenantLineInnerInterceptor'",
        exception.getCause().getMessage());
  }

  @Test
  void updateTest() {
    ProductInfo product = new ProductInfo();
    product.setId(1L);
    product.setProductName("Updated Product 1");
    Throwable exception =
        Assertions.assertThrows(PersistenceException.class, () -> Database.updateById(product));
    Assertions.assertTrue(exception.getCause() instanceof IllegalArgumentException);
    Assertions.assertEquals(
        "SQL Injection attempt detected in 'TenantLineInnerInterceptor'",
        exception.getCause().getMessage());
  }
}
