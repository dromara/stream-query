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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import lombok.val;
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

/**
 * @author VampireAchao
 * @since 2023/4/13 18:02
 */
@MybatisPlusTest
class WrapperHelperTest {

  @Test
  void testMultiIn() {
    LambdaQueryWrapper<UserInfo> wrapper =
        WrapperHelper.multiIn(
            Wrappers.lambdaQuery(UserInfo.class),
            Lists.of(
                new UserInfo() {
                  {
                    setName("Jon");
                  }
                },
                new UserInfo() {
                  {
                    setEmail("test2@baomidou.com");
                  }
                },
                new UserInfo() {
                  {
                    setName("Tom");
                  }
                }));
    // ==>  Preparing: SELECT id,name,age,email,version,gmt_deleted FROM user_info WHERE
    // gmt_deleted='2001-01-01 00:00:00' AND ((name IN (?,?) OR email IN (?)))
    // ==> Parameters: Jon(String), Tom(String), test2@baomidou.com(String)
    List<UserInfo> userInfos = Database.list(wrapper);
    Assertions.assertEquals("Jon", userInfos.get(0).getName());
    Assertions.assertEquals("test2@baomidou.com", userInfos.get(1).getEmail());
    Assertions.assertEquals("Tom", userInfos.get(2).getName());
  }

  @Test
  void testMultiOr() {
    val dataList =
        Lists.of(
            new UserInfo() {
              {
                setName("Jon");
              }
            },
            new UserInfo() {
              {
                setEmail("test2@baomidou.com");
              }
            },
            new UserInfo() {
              {
                setName("Tom");
              }
            });
    val wrapper =
        WrapperHelper.multiOr(
            Wrappers.lambdaQuery(UserInfo.class),
            dataList,
            (w, data) -> {
              w.eq(Objects.nonNull(data.getEmail()), UserInfo::getEmail, data.getEmail())
                  .eq(StringUtils.isNotBlank(data.getName()), UserInfo::getName, data.getName());
            });
    // ==>  Preparing: SELECT id,name,age,email,version,gmt_deleted FROM user_info WHERE
    // gmt_deleted='2001-01-01 00:00:00' AND ((name = ? OR email = ? OR name = ?))
    // ==> Parameters: Jon(String), test2@baomidou.com(String), Tom(String)
    List<UserInfo> userInfos = Database.list(wrapper);
    Assertions.assertEquals("Jon", userInfos.get(0).getName());
    Assertions.assertEquals("test2@baomidou.com", userInfos.get(1).getEmail());
    Assertions.assertEquals("Tom", userInfos.get(2).getName());
  }
}
