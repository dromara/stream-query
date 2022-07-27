package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 一对多测试
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt;
 * @since 2022/5/23 13:51
 */
@MybatisPlusTest
class OneToManyTest {
    @Test
    void testQuery() {
        Assertions.assertAll(() -> {
            List<Integer> userAges = Arrays.asList(18, 18, 28, 21, 24);
            Map<Integer, List<UserInfo>> ageUsersMap = OneToMany.query(userAges, UserInfo::getAge);
            Assertions.assertEquals(4, ageUsersMap.size());

            List<String> userNames = Arrays.asList("Jone","Jack","Tom","Billie");
            Map<String, List<UserInfo>> nameUsersMap = OneToMany.query(w -> w.le(UserInfo::getAge, 21), userNames, UserInfo::getName);
            Assertions.assertEquals(2, nameUsersMap.size());

            Map<Integer, List<String>> userAgeNameMap = OneToMany.query(userAges, UserInfo::getAge, UserInfo::getName);
            Assertions.assertEquals(4, userAgeNameMap.size());

            userAgeNameMap = OneToMany.query(w -> w.le(UserInfo::getAge, 22), userAges, UserInfo::getAge, UserInfo::getName);
            Assertions.assertEquals(2, userAgeNameMap.size());


            ageUsersMap = OneToMany.query(18, UserInfo::getAge);
            Assertions.assertEquals(1, ageUsersMap.size());

            userAgeNameMap = OneToMany.query(18, UserInfo::getAge, UserInfo::getName);
            Assertions.assertEquals(1, userAgeNameMap.size());

            userAgeNameMap = OneToMany.query(w -> w.le(UserInfo::getAge, 22), 18, UserInfo::getAge, UserInfo::getName);
            Assertions.assertEquals(1, userAgeNameMap.size());

            Map<Integer, List<Boolean>> query = OneToMany.query(w -> w.select(UserInfo::getAge, UserInfo::getName), userAges, UserInfo::getAge, userInfo -> userInfo.getName() != null && userInfo.getName().contains("a"));
            Assertions.assertEquals(2, query.values().stream().flatMap(Collection::stream).filter(Boolean::booleanValue).count());
        });
    }
}
