package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.core.lambda.LambdaInvokeException;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper.IMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserRole;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * QueryHelperTest
 *
 * @author VampireAchao Cizai_
 * @since 2022-05-03
 */
@MybatisPlusTest
class DatabaseTest {

    @Test
    void testExecute() {
        UserInfo entity = new UserInfo();
        entity.setName("cat");
        entity.setAge(20);
        entity.setEmail("achao1441470436@gmail.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setName("ruben");
        List<UserInfo> list = Arrays.asList(userInfo, entity);
        long effectRows = Database.execute(UserInfo.class, (IMapper<UserInfo> m) -> m.saveOneSql(list));
        Assertions.assertEquals(2, effectRows);
        Assertions.assertEquals(7, Db.count(UserInfo.class));

        Assertions.assertThrows(ClassCastException.class,
                () -> Database.execute(UserRole.class, (IMapper<UserRole> m) -> m.saveOneSql(Collections.emptyList())));
    }

    @Test
    void testSaveFewSql() {
        UserInfo entity = new UserInfo();
        entity.setName("cat");
        entity.setAge(20);
        entity.setEmail("achao1441470436@gmail.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setName("ruben");
        List<UserInfo> list = Arrays.asList(userInfo, entity);
        boolean isSuccess = Database.saveFewSql(list);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(7, Db.count(UserInfo.class));
    }

    @Test
    void testUpdateFewSql() {
        UserInfo sheep = new UserInfo();
        sheep.setId(1L);
        sheep.setName("bee bee I'm a sheep");

        UserInfo ruben = new UserInfo();
        ruben.setId(2L);
        ruben.setName("rabbit");
        Assertions.assertTrue(Database.updateFewSql(Arrays.asList(sheep, ruben)));
        Assertions.assertEquals("bee bee I'm a sheep", Db.getById(1L, UserInfo.class).getName());
        Assertions.assertEquals("rabbit", Db.getById(2L, UserInfo.class).getName());
    }

    @Test
    void testSaveOrUpdateFewSql() {
        UserInfo entity = new UserInfo();
        entity.setId(1L);
        entity.setName("cat");
        entity.setAge(20);
        entity.setEmail("achao1441470436@gmail.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setName("ruben");
        List<UserInfo> list = Arrays.asList(userInfo, entity);
        boolean isSuccess = Database.saveOrUpdateFewSql(list);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(6, Db.count(UserInfo.class));
    }

    @Test
    void testGetOne() {
        LambdaQueryWrapper<UserInfo> wrapper = Wrappers.lambdaQuery(UserInfo.class);
        Assertions.assertThrows(TooManyResultsException.class, () -> Database.getOne(wrapper));
        UserInfo one = Database.getOne(wrapper, false);
        Assertions.assertNotNull(one);
    }

    @Test
    void testGetMap() {
        Map<String, Object> map = Database.getMap(Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertNotNull(map);
    }

    @Test
    void testGetObj() {
        String name = Database.getObj(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, 1L), UserInfo::getName);
        Assertions.assertEquals("Jone", name);
    }

    @Test
    void testPropertyToColumn() {
        String columnByProperty = Database.propertyToColumn(UserInfo::getGmtDeleted);
        Assertions.assertEquals("gmt_deleted", columnByProperty);
    }

    @Test
    void testColumnToProperty() {
        String columnByProperty = Database.columnToProperty(UserInfo.class, "gmt_deleted");
        Assertions.assertEquals("gmtDeleted", columnByProperty);
    }

    @Test
    @SuppressWarnings("deprecation")
    void testOrdersPropertyToColumn() {
        Page<UserInfo> page = new Page<>();
        page.addOrder(OrderItem.desc("gmtDeleted"));
        Database.ordersPropertyToColumn(page, UserInfo.class);
        List<OrderItem> orders = page.getOrders();
        Assertions.assertEquals("gmt_deleted", orders.get(0).getColumn());

        // order by gmt_deleted desc
        Assertions.assertDoesNotThrow(() -> Db.page(page, UserInfo.class));

        // sql injection
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            try {
                Page<UserInfo> badPage = new Page<>();
                badPage.addOrder(OrderItem.asc("id;drop table user_info;"));
                Database.ordersPropertyToColumn(badPage, UserInfo.class);
            } catch (LambdaInvokeException e) {
                Throwable throwable = e.getCause().getCause();
                Assertions.assertEquals("order column { id;drop table user_info; } must not null or be sql injection",
                        throwable.getMessage());
                throw throwable;
            }
        });
    }
}
