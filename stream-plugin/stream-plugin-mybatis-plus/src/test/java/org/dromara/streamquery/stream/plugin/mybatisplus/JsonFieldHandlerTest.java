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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.streamquery.stream.core.lambda.function.SerSupp;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.handler.AbstractJsonFieldHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author VampireAchao
 * @since 2023/4/14 13:57
 */
@MybatisPlusTest
class JsonFieldHandlerTest {

  @BeforeEach
  void init(@Autowired SqlSessionFactory sqlSessionFactory) {
    Database.buildMapper(sqlSessionFactory.getConfiguration(), UserInfoWithJsonName.class);
  }

  @Test
  void test() {
    val user =
            new UserInfoWithJsonName() {
              {
                setName(
                        new Name() {
                          {
                            setUsername("VampireAchao");
                            setNickname("阿超");
                          }
                        });
              }
            };
    Database.save(user);
    val dbUser = Database.getById(user.getId(), UserInfoWithJsonName.class);
    Assertions.assertEquals("VampireAchao", dbUser.getName().getUsername());
    Assertions.assertEquals("阿超", dbUser.getName().getNickname());
  }

  public static class JsonFieldHandler extends AbstractJsonFieldHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected Object parse(String json, TableInfo tableInfo, TableFieldInfo fieldInfo) {
      Class<?> fieldType = fieldInfo.getField().getType();
      return ((SerSupp<Object>) (() -> objectMapper.readValue(json, fieldType))).get();
    }

    @Override
    @SneakyThrows
    protected String toJson(Object obj, TableInfo tableInfo, TableFieldInfo fieldInfo) {
      return objectMapper.writeValueAsString(obj);
    }
  }

  @Data
  @TableName(value = "user_info", autoResultMap = true)
  static class UserInfoWithJsonName {
    private Long id;

    @TableField(typeHandler = JsonFieldHandler.class)
    private Name name;
  }

  @Data
  static class Name {
    private String username;
    private String nickname;
  }
}
