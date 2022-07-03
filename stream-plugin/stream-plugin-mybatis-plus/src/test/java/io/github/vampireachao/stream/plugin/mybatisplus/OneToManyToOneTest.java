package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import io.github.vampireachao.stream.core.stream.StreamHelper;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.RoleInfo;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OneToManyToOneTest
 *
 * @author VampireAchao
 * @since 2022/5/23
 */
@MybatisPlusTest
class OneToManyToOneTest {

    @Test
    void testQuery() {
        Assertions.assertAll(() -> {
            List<UserInfo> userInfos = SimpleQuery.selectList(UserInfo.class, Wrappers.lambdaQuery());
            Assertions.assertEquals(5, userInfos.size());
            List<UserRole> userRoles = SimpleQuery.selectList(UserRole.class, Wrappers.lambdaQuery());
            Assertions.assertEquals(10, userRoles.size());
            List<RoleInfo> roleInfos = SimpleQuery.selectList(RoleInfo.class, Wrappers.lambdaQuery());
            Assertions.assertEquals(3, roleInfos.size());

            Set<Long> userIds = StreamHelper.mapToSet(userInfos, UserInfo::getId);

            Set<Long> roleIds = new HashSet<>();
            Map<Long, List<Long>> userIdRoleIdsMap = OneToMany.query(userIds, UserRole::getUserId, UserRole::getRoleId, (userRole, index) -> roleIds.add(userRole.getRoleId()));
            Map<Long, RoleInfo> idRoleMap = OneToOne.query(roleIds, RoleInfo::getId);
            Map<Long, List<RoleInfo>> userIdRoleInfosMap = userIdRoleIdsMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().map(idRoleMap::get).collect(Collectors.toList())));
            Assertions.assertEquals(5, userIdRoleInfosMap.size());

            userIdRoleInfosMap = OneToManyToOne.query(userIds, UserRole::getUserId, UserRole::getRoleId, RoleInfo::getId);
            Assertions.assertEquals(5, userIdRoleInfosMap.size());

        });
    }

}
