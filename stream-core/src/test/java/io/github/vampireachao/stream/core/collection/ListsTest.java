package io.github.vampireachao.stream.core.collection;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author VampireAchao
 * @since 2022/10/21 17:09
 */
class ListsTest {

    @Test
    void testOf() {
        Assertions.assertEquals("value", Lists.of("value").get(0));
    }

    @Test
    void testOfColl() {
        Assertions.assertTrue(Lists.ofColl(Collections.emptySet()).isEmpty());
    }

    @Test
    void testReverse() {
        Assertions.assertEquals(asList(1, 3, 2), Lists.reverse(asList(2, 3, 1)));
    }

    @Test
    void testAscend() {
        Assertions.assertEquals(asList(1, 2, 3), Lists.ascend(asList(2, 3, 1)));
    }

    @Test
    void testDescend() {
        Assertions.assertEquals(asList(3, 2, 1), Lists.descend(asList(2, 3, 1)));
    }

    @Test
    void testBinarySearch() {
        Assertions.assertEquals(2, Lists.binarySearch(Lists.of(1, 2, 3), 3));
    }

    @Test
    void testCustomSort() {
        List<Student> list = asList(
                Student.builder().name("aaa").age(23.0).id(123L).build(),
                Student.builder().name("azb").age(21.0).id(122L).build(),
                Student.builder().name("amb").age(22.0).id(124L).build()
        );
        List<Student> others = asList(
                Student.builder().name("azb").age(21.0).id(122L).build(),
                Student.builder().name("amb").age(22.0).id(124L).build(),
                Student.builder().name("aaa").age(23.0).id(123L).build()
        );
        List<Student> students = Lists.customSort(list, Student::getName, () -> Boolean.FALSE);

        Assertions.assertEquals(students,others);
    }

    @Data
    @Builder
    private static class Student{
        private String name;
        private Double age;
        private Long id;

        @Tolerate
        public Student() {
            // this is an accessible parameterless constructor.
        }

    }




}
