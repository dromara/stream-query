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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.core.collection.Maps;
import org.dromara.streamquery.stream.core.lambda.function.SerSupp;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.handler.AbstractJsonFieldHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author VampireAchao Cason
 * @since 2023/4/14
 */
@MybatisPlusTest
class JsonFieldHandlerTest {

  @BeforeEach
  void init(@Autowired SqlSessionFactory sqlSessionFactory) {
    Database.buildMapper(sqlSessionFactory.getConfiguration(), UserInfoWithJsonName.class);
    Database.buildMapper(sqlSessionFactory.getConfiguration(), UserInfoWithMapName.class);
  }

  @Test
  void testJsonSaveAndQuery() {
    val emails =
        Lists.of(
            new Email() {
              {
                setEmail("achao@apache.org");
                setDomain("apache.org");
              }
            },
            new Email() {
              {
                setEmail("vampireachao@dromara.org");
                setDomain("dromara.org");
              }
            },
            new Email() {
              {
                setEmail("achao@hutool.cn");
                setDomain("hutool.cn");
              }
            });
    val userInfoWithJsonName =
        new UserInfoWithJsonName() {
          {
            setName(
                new Name() {
                  {
                    setUsername("VampireAchao");
                    setNickname("阿超");
                  }
                });
            setEmail(emails);
          }
        };
    Database.saveFewSql(Lists.of(userInfoWithJsonName));
    Database.updateFewSql(Lists.of(userInfoWithJsonName));
    val dbUserInfoWithJsonName =
        Database.getById(userInfoWithJsonName.getId(), UserInfoWithJsonName.class);
    Assertions.assertEquals("VampireAchao", dbUserInfoWithJsonName.getName().getUsername());
    Assertions.assertEquals("阿超", dbUserInfoWithJsonName.getName().getNickname());
    Assertions.assertEquals(emails, dbUserInfoWithJsonName.getEmail());
    val nameMap =
        Maps.of(
            "zh",
            new Name() {
              {
                setUsername("吸血鬼阿超");
                setNickname("阿超");
              }
            },
            "en",
            new Name() {
              {
                setUsername("VampireAchao");
                setNickname("Achao");
              }
            });
    val userInfoWithJsonCollection =
        new UserInfoWithMapName() {
          {
            setName(nameMap);
            setEmail(emails);
          }
        };
    Database.saveFewSql(Lists.of(userInfoWithJsonCollection));
    Database.updateFewSql(Lists.of(userInfoWithJsonCollection));
    val dbUserInfoWithJsonCollection =
        Database.getById(userInfoWithJsonCollection.getId(), UserInfoWithMapName.class);
    Assertions.assertEquals(nameMap, dbUserInfoWithJsonCollection.getName());
    assertThat(dbUserInfoWithJsonCollection.getEmail()).containsExactlyElementsOf(emails);
  }

  @Test
  void eqTest() {
    Name name = new Name();
    name.setUsername("VampireAchao");
    name.setNickname("阿超");

    UserInfoWithJsonName user = new UserInfoWithJsonName();
    user.setName(name);
    Database.saveFewSql(Lists.of(user));
    Database.updateFewSql(Lists.of(user));

    QueryCondition<UserInfoWithJsonName> wrapper =
        QueryCondition.query(UserInfoWithJsonName.class).eq(UserInfoWithJsonName::getName, name);
    val list = Database.list(wrapper);

    assertEquals(1, list.size(), "Query should return exactly one result");
    assertEquals(
        name, list.get(0).getName(), "Returned user's name should match the expected name");
  }

  @Test
  void activeEqTest() {
    Name name = new Name();
    name.setUsername("Cason");
    name.setNickname("JAY");

    UserInfoWithJsonName user = new UserInfoWithJsonName();
    user.setName(name);

    Database.saveFewSql(Lists.of(user));

    QueryCondition<UserInfoWithJsonName> wrapper =
        QueryCondition.query(UserInfoWithJsonName.class)
            .activeEq(UserInfoWithJsonName::getName, name);

    val list = Database.list(wrapper);

    assertEquals(1, list.size(), "Query should return exactly one result");
    assertEquals(
        name.getUsername(),
        list.get(0).getName().getUsername(),
        "Returned user's username should match the expected username");
  }

