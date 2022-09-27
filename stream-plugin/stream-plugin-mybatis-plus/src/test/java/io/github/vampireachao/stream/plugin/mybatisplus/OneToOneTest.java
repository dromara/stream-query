package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 一对一测试
 *
 * @author VampireAchao ZVerify
 */
@MybatisPlusTest
class OneToOneTest {

    @Test
    void testQuery() {
        List<Long> userIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        Map<Long, UserInfo> idUserMap = OneToOne.of(UserInfo::getId).in(userIds).query();
        Assertions.assertEquals(5, idUserMap.size());

        Map<Long, String> userIdNameMap = OneToOne.of(UserInfo::getId).in(userIds).value(UserInfo::getName).query();
        Assertions.assertEquals(5, userIdNameMap.size());

        userIdNameMap = OneToOne.of(UserInfo::getId).in(userIds).value(UserInfo::getName).condition(w -> w.le(UserInfo::getAge, 22)).query();
        Assertions.assertEquals(3, userIdNameMap.size());


        idUserMap = OneToOne.of(UserInfo::getId).eq(1L).query();
        Assertions.assertEquals(1, idUserMap.size());

        userIdNameMap = OneToOne.of(UserInfo::getId).eq(1L).value(UserInfo::getName).query();
        Assertions.assertEquals(1, userIdNameMap.size());

        userIdNameMap = OneToOne.of(UserInfo::getId).eq(1L).value(UserInfo::getName).condition(w -> w.le(UserInfo::getAge, 22)).query();
        Assertions.assertEquals(1, userIdNameMap.size());

        Map<Long, Boolean> query = OneToOne.of(UserInfo::getId).in(userIds).condition(w -> w.select(UserInfo::getId, UserInfo::getName)).value(userInfo -> userInfo.getName() != null && userInfo.getName().contains("a")).query();
        Assertions.assertEquals(2, query.values().stream().filter(Boolean::booleanValue).count());
    }

}
