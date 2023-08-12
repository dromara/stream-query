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
package org.dromara.streamquery.stream.plugin.solon.features;


import org.apache.ibatis.exceptions.TooManyResultsException;
import org.dromara.streamquery.stream.plugin.solon.One;
import org.dromara.streamquery.stream.plugin.solon.config.DemoApp;
import org.dromara.streamquery.stream.plugin.solon.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.solon.test.SolonJUnit5Extension;
import org.noear.solon.test.SolonTest;

/**
 * 查询单条测试
 *
 * @author VampireAchao Cizai_
 * @since 2022/6/18 14:25
 */
@ExtendWith(SolonJUnit5Extension.class)
@SolonTest(DemoApp.class)
public class OneTest {

  @Test
  void testQuery() {
    UserInfo userInfo = One.of(UserInfo::getId).eq(1L).query();
    String name = One.of(UserInfo::getId).eq(1L).value(UserInfo::getName).query();
    String leAgeName =
        One.of(UserInfo::getId)
            .eq(1L)
            .value(UserInfo::getName)
            .condition(w -> w.le(UserInfo::getAge, 20))
            .query();
    Assertions.assertNotNull(userInfo);
    Assertions.assertNotNull(name);
    Assertions.assertNotNull(leAgeName);
    Assertions.assertThrows(
        TooManyResultsException.class, () -> One.of(UserInfo::getName).like("a").query());
  }
}
