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
package org.dromara.streamquery.stream.plugin.mybatisplus.issue.gitee.issue17BSNV;

import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.plugin.mybatisplus.Database;
import org.dromara.streamquery.stream.plugin.mybatisplus.annotation.AbstractMybatisPlusTestApplication;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.annotation.EnableMybatisPlusPlugin;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.mapper.IMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * @author VampireAchao
 * @since 2023/6/8
 */
@EnableAutoConfiguration
@EnableMybatisPlusPlugin
class RevisionTest extends AbstractMybatisPlusTestApplication {

  @Test
  void testExecute() {
    UserInfoWithTableAnnotation entity = new UserInfoWithTableAnnotation();
    entity.setName("cat");
    entity.setAge(20);
    entity.setEmail("myEmail");
    UserInfoWithTableAnnotation userInfo = new UserInfoWithTableAnnotation();
    userInfo.setName("ruben");
    List<UserInfoWithTableAnnotation> list = Arrays.asList(userInfo, entity);
    long effectRows =
        Database.execute(
            UserInfoWithTableAnnotation.class,
            (IMapper<UserInfoWithTableAnnotation> m) -> m.saveOneSql(list));
    Assertions.assertEquals(2, effectRows);
    Assertions.assertEquals(7, Database.count(UserInfoWithTableAnnotation.class));

    Assertions.assertEquals(
        0L,
        Database.execute(
            UserInfoWithTableAnnotation.class,
            (IMapper<UserInfoWithTableAnnotation> m) -> m.saveOneSql(Lists.empty())));
  }
}
