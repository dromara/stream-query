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
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.core.lambda.function.SerSupp;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.handler.AbstractJsonFieldHandler;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.mapper.IGenerateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cason
 * @date 2023-06-08 21:32
 */
@MybatisPlusTest
public class QueryConditionTest {
    @BeforeEach
    void init(@Autowired SqlSessionFactory sqlSessionFactory) {
        Database.buildMapper(sqlSessionFactory.getConfiguration(), UserInfoWithJsonName.class);
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

        QueryCondition<UserInfoWithJsonName> wrapper =  QueryCondition.query(UserInfoWithJsonName.class)
                .eq(UserInfoWithJsonName::getName, name);
        val list = Database.list(wrapper);

        assertEquals(1, list.size(), "Query should return exactly one result");
        assertEquals(name, list.get(0).getName(), "Returned user's name should match the expected name");

    }

    @Test
    void activeEqTest() {
        Name name = new Name();
        name.setUsername("Cason");
        name.setNickname("JAY");

        UserInfoWithJsonName user = new UserInfoWithJsonName();
        user.setName(name);

        Database.saveFewSql(Lists.of(user));

        QueryCondition<UserInfoWithJsonName> wrapper =  QueryCondition.query(UserInfoWithJsonName.class)
                .activeEq(UserInfoWithJsonName::getName, name);

        val list = Database.list(wrapper);

        assertEquals(1, list.size(), "Query should return exactly one result");
        assertEquals(name.getUsername(), list.get(0).getName().getUsername(), "Returned user's username should match the expected username");
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

        QueryCondition<UserInfoWithJsonName> wrapper =  QueryCondition.query(UserInfoWithJsonName.class)
                .in(UserInfoWithJsonName::getName, Lists.of(name1, name3));

        val list = Database.list(wrapper);

        assertEquals(2, list.size(), "Query should return exactly two results");

        List<String> usernames = list.stream().map(user -> user.getName().getUsername()).collect(Collectors.toList());

        assertTrue(usernames.contains(name1.getUsername()), "Returned users should contain the first expected username");
        assertTrue(usernames.contains(name3.getUsername()), "Returned users should contain the third expected username");
        assertFalse(usernames.contains(name2.getUsername()), "Returned users should not contain the second username");
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

        QueryCondition<UserInfoWithJsonName> wrapper =  QueryCondition.query(UserInfoWithJsonName.class)
                .activeIn(UserInfoWithJsonName::getName, Lists.of(name1, name3));

        val list = Database.list(wrapper);

        assertEquals(2, list.size(), "Query should return exactly two results");

        List<String> usernames = list.stream().map(user -> user.getName().getUsername()).collect(Collectors.toList());

        assertTrue(usernames.contains(name1.getUsername()), "Returned users should contain the first expected username");
        assertTrue(usernames.contains(name3.getUsername()), "Returned users should contain the third expected username");
        assertFalse(usernames.contains(name2.getUsername()), "Returned users should not contain the second username");
    }


    public static class JsonFieldHandler extends AbstractJsonFieldHandler {

        ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public Object parse(String json, TableInfo tableInfo, TableFieldInfo fieldInfo) {
            Class<?> fieldType = fieldInfo.getField().getType();
            return ((SerSupp<Object>) (() -> objectMapper.readValue(json, fieldType))).get();
        }

        @Override
        @SneakyThrows
        public String toJson(Object obj, TableInfo tableInfo, TableFieldInfo fieldInfo) {
            return objectMapper.writeValueAsString(obj);
        }
    }

    @Data
    @TableName(value = "user_info", autoResultMap = true)
    static class UserInfoWithJsonName implements IGenerateMapper {
        private Long id;

        @TableField(typeHandler = JsonFieldHandler.class)
        private Name name;
    }

    @Data
    static class Name  implements Serializable, Comparable<Name> {
        private String username;
        private String nickname;

        @Override
        public int compareTo(Name o) {

            return this.username.compareTo(o.username);
        }
    }
}
