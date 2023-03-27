package org.dromara.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.dromara.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 一对多测试
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/23 13:51
 */
@MybatisPlusTest
class OneToManyTest {
    @Test
    void testQuery() {
        Assertions.assertAll(() -> {
            List<Integer> userAges = Arrays.asList(18, 18, 28, 21, 24);
            Map<Integer, List<UserInfo>> ageUsersMap = OneToMany.of(UserInfo::getAge).in(userAges).parallel().query();
            Assertions.assertEquals(4, ageUsersMap.size());

            List<String> userNames = Arrays.asList("Jon", "Jack", "Tom", "Billie");
            Map<String, List<UserInfo>> nameUsersMap = OneToMany.of(UserInfo::getName).in(userNames).condition(w -> w.le(UserInfo::getAge, 21)).query();
            Assertions.assertEquals(2, nameUsersMap.size());

            Map<Integer, List<String>> userAgeNameMap = OneToMany.of(UserInfo::getAge).in(userAges).value(UserInfo::getName).query();
            Assertions.assertEquals(4, userAgeNameMap.size());

            userAgeNameMap = OneToMany.of(UserInfo::getAge).in(userAges).value(UserInfo::getName).condition(w -> w.le(UserInfo::getAge, 22)).query();
            Assertions.assertEquals(2, userAgeNameMap.size());

            ageUsersMap = OneToMany.of(UserInfo::getAge).eq(18).query();
            Assertions.assertEquals(1, ageUsersMap.size());

            userAgeNameMap = OneToMany.of(UserInfo::getAge).eq(18).value(UserInfo::getName).query();
            Assertions.assertEquals(1, userAgeNameMap.size());

            userAgeNameMap = OneToMany.of(UserInfo::getAge).eq(18).value(UserInfo::getName).condition(w -> w.le(UserInfo::getAge, 22)).query();
            Assertions.assertEquals(1, userAgeNameMap.size());

            Map<Integer, List<Boolean>> query = OneToMany.of(UserInfo::getAge).in(userAges).value(userInfo -> userInfo.getName() != null && userInfo.getName().contains("a")).condition(w -> w.select(UserInfo::getAge, UserInfo::getName)).query();
            Assertions.assertEquals(2, query.values().stream().flatMap(Collection::stream).filter(Boolean::booleanValue).count());
        });
    }
}
