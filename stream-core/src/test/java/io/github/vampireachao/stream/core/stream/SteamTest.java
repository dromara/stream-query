package io.github.vampireachao.stream.core.stream;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

/**
 * @author VampireAchao
 * @since 2022/7/19 14:14
 */
class SteamTest {

    @Test
    void testBuilder() {
        List<Integer> list = Steam.<Integer>builder().add(1).add(2).add(3).build().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), list);
    }

    @Test
    void testOf() {
        Assertions.assertEquals(3, Steam.of(Arrays.asList(1, 2, 3), true).count());
        Assertions.assertEquals(3, Steam.of(1, 2, 3).count());
        Assertions.assertEquals(3, Steam.of(Stream.builder().add(1).add(2).add(3).build()).count());
    }

    @Test
    void testSplit() {
        List<Integer> list = Steam.split("1,2,3", ",").map(Integer::valueOf).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), list);
    }

    @Test
    void testIterator() {
        List<Integer> list = Steam.iterate(0, i -> i < 3, i -> ++i).toList();
        Assertions.assertEquals(Arrays.asList(0, 1, 2), list);
    }


    @Test
    void testToCollection() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> toCollection = Steam.of(list).map(String::valueOf).toColl(LinkedList::new);
        Assertions.assertEquals(Arrays.asList("1", "2", "3"), toCollection);
    }

    @Test
    void testToList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> toList = Steam.of(list).map(String::valueOf).toList();
        Assertions.assertEquals(Arrays.asList("1", "2", "3"), toList);
    }

    @Test
    void testToSet() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Set<String> toSet = Steam.of(list).map(String::valueOf).toSet();
        Assertions.assertEquals(new HashSet<>(Arrays.asList("1", "2", "3")), toSet);
    }

    @Test
    void testToZip() {
        List<Integer> orders = Arrays.asList(1, 2, 3);
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        Map<Integer, String> toZip = Steam.of(orders).toZip(list);
        Assertions.assertEquals(new HashMap<Integer, String>() {{
            put(1, "dromara");
            put(2, "hutool");
            put(3, "sweet");
        }}, toZip);
    }

    @Test
    void testJoin() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        String joining = Steam.of(list).join();
        Assertions.assertEquals("123", joining);
        Assertions.assertEquals("1,2,3", Steam.of(list).join(","));
        Assertions.assertEquals("(1,2,3)", Steam.of(list).join(",", "(", ")"));
    }

    @Test
    void testToMap() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Map<String, Integer> identityMap = Steam.of(list).toMap(String::valueOf);
        Assertions.assertEquals(new HashMap<String, Integer>() {{
            put("1", 1);
            put("2", 2);
            put("3", 3);
        }}, identityMap);
    }

    @Test
    void testGroup() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Map<String, List<Integer>> group = Steam.of(list).group(String::valueOf);
        Assertions.assertEquals(
                new HashMap<String, List<Integer>>() {{
                    put("1", singletonList(1));
                    put("2", singletonList(2));
                    put("3", singletonList(3));
                }}, group);
    }

    @Test
    void testFilterIter() {
        List<Student> list = Arrays.asList(
                Student.builder().name("臧臧").age(23).build(),
                Student.builder().name("阿超").age(21).build()
        );
        List<Student> others = Arrays.asList(
                Student.builder().age(22).build(),
                Student.builder().age(23).build()
        );
        List<Student> students = Steam.of(list).filterIter(Student::getAge, others).toList();
        Assertions.assertEquals(Collections.singletonList(Student.builder().name("臧臧").age(23).build()), students);
    }

    @Data
    @Builder
    public static class Student {
        @Tolerate
        public Student() {
            // this is an accessible parameterless constructor.
        }

        private String name;
        private Integer age;
    }


    @Test
    void testMapIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> mapIndex = Steam.of(list).mapIdx((e, i) -> i + 1 + "." + e).toList();
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
        // 并行流时为-1
        Assertions.assertEquals(Arrays.asList(-1, -1, -1), Steam.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
    }

    @Test
    void testMapMulti() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> mapMulti = Steam.of(list).<Integer>mapMulti((e, buffer) -> {
            if (e % 2 == 0) {
                buffer.accept(e);
            }
            buffer.accept(e);
        }).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 2, 3), mapMulti);
    }

    @Test
    void testDistinct() {
        List<Integer> list = Arrays.asList(1, 2, 2, 3);
        List<Integer> distinctBy = Steam.of(list).distinct(String::valueOf).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), distinctBy);
    }

    @Test
    void testForeachIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        Steam.Builder<String> builder = Steam.builder();
        Steam.of(list).forEachIdx((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
        // 并行流时为-1
        Steam.of(1, 2, 3).parallel().forEachIdx((e, i) -> Assertions.assertEquals(-1, i));
    }

    @Test
    void testForEachOrderedIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        Steam.Builder<String> builder = Steam.builder();
        Steam.of(list).forEachOrderedIdx((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
    }

    @Test
    void testFlatIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> mapIndex = Steam.of(list).flatIdx((e, i) -> Steam.of(i + 1 + "." + e)).toList();
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
        // 并行流时为-1
        Assertions.assertEquals(Arrays.asList(-1, -1, -1), Steam.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
    }

    @Test
    void testFlatIter() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> flatMapIter = Steam.of(list).<Integer>flat(e -> null).toList();
        Assertions.assertEquals(Collections.emptyList(), flatMapIter);
    }

    @Test
    void testFilter() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> filterIndex = Steam.of(list).filter(String::valueOf, "1").toList();
        Assertions.assertEquals(Collections.singletonList(1), filterIndex);
    }

    @Test
    void testFilterIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> filterIndex = Steam.of(list).filterIdx((e, i) -> i < 2).toList();
        Assertions.assertEquals(Arrays.asList("dromara", "hutool"), filterIndex);
        // 并行流时为-1
        Assertions.assertEquals(3L, Steam.of(1, 2, 3).parallel().filterIdx((e, i) -> i == -1).count());
    }

    @Test
    void testNonNull() {
        List<Integer> list = Arrays.asList(1, null, 2, 3);
        List<Integer> nonNull = Steam.of(list).nonNull().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), nonNull);
    }

    @Test
    void testParallel() {
        Assertions.assertTrue(Steam.of(1, 2, 3).parallel(true).isParallel());
        Assertions.assertFalse(Steam.of(1, 2, 3).parallel(false).isParallel());
    }

    @Test
    void testPush() {
        List<Integer> list = Arrays.asList(1, 2);
        List<Integer> push = Steam.of(list).push(3).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), push);
    }

    @Test
    void testUnshift() {
        List<Integer> list = Arrays.asList(2, 3);
        List<Integer> unshift = Steam.of(list).unshift(1).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), unshift);
    }

    @Test
    void testAt() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Assertions.assertEquals(1, Steam.of(list).at(0).orElse(null));
        Assertions.assertEquals(2, Steam.of(list).at(1).orElse(null));
        Assertions.assertEquals(1, Steam.of(list).at(-3).orElse(null));
        Assertions.assertEquals(3, Steam.of(list).at(-1).orElse(null));
        Assertions.assertFalse(Steam.of(list).at(-4).isPresent());
    }

    @Test
    void testSplice() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Assertions.assertEquals(Arrays.asList(1, 2, 2, 3), Steam.of(list).splice(1, 0, 2).toList());
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 3), Steam.of(list).splice(3, 1, 3).toList());
        Assertions.assertEquals(Arrays.asList(1, 2, 4), Steam.of(list).splice(2, 1, 4).toList());
        Assertions.assertEquals(Arrays.asList(1, 2), Steam.of(list).splice(2, 1).toList());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), Steam.of(list).splice(2, 0).toList());
        Assertions.assertEquals(Arrays.asList(1, 2), Steam.of(list).splice(-1, 1).toList());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), Steam.of(list).splice(-2, 2, 2, 3).toList());
    }

    @Test
    void testFindFirst() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Integer find = Steam.of(list).findFirst(Objects::nonNull);
        Assertions.assertEquals(1, find);
    }

    @Test
    void testFindFirstIdx() {
        List<Integer> list = Arrays.asList(null, 2, 3);
        Integer idx = Steam.of(list).findFirstIdx(Objects::nonNull);
        Assertions.assertEquals(1, idx);
        Assertions.assertEquals(-1, Steam.of(list).parallel().findFirstIdx(Objects::nonNull));
    }

    @Test
    void testFindLast() {
        List<Integer> list = Arrays.asList(1, null, 3);
        Integer find = Steam.of(list).findLast(Objects::nonNull);
        Assertions.assertEquals(3, find);
        Assertions.assertEquals(3, Steam.of(list).findLast().orElse(null));
    }

    @Test
    void testFindLastIdx() {
        List<Integer> list = Arrays.asList(1, null, 3);
        Integer idx = Steam.of(list).findLastIdx(Objects::nonNull);
        Assertions.assertEquals(2, idx);
        Assertions.assertEquals(-1, Steam.of(list).parallel().findLastIdx(Objects::nonNull));
    }

    @Test
    void testReverse() {
        List<Integer> list = Arrays.asList(1, 3, 2);
        List<Integer> reverse = Steam.of(list).reverse().toList();
        Assertions.assertEquals(Arrays.asList(2, 3, 1), reverse);
    }

    @Test
    void testZip() {
        List<Integer> orders = Arrays.asList(1, 2, 3);
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> zip = Steam.of(orders).zip(list, (e1, e2) -> e1 + "." + e2).toList();
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), zip);
    }

    @Test
    void testListSplit() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> lists = Steam.of(list).split(2).map(Steam::toList).toList();
        Assertions.assertEquals(Arrays.asList(Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                singletonList(5)
        ), lists);
    }

    @Test
    void testSplitList() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<List<Integer>> lists = Steam.of(list).splitList(2).toList();
        Assertions.assertEquals(Arrays.asList(Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                singletonList(5)
        ), lists);
    }
}
