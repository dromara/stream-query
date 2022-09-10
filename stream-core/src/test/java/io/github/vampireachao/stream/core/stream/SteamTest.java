package io.github.vampireachao.stream.core.stream;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/7/19 14:14
 */
class SteamTest {

    @Test
    void testBuilder() {
        List<Integer> list = Steam.<Integer>builder().add(1).add(2).add(3).build().toList();
        Assertions.assertEquals(asList(1, 2, 3), list);
    }

    @Test
    void testOf() {
        Assertions.assertEquals(3, Steam.of(asList(1, 2, 3), true).count());
        Assertions.assertEquals(3, Steam.of(1, 2, 3).count());
        Assertions.assertEquals(3, Steam.of(Stream.builder().add(1).add(2).add(3).build()).count());
    }

    @Test
    void testSplit() {
        List<Integer> list = Steam.split("1,2,3", ",").map(Integer::valueOf).toList();
        Assertions.assertEquals(asList(1, 2, 3), list);
    }

    @Test
    void testIterator() {
        List<Integer> list = Steam.iterate(0, i -> i < 3, i -> ++i).toList();
        Assertions.assertEquals(asList(0, 1, 2), list);
    }


    @Test
    void testToCollection() {
        List<Integer> list = asList(1, 2, 3);
        List<String> toCollection = Steam.of(list).map(String::valueOf).toCollection(LinkedList::new);
        Assertions.assertEquals(asList("1", "2", "3"), toCollection);
    }

    @Test
    void testToList() {
        List<Integer> list = asList(1, 2, 3);
        List<String> toList = Steam.of(list).map(String::valueOf).toList();
        Assertions.assertEquals(asList("1", "2", "3"), toList);
    }

