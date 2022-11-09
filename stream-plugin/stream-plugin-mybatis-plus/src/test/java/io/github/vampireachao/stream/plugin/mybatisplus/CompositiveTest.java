package io.github.vampireachao.stream.plugin.mybatisplus;

/**
 * @author VampireAchao
 * @since 2022/11/9 17:47
 */
class CompositiveTest {

    /*@Test
    void test() {
        List<UserInfo> userInfos = Database.list(UserInfo.class);
        Compositive.of(userInfos).map(UserInfo::getId)
                .beforeAsync(SerCons.nothing())
                .afterAsync(SerCons.nothing())
                .supplyAsync(OneToMany.of(UserRole::getUserId).value(UserRole::getRoleId)
                                .and(OneToOne.of(RoleInfo::getId)),
                        UserInfo::setRoleInfo)
                .supplyAsync(OneToMany.of(UserDepartment::getUserId).value(UserDepartment::getDepartmentId)
                                .and(OneToOne.of(DepartmentInfo::getId)),
                        UserInfo::setDepartmentInfo)
                .query();
    }*/
}
