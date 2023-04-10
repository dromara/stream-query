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
package org.dromara.streamquery.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 一对一测试
 *
 * @author VampireAchao Cizai_
 */
@MybatisPlusTest
class OneToOneTest {

  @Test
  void testQuery() {
    List<Long> userIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
    Map<Long, UserInfo> idUserMap = OneToOne.of(UserInfo::getId).in(userIds).query();
    Assertions.assertEquals(5, idUserMap.size());

    Map<Long, String> userIdNameMap =
        OneToOne.of(UserInfo::getId).in(userIds).value(UserInfo::getName).query();
    Assertions.assertEquals(5, userIdNameMap.size());

    userIdNameMap =
        OneToOne.of(UserInfo::getId)
            .in(userIds)
            .value(UserInfo::getName)
            .condition(w -> w.le(UserInfo::getAge, 22))
            .query();
    Assertions.assertEquals(3, userIdNameMap.size());

    idUserMap = OneToOne.of(UserInfo::getId).eq(1L).query();
    Assertions.assertEquals(1, idUserMap.size());

    userIdNameMap = OneToOne.of(UserInfo::getId).eq(1L).value(UserInfo::getName).query();
    Assertions.assertEquals(1, userIdNameMap.size());

    userIdNameMap =
        OneToOne.of(UserInfo::getId)
            .eq(1L)
            .value(UserInfo::getName)
            .condition(w -> w.le(UserInfo::getAge, 22))
            .query();
    Assertions.assertEquals(1, userIdNameMap.size());

    Map<Long, Boolean> query =
        OneToOne.of(UserInfo::getId)
            .in(userIds)
            .condition(w -> w.select(UserInfo::getId, UserInfo::getName))
            .value(userInfo -> userInfo.getName() != null && userInfo.getName().contains("a"))
            .query();
    Assertions.assertEquals(2, query.values().stream().filter(Boolean::booleanValue).count());
  }
}
