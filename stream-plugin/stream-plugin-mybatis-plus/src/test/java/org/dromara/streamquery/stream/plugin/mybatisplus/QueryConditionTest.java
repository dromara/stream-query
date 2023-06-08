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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void test() {
        Name name = new Name();
        name.setUsername("VampireAchao");
        name.setNickname("阿超");

        UserInfoWithJsonName user = new UserInfoWithJsonName();
        user.setName(name);
        Database.saveFewSql(Lists.of(user));
        Database.updateFewSql(Lists.of(user));

        QueryCondition<UserInfoWithJsonName> wrapper =  QueryCondition.query(UserInfoWithJsonName.class).eq(UserInfoWithJsonName::getName, name);
        val list = Database.list(wrapper);

        // Assert that the query returned exactly one result
        assertEquals(1, list.size(), "Query should return exactly one result");

        // Assert that the returned user's name matches the expected name
        assertEquals(name, list.get(0).getName(), "Returned user's name should match the expected name");

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