  @Test
  void orTest() {
    Name name1 = new Name();
    name1.setUsername("Cason");
    name1.setNickname("JAY");

    Name name2 = new Name();
    name2.setUsername("Alice");
    name2.setNickname("AL");

    Name name3 = new Name();
    name3.setUsername("Bob");
    name3.setNickname("BB");

    UserInfoWithJsonName user1 = new UserInfoWithJsonName();
    user1.setName(name1);

    UserInfoWithJsonName user2 = new UserInfoWithJsonName();
    user2.setName(name2);

    UserInfoWithJsonName user3 = new UserInfoWithJsonName();
    user3.setName(name3);

    Database.saveFewSql(Lists.of(user1, user2, user3));

    QueryCondition<UserInfoWithJsonName> wrapper =
        QueryCondition.query(UserInfoWithJsonName.class)
            .in(UserInfoWithJsonName::getName, Lists.of(name1, name3))
            .or(i -> i.eq(UserInfoWithJsonName::getName, user2.getName()));

    List<UserInfoWithJsonName> list = Database.list(wrapper);

    assertEquals(3, list.size(), "Query should return exactly two results");

    List<String> usernames =
        list.stream().map(user -> user.getName().getUsername()).collect(Collectors.toList());

    assertTrue(
        usernames.contains(name1.getUsername()),
        "Returned users should contain the first expected username");
    assertTrue(
        usernames.contains(name3.getUsername()),
        "Returned users should contain the third expected username");
    assertTrue(
        usernames.contains(name2.getUsername()),
        "Returned users should not contain the second username");
  }

  @Test
  void andTest() {
    Name name1 = new Name();
    name1.setUsername("Cason");
    name1.setNickname("JAY");

    Name name2 = new Name();
    name2.setUsername("Alice");
    name2.setNickname("AL");

    Name name3 = new Name();
    name3.setUsername("Bob");
    name3.setNickname("BB");

    UserInfoWithJsonName user1 = new UserInfoWithJsonName();
    user1.setName(name1);

    UserInfoWithJsonName user2 = new UserInfoWithJsonName();
    user2.setName(name2);

    UserInfoWithJsonName user3 = new UserInfoWithJsonName();
    user3.setName(name3);

    Database.saveFewSql(Lists.of(user1, user2, user3));

    QueryCondition<UserInfoWithJsonName> wrapper =
        QueryCondition.query(UserInfoWithJsonName.class)
            .eq(UserInfoWithJsonName::getId, 1L)
            .and(i -> i.eq(UserInfoWithJsonName::getName, user2.getName()));

    val list = Database.list(wrapper);

    assertEquals(0, list.size(), "Query should return exactly zero result");
  }

  @Test
  void InTest() {
    Name name1 = new Name();
    name1.setUsername("Cason");
    name1.setNickname("JAY");

    Name name2 = new Name();
    name2.setUsername("Alice");
    name2.setNickname("AL");

    Name name3 = new Name();
    name3.setUsername("Bob");
    name3.setNickname("BB");

    UserInfoWithJsonName user1 = new UserInfoWithJsonName();
    user1.setName(name1);

    UserInfoWithJsonName user2 = new UserInfoWithJsonName();
    user2.setName(name2);

    UserInfoWithJsonName user3 = new UserInfoWithJsonName();
    user3.setName(name3);

    Database.saveFewSql(Lists.of(user1, user2, user3));

    QueryCondition<UserInfoWithJsonName> wrapper =
        QueryCondition.query(UserInfoWithJsonName.class)
            .activeIn(UserInfoWithJsonName::getName, Lists.of(name1, name3));

    List<UserInfoWithJsonName> list = Database.list(wrapper);

    assertEquals(2, list.size(), "Query should return exactly two results");

    List<String> usernames =
        list.stream().map(user -> user.getName().getUsername()).collect(Collectors.toList());

    assertTrue(
        usernames.contains(name1.getUsername()),
        "Returned users should contain the first expected username");
    assertTrue(
        usernames.contains(name3.getUsername()),
        "Returned users should contain the third expected username");
    assertFalse(
        usernames.contains(name2.getUsername()),
        "Returned users should not contain the second username");
  }

