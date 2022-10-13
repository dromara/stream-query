package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.core.lambda.LambdaInvokeException;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper.IMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * QueryHelperTest
 *
 * @author VampireAchao ZVerify
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
        Assertions.assertEquals(7, Database.count(UserInfo.class));

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
        Assertions.assertEquals(7, Database.count(UserInfo.class));
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
        Assertions.assertEquals("bee bee I'm a sheep", Database.getById(1L, UserInfo.class).getName());
        Assertions.assertEquals("rabbit", Database.getById(2L, UserInfo.class).getName());
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
        Assertions.assertEquals(6, Database.count(UserInfo.class));
    }

    @Test
    void testSave() {
        UserInfo entity = new UserInfo();
        entity.setName("ruben");
        boolean isSuccess = Database.save(entity);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(6L, Database.count(UserInfo.class));
    }

    @Test
    void testSaveBatch() {
        UserInfo entity = new UserInfo();
        entity.setName("cat");
        entity.setAge(20);
        entity.setEmail("achao1441470436@gmail.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setName("ruben");
        List<UserInfo> list = Arrays.asList(userInfo, entity);
        boolean isSuccess = Database.saveBatch(list);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(7, Database.count(UserInfo.class));
    }

    @Test
    void testSaveOrUpdateBatch() {
        UserInfo entity = new UserInfo();
        entity.setId(1L);
        entity.setName("cat");
        entity.setAge(20);
        entity.setEmail("achao1441470436@gmail.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setName("ruben");
        List<UserInfo> list = Arrays.asList(userInfo, entity);
        boolean isSuccess = Database.saveOrUpdateBatch(list);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(6, Database.count(UserInfo.class));
    }

    @Test
    void testRemoveById() {
        UserInfo entity = new UserInfo();
        entity.setId(1L);
        boolean isSuccess = Database.removeById(entity);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(4, Database.count(UserInfo.class));
        isSuccess = Database.removeById(2L, UserInfo.class);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(3, Database.count(UserInfo.class));
    }

    @Test
    void testUpdateById() {
        UserInfo entity = new UserInfo();
        entity.setId(1L);
        entity.setName("bee bee I'm a sheep");
        boolean isSuccess = Database.updateById(entity);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("bee bee I'm a sheep", Database.getById(1L, UserInfo.class).getName());
    }

    @Test
    void testUpdate() {
        boolean isSuccess = Database.update(Wrappers.lambdaUpdate(UserInfo.class).eq(UserInfo::getId, 1L).set(UserInfo::getName, "be better"));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("be better", Database.getById(1L, UserInfo.class).getName());

        UserInfo entity = new UserInfo();
        entity.setId(1L);
        entity.setName("bee bee I'm a sheep");
        isSuccess = Database.update(entity, Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, 1L));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("bee bee I'm a sheep", Database.getById(1L, UserInfo.class).getName());
    }

    @Test
    void testUpdateBatchById() {
        UserInfo sheep = new UserInfo();
        sheep.setId(1L);
        sheep.setName("bee bee I'm a sheep");

        UserInfo ruben = new UserInfo();
        ruben.setId(2L);
        ruben.setName("rabbit");
        boolean isSuccess = Database.updateBatchById(Arrays.asList(sheep, ruben));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("bee bee I'm a sheep", Database.getById(1L, UserInfo.class).getName());
        Assertions.assertEquals("rabbit", Database.getById(2L, UserInfo.class).getName());
    }

    @Test
    void testUpdateForceById() {
        UserInfo sheep = new UserInfo();
        sheep.setId(1L);
        sheep.setName("bee bee I'm a sheep");

        boolean isSuccess = Database.updateForceById(sheep, UserInfo::getName, UserInfo::getAge);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("bee bee I'm a sheep", Database.getById(1L, UserInfo.class).getName());
        Assertions.assertNull(Database.getById(1L, UserInfo.class).getAge());
    }

    @Test
    void testRemove() {
        boolean isSuccess = Database.remove(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, 1L));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(4, Database.count(UserInfo.class));
    }

    @Test
    void testRemoveByIds() {
        boolean isSuccess = Database.removeByIds(Arrays.asList(1L, 2L), UserInfo.class);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(3, Database.count(UserInfo.class));
    }

    @Test
    void testRemoveByMap() {
        boolean isSuccess = Database.removeByMap(Collections.singletonMap("id", 1L), UserInfo.class);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(4, Database.count(UserInfo.class));
    }

    @Test
    void testSaveOrUpdate() {
        UserInfo entity = new UserInfo();
        entity.setId(null);
        entity.setName("bee bee I'm a sheep");
        boolean isSuccess = Database.saveOrUpdate(entity);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("bee bee I'm a sheep", Database.getById(entity.getId(), UserInfo.class).getName());

        entity.setName("be better");
        isSuccess = Database.saveOrUpdate(entity, Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, entity.getId()));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("be better", Database.getById(entity.getId(), UserInfo.class).getName());
    }

    @Test
    void testGetOne() {
        LambdaQueryWrapper<UserInfo> wrapper = Wrappers.lambdaQuery(UserInfo.class);
        Assertions.assertThrows(MybatisPlusException.class, () -> Database.getOne(wrapper));
        UserInfo one = Database.getOne(wrapper, false);
        Assertions.assertNotNull(one);
    }

    @Test
    void testListByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        List<UserInfo> list = Database.listByMap(map, UserInfo.class);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("Jone", list.get(0).getName());
    }

    @Test
    void testByIds() {
        List<UserInfo> list = Database.listByIds(Arrays.asList(1L, 2L), UserInfo.class);
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void testGetMap() {
        Map<String, Object> map = Database.getMap(Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertNotNull(map);
    }

    @Test
    void testList() {
        List<UserInfo> list = Database.list(Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertEquals(5, list.size());

        list = Database.list(UserInfo.class);
        Assertions.assertEquals(5, list.size());
    }

    @Test
    void testListMaps() {
        List<Map<String, Object>> list = Database.listMaps(Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertEquals(5, list.size());

        list = Database.listMaps(UserInfo.class);
        Assertions.assertEquals(5, list.size());
    }

    @Test
    void testListObjs() {
        List<UserInfo> list = Database.listObjs(UserInfo.class);
        Assertions.assertEquals(5, list.size());

        List<Long> objectList = Database.listObjs(Wrappers.lambdaQuery(UserInfo.class), UserInfo::getId);
        Assertions.assertEquals(5, objectList.size());

        List<String> names = Database.listObjs(UserInfo.class, UserInfo::getName);
        Assertions.assertArrayEquals(new String[]{"Jone", "Jack", "Tom", "Sandy", "Billie"}, names.toArray());
    }

    @Test
    void testPageMaps() {
        Page<Map<String, Object>> page = Database.pageMaps(new Page<>(1, 1), UserInfo.class);
        Assertions.assertEquals(5, page.getTotal());

        page = Database.pageMaps(new Page<>(1, 1), Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertEquals(1, page.getRecords().size());
    }

    @Test
    void testPage() {
        IPage<UserInfo> page = Database.page(new Page<>(1, 1), UserInfo.class);
        Assertions.assertEquals(5, page.getTotal());

        page = Database.page(new Page<>(1, 1), Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertEquals(1, page.getRecords().size());
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
        Assertions.assertDoesNotThrow(() -> Database.page(page, UserInfo.class));

        // sql injection
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            try {
                Page<UserInfo> badPage = new Page<>();
                badPage.addOrder(OrderItem.asc("id;drop table user_info;"));
                Database.ordersPropertyToColumn(badPage, UserInfo.class);
            } catch (LambdaInvokeException e) {
                Throwable throwable = e.getRealException();
                Assertions.assertEquals("order column { id;drop table user_info; } must not null or be sql injection", throwable.getMessage());
                throw throwable;
            }
        });
    }
}