    @Test
    void testToUnmodifiableList() {
        List<Integer> list = Steam.of(1, 2, 3)
                .toUnmodifiableList();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> list.remove(0));
    }

    @Test
    void testToSet() {
        List<Integer> list = asList(1, 2, 3);
        Set<String> toSet = Steam.of(list).map(String::valueOf).toSet();
        Assertions.assertEquals(new HashSet<>(asList("1", "2", "3")), toSet);
    }

    @Test
    void testToUnmodifiableSet() {
        Set<Integer> set = Steam.of(1, 2, 3)
                .toUnmodifiableSet();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> set.remove(0));
    }

    @Test
    void testToZip() {
        List<Integer> orders = asList(1, 2, 3);
        List<String> list = asList("dromara", "hutool", "sweet");
        Map<Integer, String> toZip = Steam.of(orders).toZip(list);
        Assertions.assertEquals(new HashMap<Integer, String>() {{
            put(1, "dromara");
            put(2, "hutool");
            put(3, "sweet");
        }}, toZip);
    }

    @Test
    void testJoin() {
        List<Integer> list = asList(1, 2, 3);
        String joining = Steam.of(list).join();
        Assertions.assertEquals("123", joining);
        Assertions.assertEquals("1,2,3", Steam.of(list).join(","));
        Assertions.assertEquals("(1,2,3)", Steam.of(list).join(",", "(", ")"));
    }

    @Test
    void testToMap() {
        List<Integer> list = asList(1, 2, 3);
        Map<String, Integer> identityMap = Steam.of(list).toMap(String::valueOf);
        Assertions.assertEquals(new HashMap<String, Integer>() {{
            put("1", 1);
            put("2", 2);
            put("3", 3);
        }}, identityMap);
    }

    @Test
    void testToUnmodifiableMap() {
        Map<Integer, Integer> map1 = Steam.of(1, 2, 3).toUnmodifiableMap(Function.identity(), Function.identity());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map1.remove(1));
        Map<Integer, Integer> map2 = Steam.of(1, 2, 3).toUnmodifiableMap(Function.identity(), Function.identity(), (t1, t2) -> t1);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map2.remove(1));
    }

    @Test
    void testGroup() {
        List<Integer> list = asList(1, 2, 3);
        Map<String, List<Integer>> group = Steam.of(list).group(String::valueOf);
        Assertions.assertEquals(
                new HashMap<String, List<Integer>>() {{
                    put("1", singletonList(1));
                    put("2", singletonList(2));
                    put("3", singletonList(3));
                }}, group);
    }

    @Test
    void testPartition() {
        // 是否为奇数
        Predicate<Integer> predicate = t -> (t & 1) == 1;

        Map<Boolean, List<Integer>> map = Steam.of(1, 1, 2, 3).partition(predicate, Collectors.toList());
        Assertions.assertEquals(3, map.get(Boolean.TRUE).size());
        Assertions.assertEquals(1, map.get(Boolean.FALSE).size());

        map = Steam.of(1, 1, 2, 3).partition(predicate);
        Assertions.assertEquals(3, map.get(Boolean.TRUE).size());
        Assertions.assertEquals(1, map.get(Boolean.FALSE).size());

        Map<Boolean, Set<Integer>> map2 = Steam.of(1, 1, 2, 3).partition(predicate, HashSet::new);
        Assertions.assertEquals(2, map2.get(Boolean.TRUE).size());
        Assertions.assertEquals(1, map2.get(Boolean.FALSE).size());
    }

    @Test
    void testGenerate() {
        Random random = new SecureRandom();
        Steam<Integer> limit = Steam.generate(() -> random.nextInt(10)).limit(10);
        Assertions.assertEquals(Boolean.TRUE, limit.allMatch(v -> v >= 0 && v < 10));
    }


    @Test
    void testFilterIter() {
        List<Student> list = asList(
                Student.builder().name("臧臧").age(23).build(),
                Student.builder().name("阿超").age(21).build()
        );
        List<Student> others = asList(
                Student.builder().age(22).build(),
                Student.builder().age(23).build()
        );
        List<Student> students = Steam.of(list).filterIter(Student::getAge, others).toList();
        Assertions.assertEquals(singletonList(Student.builder().name("臧臧").age(23).build()), students);
    }

    @Test
    void testMapIdx() {
        List<String> list = asList("dromara", "hutool", "sweet");
        List<String> mapIndex = Steam.of(list).mapIdx((e, i) -> i + 1 + "." + e).toList();
        Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
        // 并行流时为-1
        Assertions.assertEquals(asList(-1, -1, -1), Steam.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
    }

    @Test
    void testMapMulti() {
        List<Integer> list = asList(1, 2, 3);
        List<Integer> mapMulti = Steam.of(list).<Integer>mapMulti((e, buffer) -> {
            if (e % 2 == 0) {
                buffer.accept(e);
            }
            buffer.accept(e);
        }).toList();
        Assertions.assertEquals(asList(1, 2, 2, 3), mapMulti);
    }

    @Test
    void testDistinct() {
        List<Integer> list = asList(1, 2, 2, 3);
        List<Integer> distinctBy = Steam.of(list).distinct(String::valueOf).toList();
        Assertions.assertEquals(asList(1, 2, 3), distinctBy);
    }

    @Test
    void testForeachIdx() {
        List<String> list = asList("dromara", "hutool", "sweet");
        Steam.Builder<String> builder = Steam.builder();
        Steam.of(list).forEachIdx((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
        // 并行流时为-1
        Steam.of(1, 2, 3).parallel().forEachIdx((e, i) -> Assertions.assertEquals(-1, i));
    }

    @Test
    void testForEachOrderedIdx() {
        List<String> list = asList("dromara", "hutool", "sweet");
        Steam.Builder<String> builder = Steam.builder();
        Steam.of(list).forEachOrderedIdx((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
    }

    @Test
    void testFlatIdx() {
        List<String> list = asList("dromara", "hutool", "sweet");
        List<String> mapIndex = Steam.of(list).flatIdx((e, i) -> Steam.of(i + 1 + "." + e)).toList();
        Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
        // 并行流时为-1
        Assertions.assertEquals(asList(-1, -1, -1), Steam.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
    }

    @Test
    void testFlatIter() {
        List<Integer> list = asList(1, 2, 3);
        List<Integer> flatMapIter = Steam.of(list).<Integer>flat(e -> null).toList();
        Assertions.assertEquals(Collections.emptyList(), flatMapIter);
    }

    @Test
    void testFilter() {
        List<Integer> list = asList(1, 2, 3);
        List<Integer> filterIndex = Steam.of(list).filter(String::valueOf, "1").toList();
        Assertions.assertEquals(singletonList(1), filterIndex);
    }

    @Test
    void testFilterIdx() {
        List<String> list = asList("dromara", "hutool", "sweet");
        List<String> filterIndex = Steam.of(list).filterIdx((e, i) -> i < 2).toList();
        Assertions.assertEquals(asList("dromara", "hutool"), filterIndex);
        // 并行流时为-1
        Assertions.assertEquals(3L, Steam.of(1, 2, 3).parallel().filterIdx((e, i) -> i == -1).count());
    }

    @Test
    void testNonNull() {
        List<Integer> list = asList(1, null, 2, 3);
        List<Integer> nonNull = Steam.of(list).nonNull().toList();
        Assertions.assertEquals(asList(1, 2, 3), nonNull);
    }

    @Test
    void testPush() {
        List<Integer> list = asList(1, 2);
        List<Integer> push = Steam.of(list).push(3).toList();
        Assertions.assertEquals(asList(1, 2, 3), push);

        List<Integer> lastPust = Steam.of(push).push(2, 1).toList();
        Assertions.assertEquals(asList(1, 2, 3, 2, 1), lastPust);
    }

    @Test
    void testParallel() {
        Assertions.assertTrue(Steam.of(1, 2, 3).parallel(true).isParallel());
        Assertions.assertFalse(Steam.of(1, 2, 3).parallel(false).isParallel());
    }

    @Test
    void testUnshift() {
        List<Integer> list = asList(2, 3);
        List<Integer> unshift = Steam.of(list).unshift(1).toList();
        Assertions.assertEquals(asList(1, 2, 3), unshift);

        List<Integer> lastUnshift = Steam.of(unshift).unshift(3, 2).toList();
        Assertions.assertEquals(asList(3, 2, 1, 2, 3), lastUnshift);
    }

    @Test
    void testAt() {
        List<Integer> list = asList(1, 2, 3);
        Assertions.assertEquals(1, Steam.of(list).at(0).orElse(null));
        Assertions.assertEquals(2, Steam.of(list).at(1).orElse(null));
        Assertions.assertEquals(1, Steam.of(list).at(-3).orElse(null));
        Assertions.assertEquals(3, Steam.of(list).at(-1).orElse(null));
        Assertions.assertFalse(Steam.of(list).at(-4).isPresent());
    }

    @Test
    void testSplice() {
        List<Integer> list = asList(1, 2, 3);
        Assertions.assertEquals(asList(1, 2, 2, 3), Steam.of(list).splice(1, 0, 2).toList());
        Assertions.assertEquals(asList(1, 2, 3, 3), Steam.of(list).splice(3, 1, 3).toList());
        Assertions.assertEquals(asList(1, 2, 4), Steam.of(list).splice(2, 1, 4).toList());
        Assertions.assertEquals(asList(1, 2), Steam.of(list).splice(2, 1).toList());
        Assertions.assertEquals(asList(1, 2, 3), Steam.of(list).splice(2, 0).toList());
        Assertions.assertEquals(asList(1, 2), Steam.of(list).splice(-1, 1).toList());
        Assertions.assertEquals(asList(1, 2, 3), Steam.of(list).splice(-2, 2, 2, 3).toList());
    }

    @Test
    void testFindFirst() {
        List<Integer> list = asList(1, 2, 3);
        Optional<Integer> find = Steam.of(list).findFirst(Objects::nonNull);
        Assertions.assertEquals(1, find.orElse(null));

    }

    @Test
    void testFindFirstIdx() {
        List<Integer> list = asList(null, 2, 3);
        Integer idx = Steam.of(list).findFirstIdx(Objects::nonNull);
        Assertions.assertEquals(1, idx);
        Assertions.assertEquals(-1, Steam.of(list).parallel().findFirstIdx(Objects::nonNull));
    }

    @Test
    void testFindLast() {
        List<Integer> list = asList(1, null, 3);
        Optional<Integer> find = Steam.of(list).findLast(Objects::nonNull);
        Assertions.assertEquals(3, find.orElse(null));
        Assertions.assertEquals(3, Steam.of(list).findLast().orElse(null));
    }

    @Test
    void testFindLastIdx() {
        List<Integer> list = asList(1, null, 3);
        Integer idx = Steam.of(list).findLastIdx(Objects::nonNull);
        Assertions.assertEquals(2, idx);
        Assertions.assertEquals(-1, Steam.of(list).parallel().findLastIdx(Objects::nonNull));
    }

    @Test
    void testReverse() {
        List<Integer> list = asList(1, 3, 2);
        List<Integer> reverse = Steam.of(list).reverse().toList();
        Assertions.assertEquals(asList(2, 3, 1), reverse);
    }

    @Test
    void testZip() {
        List<Integer> orders = asList(1, 2, 3);
        List<String> list = asList("dromara", "hutool", "sweet");
        List<String> zip = Steam.of(orders).zip(list, (e1, e2) -> e1 + "." + e2).toList();
        Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), zip);
    }

    @Test
    void testListSplit() {
        List<Integer> list = asList(1, 2, 3, 4, 5);
        List<List<Integer>> lists = Steam.of(list).split(2).map(Steam::toList).toList();
        Assertions.assertEquals(asList(asList(1, 2),
                asList(3, 4),
                singletonList(5)
        ), lists);
    }

    @Test
    void testSplitList() {
        List<Integer> list = asList(1, 2, 3, 4, 5);
        List<List<Integer>> lists = Steam.of(list).splitList(2).toList();
        Assertions.assertEquals(asList(asList(1, 2),
                asList(3, 4),
                singletonList(5)
        ), lists);
    }

    @Test
    void testToTree() {
        Consumer<Object> test = o -> {
            List<Student> studentTree = Steam
                    .of(
                            Student.builder().id(1L).name("dromara").build(),
                            Student.builder().id(2L).name("baomidou").build(),
                            Student.builder().id(3L).name("hutool").parentId(1L).build(),
                            Student.builder().id(4L).name("sa-token").parentId(1L).build(),
                            Student.builder().id(5L).name("mybatis-plus").parentId(2L).build(),
                            Student.builder().id(6L).name("looly").parentId(3L).build(),
                            Student.builder().id(7L).name("click33").parentId(4L).build(),
                            Student.builder().id(8L).name("jobob").parentId(5L).build()
                    )
                    // just 3 lambda,top parentId is null
                    .toTree(Student::getId, Student::getParentId, Student::setChildren);
            Assertions.assertEquals(asList(
                    Student.builder().id(1L).name("dromara")
                            .children(asList(
                                    Student.builder().id(3L).name("hutool").parentId(1L)
                                            .children(singletonList(Student.builder().id(6L).name("looly").parentId(3L).build()))
                                            .build(),
                                    Student.builder().id(4L).name("sa-token").parentId(1L)
                                            .children(singletonList(Student.builder().id(7L).name("click33").parentId(4L).build()))
                                            .build()))
                            .build(),
                    Student.builder().id(2L).name("baomidou")
                            .children(singletonList(
                                    Student.builder().id(5L).name("mybatis-plus").parentId(2L)
                                            .children(singletonList(
                                                    Student.builder().id(8L).name("jobob").parentId(5L).build()
                                            ))
                                            .build()))
                            .build()
            ), studentTree);
        };
        test = test.andThen(o -> {
            List<Student> studentTree = Steam
                    .of(
                            Student.builder().id(1L).name("dromara").matchParent(true).build(),
                            Student.builder().id(2L).name("baomidou").matchParent(true).build(),
                            Student.builder().id(3L).name("hutool").parentId(1L).build(),
                            Student.builder().id(4L).name("sa-token").parentId(1L).build(),
                            Student.builder().id(5L).name("mybatis-plus").parentId(2L).build(),
                            Student.builder().id(6L).name("looly").parentId(3L).build(),
                            Student.builder().id(7L).name("click33").parentId(4L).build(),
                            Student.builder().id(8L).name("jobob").parentId(5L).build()
                    )
                    // just 4 lambda ,top by condition
                    .toTree(Student::getId, Student::getParentId, Student::setChildren, Student::getMatchParent);
            Assertions.assertEquals(asList(
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
            ), studentTree);
        });
    }

    @Test
    void testFlatTree() {
        List<Student> studentTree = asList(
                Student.builder().id(1L).name("dromara")
                        .children(asList(Student.builder().id(3L).name("hutool").parentId(1L)
                                        .children(singletonList(Student.builder().id(6L).name("looly").parentId(3L).build()))
                                        .build(),
                                Student.builder().id(4L).name("sa-token").parentId(1L)
                                        .children(singletonList(Student.builder().id(7L).name("click33").parentId(4L).build()))
                                        .build()))
                        .build(),
                Student.builder().id(2L).name("baomidou")
                        .children(singletonList(
                                Student.builder().id(5L).name("mybatis-plus").parentId(2L)
                                        .children(singletonList(
                                                Student.builder().id(8L).name("jobob").parentId(5L).build()
                                        ))
                                        .build()))
                        .build()
        );
        Assertions.assertEquals(asList(
                Student.builder().id(1L).name("dromara").build(),
                Student.builder().id(2L).name("baomidou").build(),
                Student.builder().id(3L).name("hutool").parentId(1L).build(),
                Student.builder().id(4L).name("sa-token").parentId(1L).build(),
                Student.builder().id(5L).name("mybatis-plus").parentId(2L).build(),
                Student.builder().id(6L).name("looly").parentId(3L).build(),
                Student.builder().id(7L).name("click33").parentId(4L).build(),
                Student.builder().id(8L).name("jobob").parentId(5L).build()
        ), Steam.of(studentTree).flatTree(Student::getChildren, Student::setChildren).sorted(Comparator.comparingLong(Student::getId)).toList());

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
        private Long id;
        private Long parentId;
        private List<Student> children;
        private Boolean matchParent;
    }

    @Test
    @Ignore
    void testLog() {
        List<Integer> list = asList(0, 1, 2);
        Assertions.assertEquals(asList(1, 2, 3), Steam.of(list).map(i -> i + 1).log().toList());
    }

}
