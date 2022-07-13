package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * QueryHelperTest
 *
 * @author VampireAchao
 * @since 2022-05-03
 */
@MybatisPlusTest
class QueryHelperTest {

    @Test
    void testSave() {
        UserInfo entity = new UserInfo();
        entity.setName("ruben");
        boolean isSuccess = QueryHelper.save(entity);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(6L, QueryHelper.count(UserInfo.class));
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
        boolean isSuccess = QueryHelper.saveBatch(list);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(7, QueryHelper.count(UserInfo.class));
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
        boolean isSuccess = QueryHelper.saveOrUpdateBatch(list);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(6, QueryHelper.count(UserInfo.class));
    }

    @Test
    void testRemoveById() {
        UserInfo entity = new UserInfo();
        entity.setId(1L);
        boolean isSuccess = QueryHelper.removeById(entity);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(4, QueryHelper.count(UserInfo.class));
        isSuccess = QueryHelper.removeById(2L, UserInfo.class);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(3, QueryHelper.count(UserInfo.class));
    }

    @Test
    void testUpdateById() {
        UserInfo entity = new UserInfo();
        entity.setId(1L);
        entity.setName("bee bee I'm a sheep");
        boolean isSuccess = QueryHelper.updateById(entity);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("bee bee I'm a sheep", QueryHelper.getById(1L, UserInfo.class).getName());
    }

    @Test
    void testUpdate() {
        boolean isSuccess = QueryHelper.update(Wrappers.lambdaUpdate(UserInfo.class).eq(UserInfo::getId, 1L).set(UserInfo::getName, "be better"));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("be better", QueryHelper.getById(1L, UserInfo.class).getName());

        UserInfo entity = new UserInfo();
        entity.setId(1L);
        entity.setName("bee bee I'm a sheep");
        isSuccess = QueryHelper.update(entity, Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, 1L));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("bee bee I'm a sheep", QueryHelper.getById(1L, UserInfo.class).getName());
    }

    @Test
    void testUpdateBatchById() {
        UserInfo sheep = new UserInfo();
        sheep.setId(1L);
        sheep.setName("bee bee I'm a sheep");

        UserInfo ruben = new UserInfo();
        ruben.setId(2L);
        ruben.setName("rabbit");
        boolean isSuccess = QueryHelper.updateBatchById(Arrays.asList(sheep, ruben));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("bee bee I'm a sheep", QueryHelper.getById(1L, UserInfo.class).getName());
        Assertions.assertEquals("rabbit", QueryHelper.getById(2L, UserInfo.class).getName());
    }

    @Test
    void testRemove() {
        boolean isSuccess = QueryHelper.remove(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, 1L));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(4, QueryHelper.count(UserInfo.class));
    }

    @Test
    void testRemoveByIds() {
        boolean isSuccess = QueryHelper.removeByIds(Arrays.asList(1L, 2L), UserInfo.class);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(3, QueryHelper.count(UserInfo.class));
    }

    @Test
    void testRemoveByMap() {
        boolean isSuccess = QueryHelper.removeByMap(Collections.singletonMap("id", 1L), UserInfo.class);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals(4, QueryHelper.count(UserInfo.class));
    }

    @Test
    void testSaveOrUpdate() {
        UserInfo entity = new UserInfo();
        entity.setId(null);
        entity.setName("bee bee I'm a sheep");
        boolean isSuccess = QueryHelper.saveOrUpdate(entity);
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("bee bee I'm a sheep", QueryHelper.getById(entity.getId(), UserInfo.class).getName());

        entity.setName("be better");
        isSuccess = QueryHelper.saveOrUpdate(entity, Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, entity.getId()));
        Assertions.assertTrue(isSuccess);
        Assertions.assertEquals("be better", QueryHelper.getById(entity.getId(), UserInfo.class).getName());
    }

    @Test
    void testGetOne() {
        LambdaQueryWrapper<UserInfo> wrapper = Wrappers.lambdaQuery(UserInfo.class);
        Assertions.assertThrows(MybatisPlusException.class, () -> QueryHelper.getOne(wrapper));
        UserInfo one = QueryHelper.getOne(wrapper, false);
        Assertions.assertNotNull(one);
    }

    @Test
    void testListByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        List<UserInfo> list = QueryHelper.listByMap(map, UserInfo.class);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("Jone", list.get(0).getName());
    }

    @Test
    void testByIds() {
        List<UserInfo> list = QueryHelper.listByIds(Arrays.asList(1L, 2L), UserInfo.class);
        Assertions.assertEquals(2, list.size());
    }

    @Test
    void testGetMap() {
        Map<String, Object> map = QueryHelper.getMap(Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertNotNull(map);
    }

    @Test
    void testList() {
        List<UserInfo> list = QueryHelper.list(Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertEquals(5, list.size());

        list = QueryHelper.list(UserInfo.class);
        Assertions.assertEquals(5, list.size());
    }

    @Test
    void testListMaps() {
        List<Map<String, Object>> list = QueryHelper.listMaps(Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertEquals(5, list.size());

        list = QueryHelper.listMaps(UserInfo.class);
        Assertions.assertEquals(5, list.size());
    }

    @Test
    void testListObjs() {
        List<UserInfo> list = QueryHelper.listObjs(UserInfo.class);
        Assertions.assertEquals(5, list.size());

        List<Long> objectList = QueryHelper.listObjs(Wrappers.lambdaQuery(UserInfo.class), UserInfo::getId);
        Assertions.assertEquals(5, objectList.size());

        List<String> names = QueryHelper.listObjs(UserInfo.class, UserInfo::getName);
        Assertions.assertArrayEquals(new String[]{"Jone", "Jack", "Tom", "Sandy", "Billie"}, names.toArray());
    }

    @Test
    void testPageMaps() {
        Page<Map<String, Object>> page = QueryHelper.pageMaps(new Page<>(1, 1), UserInfo.class);
        Assertions.assertEquals(5, page.getTotal());

        page = QueryHelper.pageMaps(new Page<>(1, 1), Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertEquals(1, page.getRecords().size());
    }

    @Test
    void testPage() {
        IPage<UserInfo> page = QueryHelper.page(new Page<>(1, 1), UserInfo.class);
        Assertions.assertEquals(5, page.getTotal());

        page = QueryHelper.page(new Page<>(1, 1), Wrappers.lambdaQuery(UserInfo.class));
        Assertions.assertEquals(1, page.getRecords().size());
    }

    @Test
    void testGetObj() {
        String name = QueryHelper.getObj(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, 1L), UserInfo::getName);
        Assertions.assertEquals("Jone", name);
    }
}
