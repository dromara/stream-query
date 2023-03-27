package org.dromara.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.dromara.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 查询单条测试
 *
 * @author VampireAchao Cizai_
 * @since 2022/6/18 14:25
 */
@MybatisPlusTest
class OneTest {


    @Test
    void testQuery() {
        UserInfo userInfo = One.of(UserInfo::getId).eq(1L).query();
        String name = One.of(UserInfo::getId).eq(1L).value(UserInfo::getName).query();
        String leAgeName = One.of(UserInfo::getId).eq(1L).value(UserInfo::getName)
                .condition(w -> w.le(UserInfo::getAge, 20))
                .query();
        Assertions.assertNotNull(userInfo);
        Assertions.assertNotNull(name);
        Assertions.assertNotNull(leAgeName);
        Assertions.assertThrows(TooManyResultsException.class, () -> One.of(UserInfo::getName).like("a").query());
    }

}
