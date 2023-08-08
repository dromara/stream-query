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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.plugin.solon.Database;
import org.dromara.streamquery.stream.plugin.solon.config.DemoApp;
import org.dromara.streamquery.stream.plugin.solon.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.solon.test.SolonJUnit5Extension;
import org.noear.solon.test.SolonTest;

import java.util.Arrays;
import java.util.List;

/** @author noear 2021/9/3 created */
@ExtendWith(SolonJUnit5Extension.class)
@SolonTest(DemoApp.class)
public class DataBaseTest {

  @Test
  void testSaveFewSql() {
    UserInfo entity = new UserInfo();
    entity.setName("cat");
    entity.setAge(20);
    entity.setEmail("myEmail");
    UserInfo userInfo = new UserInfo();
    userInfo.setName("ruben");
    List<UserInfo> list = Arrays.asList(userInfo, entity);
    boolean isSuccess = Database.saveFewSql(list);
    Assertions.assertTrue(isSuccess);
    Assertions.assertEquals(7, Database.count(UserInfo.class));

    Assertions.assertFalse(Database.saveFewSql(Lists.empty()));
  }

  @Test
  void testGetOne() {
    LambdaQueryWrapper<UserInfo> wrapper = Wrappers.lambdaQuery(UserInfo.class);
    Assertions.assertThrows(TooManyResultsException.class, () -> Database.getOne(wrapper));
    UserInfo one = Database.getOne(wrapper, false);
    Assertions.assertNotNull(one);
  }
}