  @Test
  void activeInTest() {
    Name name1 = new Name();
    name1.setUsername("Cason");
    name1.setNickname("JAY");

    Name name2 = new Name();
    name2.setUsername("Alice");
    name2.setNickname("AL");

    Name name3 = new Name();
    name3.setUsername("Bob");
    name3.setNickname("BB");

    UserInfoWithJsonName user1 = new UserInfoWithJsonName();
    user1.setName(name1);

    UserInfoWithJsonName user2 = new UserInfoWithJsonName();
    user2.setName(name2);

    UserInfoWithJsonName user3 = new UserInfoWithJsonName();
    user3.setName(name3);

    Database.saveFewSql(Lists.of(user1, user2, user3));

    QueryCondition<UserInfoWithJsonName> wrapper =
        QueryCondition.query(UserInfoWithJsonName.class)
            .activeIn(UserInfoWithJsonName::getName, Lists.of(name1, name3));

    List<UserInfoWithJsonName> list = Database.list(wrapper);

    assertEquals(2, list.size(), "Query should return exactly two results");

    List<String> usernames =
        list.stream().map(user -> user.getName().getUsername()).collect(Collectors.toList());

    assertTrue(
        usernames.contains(name1.getUsername()),
        "Returned users should contain the first expected username");
    assertTrue(
        usernames.contains(name3.getUsername()),
        "Returned users should contain the third expected username");
    assertFalse(
        usernames.contains(name2.getUsername()),
        "Returned users should not contain the second username");
  }

  @Test
  void selectTest() {
    Name name = new Name();
    name.setUsername("VampireAchao");
    name.setNickname("阿超");

    UserInfoWithJsonName user = new UserInfoWithJsonName();
    user.setName(name);
    Database.saveFewSql(Lists.of(user));
    Database.updateFewSql(Lists.of(user));

    LambdaQueryWrapper<UserInfoWithJsonName> wrapper =
        QueryCondition.query(UserInfoWithJsonName.class)
            .select(UserInfoWithJsonName::getName)
            .eq(UserInfoWithJsonName::getName, name);
    val list = Database.list(wrapper);
    assertEquals(1, list.size(), "Query should return exactly one result");

    UserInfoWithJsonName dbUser = list.get(0);
    assertEquals(
        user.getName().getUsername(), dbUser.getName().getUsername(), "Username should match");
    assertEquals(
        user.getName().getNickname(), dbUser.getName().getNickname(), "Nickname should match");
  }

  public static class JsonFieldHandler extends AbstractJsonFieldHandler<Object> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object parse(String json, TableInfo tableInfo, TableFieldInfo fieldInfo) {
      Class<?> fieldType = fieldInfo.getField().getType();
      if (List.class.isAssignableFrom(fieldInfo.getField().getType())) {
        Class<?> clazzOpt =
            Steam.of(ReflectHelper.getGenericTypes(fieldInfo.getField().getGenericType()))
                .findFirst()
                .map(type -> (Class<?>) type)
                .orElseThrow(
                    () -> new IllegalArgumentException("List type must have a generic type"));
        return ((SerSupp<Object>)
                (() ->
                    objectMapper.readValue(
                        json,
                        objectMapper
                            .getTypeFactory()
                            .constructCollectionType(List.class, clazzOpt))))
            .get();
      } else if (Map.class.isAssignableFrom(fieldInfo.getField().getType())) {
        Class<?>[] genericClazz =
            Steam.of(ReflectHelper.getGenericTypes(fieldInfo.getField().getGenericType()))
                .map(type -> (Class<?>) type)
                .toArray(Class[]::new);
        return ((SerSupp<Object>)
                (() ->
                    objectMapper.readValue(
                        json,
                        objectMapper
                            .getTypeFactory()
                            .constructMapType(HashMap.class, genericClazz[0], genericClazz[1]))))
            .get();
      }
      return ((SerSupp<Object>) (() -> objectMapper.readValue(json, fieldType))).get();
    }

    @Override
    @SneakyThrows
    public String toJson(Object obj) {
      return objectMapper.writeValueAsString(obj);
    }
  }

  @Data
  @TableName(value = "user_info", autoResultMap = true)
  static class UserInfoWithJsonName {
    private Long id;

    @TableField(typeHandler = JsonFieldHandler.class)
    private Name name;

    @TableField(typeHandler = JsonFieldHandler.class)
    private List<Email> email;
  }

  @Data
  static class Name implements Serializable {
    private String username;
    private String nickname;
  }

  @Data
  static class Email implements Serializable {
    private String email;
    private String domain;
  }

  @Data
  @TableName(value = "user_info", autoResultMap = true)
  static class UserInfoWithMapName {
    private Long id;

    @TableField(typeHandler = JsonFieldHandler.class)
    private Map<String, Name> name;

    @TableField(typeHandler = JsonFieldHandler.class)
    private List<Email> email;
  }
}
