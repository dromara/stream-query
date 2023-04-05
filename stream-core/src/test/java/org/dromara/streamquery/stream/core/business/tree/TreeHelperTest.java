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
package org.dromara.streamquery.stream.core.business.tree;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * TreeHelperTest
 *
 * @author VampireAchao
 * @since 2022/11/26
 */
class TreeHelperTest {

    List<Student> originStudentList;
    List<Student> originStudentTree;
    TreeHelper<Student, Long> studentTreeHelper;
    List<Student> treeByLevelOriginStudentTree;
    List<Student> treeFromRootToLevelOriginStudentTree;

    @BeforeEach
    void setUp() {
        originStudentList = Steam
                .of(
                        Student.builder().id(1L).name("dromara").matchParent(true).build(),
                        Student.builder().id(2L).name("baomidou").matchParent(true).build(),
                        Student.builder().id(3L).name("hutool").parentId(1L).build(),
                        Student.builder().id(4L).name("sa-token").parentId(1L).build(),
                        Student.builder().id(5L).name("mybatis-plus").parentId(2L).build(),
                        Student.builder().id(6L).name("looly").parentId(3L).build(),
                        Student.builder().id(7L).name("click33").parentId(4L).build(),
                        Student.builder().id(8L).name("jobob").parentId(5L).build()
                ).toList();
        originStudentTree = asList(
                Student.builder().id(1L).name("dromara").matchParent(true)
                        .children(asList(
                                Student.builder().id(3L).name("hutool").parentId(1L)
                                        .children(singletonList(Student.builder().id(6L).name("looly").parentId(3L).build()))
                                        .build(),
                                Student.builder().id(4L).name("sa-token").parentId(1L)
                                        .children(singletonList(Student.builder().id(7L).name("click33").parentId(4L).build()))
                                        .build()))
                        .build(),
                Student.builder().id(2L).name("baomidou").matchParent(true)
                        .children(singletonList(
                                Student.builder().id(5L).name("mybatis-plus").parentId(2L)
                                        .children(singletonList(
                                                Student.builder().id(8L).name("jobob").parentId(5L).build()
                                        ))
                                        .build()))
                        .build()
        );
        treeByLevelOriginStudentTree = asList(
                Student.builder().id(3L).name("hutool").parentId(1L)
                        .children(singletonList(Student.builder().id(6L).name("looly").parentId(3L).build()))
                        .build(),
                Student.builder().id(4L).name("sa-token").parentId(1L)
                        .children(singletonList(Student.builder().id(7L).name("click33").parentId(4L).build()))
                        .build(),
                Student.builder().id(5L).name("mybatis-plus").parentId(2L)
                        .children(singletonList(
                                Student.builder().id(8L).name("jobob").parentId(5L).build()
                        ))
                        .build()
        );
        treeFromRootToLevelOriginStudentTree = asList(
                Student.builder().id(1L).name("dromara").matchParent(true)
                        .children(asList(
                                Student.builder().id(3L).name("hutool").parentId(1L).build(),
                                Student.builder().id(4L).name("sa-token").parentId(1L).build()))
                        .build(),
                Student.builder().id(2L).name("baomidou").matchParent(true)
                        .children(singletonList(
                                Student.builder().id(5L).name("mybatis-plus").parentId(2L).build()))
                        .build()
        );
        studentTreeHelper = TreeHelper.of(Student::getId, Student::getParentId, null, Student::getChildren, Student::setChildren);
    }

    @Test
    void testToTreeAndLevel() {
        List<Student> studentTree = studentTreeHelper.toTree(originStudentList);
        Assertions.assertEquals(originStudentTree, studentTree);
        TreeHelper<Student, Long> conditionTreeHelper = TreeHelper.ofMatch(Student::getId, Student::getParentId, s -> Boolean.TRUE.equals(s.getMatchParent()), Student::getChildren, Student::setChildren);
        Assertions.assertEquals(originStudentTree, conditionTreeHelper.toTree(originStudentList));
        Assertions.assertEquals(treeByLevelOriginStudentTree, conditionTreeHelper.getTreeByLevel(originStudentList, 2));
        Assertions.assertEquals(treeFromRootToLevelOriginStudentTree, conditionTreeHelper.toTree(originStudentList, 1));
    }

    @Test
    void testFlat() {
        List<Student> studentList = studentTreeHelper.flat(originStudentTree);
        studentList.sort(Comparator.comparing(Student::getId));
        Assertions.assertEquals(originStudentList, studentList);
    }

    @Test
    void testFilter() {
        List<Student> studentTree = studentTreeHelper.filter(originStudentTree, s -> "looly".equals(s.getName()));
        Assertions.assertEquals(singletonList(
                        Student.builder().id(1L).name("dromara").matchParent(true)
                                .children(singletonList(Student.builder().id(3L).name("hutool").parentId(1L)
                                        .children(singletonList(Student.builder().id(6L).name("looly").parentId(3L).build()))
                                        .build()))
                                .build()),
                studentTree);
    }

    @Test
    void testForeach() {
        List<Student> studentList = studentTreeHelper.forEach(originStudentTree, s -> s.setName("【open source】" + s.getName()));
        Assertions.assertEquals(asList(
                Student.builder().id(1L).name("【open source】dromara").matchParent(true)
                        .children(asList(Student.builder().id(3L).name("【open source】hutool").parentId(1L)
                                        .children(singletonList(Student.builder().id(6L).name("【open source】looly").parentId(3L).build()))
                                        .build(),
                                Student.builder().id(4L).name("【open source】sa-token").parentId(1L)
                                        .children(singletonList(Student.builder().id(7L).name("【open source】click33").parentId(4L).build()))
                                        .build()))
                        .build(),
                Student.builder().id(2L).name("【open source】baomidou").matchParent(true)
                        .children(singletonList(
                                Student.builder().id(5L).name("【open source】mybatis-plus").parentId(2L)
                                        .children(singletonList(
                                                Student.builder().id(8L).name("【open source】jobob").parentId(5L).build()
                                        ))
                                        .build()))
                        .build()
        ), studentList);
    }

    @Data
    @Builder
    private static class Student {
        private String name;
        private Integer age;
        private Long id;
        private Long parentId;
        private List<Student> children;
        private Boolean matchParent;

        @Tolerate
        public Student() {
            // this is an accessible parameterless constructor.
        }
    }

}
