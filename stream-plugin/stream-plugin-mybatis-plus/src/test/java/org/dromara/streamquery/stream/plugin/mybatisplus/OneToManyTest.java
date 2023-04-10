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
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 一对多测试
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/23 13:51
 */
@MybatisPlusTest
class OneToManyTest {
  @Test
  void testQuery() {
    Assertions.assertAll(
        () -> {
          List<Integer> userAges = Arrays.asList(18, 18, 28, 21, 24);
          Map<Integer, List<UserInfo>> ageUsersMap =
              OneToMany.of(UserInfo::getAge).in(userAges).parallel().query();
          Assertions.assertEquals(4, ageUsersMap.size());

          List<String> userNames = Arrays.asList("Jon", "Jack", "Tom", "Billie");
          Map<String, List<UserInfo>> nameUsersMap =
              OneToMany.of(UserInfo::getName)
                  .in(userNames)
                  .condition(w -> w.le(UserInfo::getAge, 21))
                  .query();
          Assertions.assertEquals(2, nameUsersMap.size());

          Map<Integer, List<String>> userAgeNameMap =
              OneToMany.of(UserInfo::getAge).in(userAges).value(UserInfo::getName).query();
          Assertions.assertEquals(4, userAgeNameMap.size());

          userAgeNameMap =
              OneToMany.of(UserInfo::getAge)
                  .in(userAges)
                  .value(UserInfo::getName)
                  .condition(w -> w.le(UserInfo::getAge, 22))
                  .query();
          Assertions.assertEquals(2, userAgeNameMap.size());

          ageUsersMap = OneToMany.of(UserInfo::getAge).eq(18).query();
          Assertions.assertEquals(1, ageUsersMap.size());

          userAgeNameMap = OneToMany.of(UserInfo::getAge).eq(18).value(UserInfo::getName).query();
          Assertions.assertEquals(1, userAgeNameMap.size());

          userAgeNameMap =
              OneToMany.of(UserInfo::getAge)
                  .eq(18)
                  .value(UserInfo::getName)
                  .condition(w -> w.le(UserInfo::getAge, 22))
                  .query();
          Assertions.assertEquals(1, userAgeNameMap.size());

          Map<Integer, List<Boolean>> query =
              OneToMany.of(UserInfo::getAge)
                  .in(userAges)
                  .value(userInfo -> userInfo.getName() != null && userInfo.getName().contains("a"))
                  .condition(w -> w.select(UserInfo::getAge, UserInfo::getName))
                  .query();
          Assertions.assertEquals(
              2,
              query.values().stream()
                  .flatMap(Collection::stream)
                  .filter(Boolean::booleanValue)
                  .count());
        });
  }
}
