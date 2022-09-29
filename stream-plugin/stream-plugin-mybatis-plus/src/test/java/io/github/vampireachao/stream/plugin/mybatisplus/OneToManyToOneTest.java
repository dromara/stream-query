package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.core.collector.Collective;
import io.github.vampireachao.stream.core.stream.Steam;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.RoleInfo;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserRole;
import org.apache.ibatis.util.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OneToManyToOneTest
 *
 * @author VampireAchao ZVerify
 * @since 2022/5/23
 */
@MybatisPlusTest
class OneToManyToOneTest {

    @Test
    void testQuery() {
        List<UserInfo> userInfos = SimpleQuery.selectList(UserInfo.class, Wrappers.lambdaQuery());
        Set<Long> userIds = Steam.of(userInfos).map(UserInfo::getId).toSet();

        List<String> roleIds = new ArrayList<>();
        Map<Long, List<String>> userIdRoleIds = OneToMany.of(UserRole::getUserId).in(userIds).value(UserRole::getRoleId).peek(e -> roleIds.add(e.getRoleId())).query();
        Map<String, RoleInfo> idRoleMap = OneToOne.of(RoleInfo::getId).in(roleIds).query();
        Map<Long, List<RoleInfo>> userIdRolesMap = Steam.of(userIdRoleIds.entrySet()).map(e -> MapUtil.entry(e.getKey(), Steam.of(e.getValue()).map(idRoleMap::get).nonNull().toList())).collect(Collective.entryToMap());
        Assertions.assertEquals(5, userIdRolesMap.size());

        /*Map<Long, List<RoleInfo>> userIdRoleInfosMap = OneToManyToOne.of(UserRole::getUserId).in(userIds).value(UserRole::getRoleId)
                .attachKey(RoleInfo::getId).attachPeek(SerCons.nothing()).query();
        Assertions.assertEquals(5, userIdRoleInfosMap.size());

        Map<String, List<String>> roleIdNamesMap = OneToManyToOne.of(UserRole::getUserId).in(userIds).value(UserRole::getRoleId)
                .attachKey(RoleInfo::getId).attachValue(RoleInfo::getRoleName).groupBy(UserRole::getRoleId).query(Collective.toList());
        Assertions.assertFalse(roleIdNamesMap.isEmpty());*/

        /*OneToMany.of(UserRole::getUserId).in(userIds).value(UserRole::getRoleId)
                .join((Map<Long, List<String>> userIdRoleIdsMap) -> OneToOne.of(RoleInfo::getId).in(Steam.of(userIdRoleIdsMap.values()).flat(SerFunc.identity()).toList()),
                        (Map<Long, List<String>> userIdRoleIdsMap, Map<String, RoleInfo> idRoleMap) ->
                                Steam.of(userIdRoleIdsMap.entrySet()).map(e -> MapUtil.entry(e.getKey(), Steam.of(e.getValue()).map(idRoleMap::get).nonNull().toList())).collect(Collective.entryToMap())).query();*/

    }

}
