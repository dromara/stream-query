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

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.dromara.streamquery.stream.plugin.mybatisplus.QueryCondition;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Cason
 * @since 2023-06-27
 */
@MybatisPlusTest
class SqlInjectionWrapperTest {

  @Test
  void applyTest() {
    String unsafeSql = "1 = 1) OR 1 = 1 --";
    Throwable exception =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> QueryCondition.query(UserInfo.class).apply(unsafeSql));

    Assertions.assertEquals("SQL Injection attempt detected in 'apply'", exception.getMessage());
  }

  @Test
  void havingApply() {
    String unsafeSql = "1 = 1) OR 1 = 1 --";
    Throwable exception =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> QueryCondition.query(UserInfo.class).having(unsafeSql));

    Assertions.assertEquals("SQL Injection attempt detected in 'having'", exception.getMessage());
  }
}
