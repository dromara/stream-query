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

import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.plugin.solon.Many;
import org.dromara.streamquery.stream.plugin.solon.config.DemoApp;
import org.dromara.streamquery.stream.plugin.solon.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.solon.test.SolonJUnit5Extension;
import org.noear.solon.test.SolonTest;

import java.util.Collections;
import java.util.List;

/**
 * 查询多条测试
 *
 * @author VampireAchao Cizai_
 * @since 2022/6/18 14:25
 */
@ExtendWith(SolonJUnit5Extension.class)
@SolonTest(DemoApp.class)
public class ManyTest {

  private static final Long NULL_LONG_VALUE = Opp.<Long>empty().get();

  @Test
  void testQuery() {

    List<UserInfo> userInfoList = Many.of(UserInfo::getId).eq(1L).parallel().query();
    List<String> nameList =
        Many.of(UserInfo::getId).eq(1L).value(UserInfo::getName).sequential().query();
    List<String> leAgeNameList =
        Many.of(UserInfo::getId)
            .eq(1L)
            .value(UserInfo::getName)
            .condition(w -> w.le(UserInfo::getAge, 20))
            .query();
    Assertions.assertFalse(userInfoList.isEmpty());
    Assertions.assertFalse(nameList.isEmpty());
    Assertions.assertFalse(leAgeNameList.isEmpty());
  }

  @Test
  void testPart() {
    List<UserInfo> userInfoList = Many.of(UserInfo::getId).query();
    Assertions.assertFalse(userInfoList.isEmpty());
  }

  @Test
  void testNoQuery() {
    UserInfo[] eqNullUserArray =
        Many.of(UserInfo::getId).eq(NULL_LONG_VALUE).query(s -> s.toArray(UserInfo[]::new));
    Assertions.assertArrayEquals(new UserInfo[0], eqNullUserArray);

    List<String> inEmptyNameList =
        Many.of(UserInfo::getId).in(Collections.emptyList()).value(UserInfo::getName).query();
    Assertions.assertTrue(inEmptyNameList.isEmpty());

    List<UserInfo> conditionNullUserList = Many.of(UserInfo::getId).condition(w -> null).query();
    Assertions.assertTrue(conditionNullUserList.isEmpty());
  }
}
