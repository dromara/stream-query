package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 查询多条测试
 *
 * @author VampireAchao ZVerify
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
    void noKeyTest() {
        List<UserInfo> userInfoList = Many.of(UserInfo::getId).query();
        Assertions.assertFalse(userInfoList.isEmpty());
    }


}
