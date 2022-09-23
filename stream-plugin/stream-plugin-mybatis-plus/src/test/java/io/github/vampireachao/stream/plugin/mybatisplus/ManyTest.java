package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 查询多条测试
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/6/18 14:25
 */
@MybatisPlusTest
class ManyTest {


    @Test
    void testQuery() {
        Assertions.assertFalse(Many.query(1L, UserInfo::getId).isEmpty());
        Assertions.assertFalse(Many.query(1L, UserInfo::getId, UserInfo::getName).isEmpty());
        Assertions.assertFalse(Many.query(w -> w.le(UserInfo::getAge, 20), 1L, UserInfo::getId, UserInfo::getName).isEmpty());


        List<UserInfo> userInfoList = Many.of(UserInfo::getId).eq(1L).query();
        List<String> nameList = Many.of(UserInfo::getId).eq(1L).value(UserInfo::getName).query();
        List<String> leAgeNameList = Many.of(UserInfo::getId).eq(1L).value(UserInfo::getName)
                .condition(w -> w.le(UserInfo::getAge, 20))
                .query();
        Assertions.assertFalse(userInfoList.isEmpty());
        Assertions.assertFalse(nameList.isEmpty());
        Assertions.assertFalse(leAgeNameList.isEmpty());
    }


}
