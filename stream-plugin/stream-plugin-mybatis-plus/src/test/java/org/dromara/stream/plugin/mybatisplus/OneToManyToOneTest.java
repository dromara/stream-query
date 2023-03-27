package org.dromara.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.apache.ibatis.util.MapUtil;
import org.dromara.stream.core.lambda.function.SerCons;
import org.dromara.stream.core.stream.Steam;
import org.dromara.stream.core.stream.collector.Collective;
import org.dromara.stream.plugin.mybatisplus.pojo.po.RoleInfo;
import org.dromara.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.dromara.stream.plugin.mybatisplus.pojo.po.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OneToManyToOneTest
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/23
 */
@MybatisPlusTest
class OneToManyToOneTest {

    @Test
    void testQuery() {
        List<UserInfo> userInfos = Database.list(Wrappers.lambdaQuery(UserInfo.class));
        Set<Long> userIds = Steam.of(userInfos).map(UserInfo::getId).toSet();

        List<String> roleIds = new ArrayList<>();
        Map<Long, List<String>> userIdRoleIds = OneToMany.of(UserRole::getUserId).in(userIds).value(UserRole::getRoleId).peek(e -> roleIds.add(e.getRoleId())).query();
        Map<String, RoleInfo> idRoleMap = OneToOne.of(RoleInfo::getId).in(roleIds).query();
        Map<Long, List<RoleInfo>> userIdRolesMap = Steam.of(userIdRoleIds.entrySet()).map(e -> MapUtil.entry(e.getKey(), Steam.of(e.getValue()).map(idRoleMap::get).nonNull().toList())).collect(Collective.entryToMap());
        Assertions.assertEquals(5, userIdRolesMap.size());

        Map<Long, List<RoleInfo>> userIdRoleInfosMap = OneToManyToOne.of(UserRole::getUserId).in(userIds).value(UserRole::getRoleId)
                .attachKey(RoleInfo::getId).attachPeek(SerCons.nothing()).query();
        Assertions.assertEquals(userIdRolesMap, userIdRoleInfosMap);
    }

    @Test
    void testPart() {
        Map<Long, List<UserRole>> userIdUserRolesMap = OneToManyToOne.of(UserRole::getUserId).query();
        Assertions.assertFalse(userIdUserRolesMap.isEmpty());

        Map<Long, List<String>> userIdRoleIdsMap = OneToManyToOne.of(UserRole::getUserId).value(UserRole::getRoleId).query();
        Assertions.assertFalse(userIdRoleIdsMap.isEmpty());

        Map<Long, List<String>> userIdEq1RoleIdsMap = OneToManyToOne.of(UserRole::getUserId).value(UserRole::getRoleId).condition(w -> w.eq(UserRole::getId, 1L)).query();
        Assertions.assertFalse(userIdEq1RoleIdsMap.isEmpty());

        Map<Long, List<RoleInfo>> userIdRolesMap = OneToManyToOne.of(UserRole::getUserId).value(UserRole::getRoleId).attachKey(RoleInfo::getId).query();
        Assertions.assertFalse(userIdRolesMap.isEmpty());

        Map<Long, List<String>> userIdRoleNamesMap = OneToManyToOne.of(UserRole::getUserId).value(UserRole::getRoleId).attachKey(RoleInfo::getId).attachValue(RoleInfo::getRoleName).query();
        Assertions.assertFalse(userIdRoleNamesMap.isEmpty());

        Map<Long, List<String>> onlyMiddle = OneToManyToOne.of(UserRole::getUserId).value(UserRole::getRoleId).attachKey(RoleInfo::getId).attachValue(RoleInfo::getRoleName).attachCondition(w -> null).query();
        Assertions.assertFalse(onlyMiddle.isEmpty());
    }

    @Test
    void testNoQuery() {
        Assertions.assertTrue(OneToManyToOne.of(UserRole::getUserId).eq(null).query().isEmpty());
        Assertions.assertTrue(OneToManyToOne.of(UserRole::getUserId).condition(w -> null).query().isEmpty());
        Assertions.assertTrue(OneToManyToOne.of(UserRole::getUserId).value(UserRole::getRoleId).in(null).query().isEmpty());
        Assertions.assertTrue(OneToManyToOne.of(UserRole::getUserId).value(UserRole::getRoleId).in(null).attachKey(RoleInfo::getId).query().isEmpty());
        Assertions.assertTrue(OneToManyToOne.of(UserRole::getUserId).value(UserRole::getRoleId).in(null).attachKey(RoleInfo::getId).attachValue(RoleInfo::getRoleName).query().isEmpty());
    }

}