package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.plugin.mybatisplus.mapper.UserInfoMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/7/26 11:11
 */
@MybatisPlusTest
class IMapperTest {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Test
    void testInsertOneSql() {
        UserInfo entity = new UserInfo();
        entity.setName("cat");
        entity.setAge(20);
        entity.setEmail("achao1441470436@gmail.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setName("ruben");
        List<UserInfo> list = Arrays.asList(userInfo, entity);
        long affectRows = userInfoMapper.insertOneSql(list);
        Assertions.assertEquals(2L, affectRows);
        Assertions.assertEquals(7, Database.count(UserInfo.class));
    }

    @Test
    void testInsertFewSql() {
        UserInfo entity = new UserInfo();
        entity.setName("cat");
        entity.setAge(20);
        entity.setEmail("achao1441470436@gmail.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setName("ruben");
        List<UserInfo> list = Arrays.asList(userInfo, entity);
        long affectRows = userInfoMapper.insertFewSql(list);
        Assertions.assertEquals(2L, affectRows);
        Assertions.assertEquals(7, Database.count(UserInfo.class));
    }

    @Test
    void testUpdateOneSql() {
        UserInfo sheep = new UserInfo();
        sheep.setId(1L);
        sheep.setName("bee bee I'm a sheep");

        UserInfo ruben = new UserInfo();
        ruben.setId(2L);
        ruben.setName("rabbit");
        long affectRows = userInfoMapper.updateOneSql(Arrays.asList(sheep, ruben));
        Assertions.assertEquals(2L, affectRows);
        Assertions.assertEquals("bee bee I'm a sheep", Database.getById(1L, UserInfo.class).getName());
        Assertions.assertEquals("rabbit", Database.getById(2L, UserInfo.class).getName());
    }

}
