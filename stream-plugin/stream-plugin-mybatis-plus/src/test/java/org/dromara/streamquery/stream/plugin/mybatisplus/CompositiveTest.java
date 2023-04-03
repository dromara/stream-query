/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.streamquery.stream.plugin.mybatisplus;

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
