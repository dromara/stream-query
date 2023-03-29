package org.dromara.streamquery.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

/**
 * 查询多条测试
 *
 * @author VampireAchao Cizai_
 * @since 2022/6/18 14:25
 */
@MybatisPlusTest
class ManyTest {


    @Test
    void testQuery() {

        List<UserInfo> userInfoList = Many.of(UserInfo::getId).eq(1L).parallel().query();
        List<String> nameList = Many.of(UserInfo::getId).eq(1L).value(UserInfo::getName).sequential().query();
        List<String> leAgeNameList = Many.of(UserInfo::getId).eq(1L).value(UserInfo::getName)
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
        UserInfo[] eqNullUserArray = Many.of(UserInfo::getId).eq(null).query(s -> s.toArray(UserInfo[]::new));
        Assertions.assertArrayEquals(new UserInfo[0], eqNullUserArray);

        List<String> inEmptyNameList = Many.of(UserInfo::getId).in(Collections.emptyList()).value(UserInfo::getName).query();
        Assertions.assertTrue(inEmptyNameList.isEmpty());

        List<UserInfo> conditionNullUserList = Many.of(UserInfo::getId).condition(w -> null).query();
        Assertions.assertTrue(conditionNullUserList.isEmpty());
    }


}
