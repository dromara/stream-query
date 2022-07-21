package io.github.vampireachao.stream.core.stream;

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
        Assertions.assertEquals(3, Steam.of(Arrays.asList(1, 2, 3)).count());
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
        List<String> toCollection = Steam.of(list).map(String::valueOf).toCollection(LinkedList::new);
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
    void testJoining() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        String joining = Steam.of(list).joining();
        Assertions.assertEquals("123", joining);
        Assertions.assertEquals("1,2,3", Steam.of(list).joining(","));
        Assertions.assertEquals("(1,2,3)", Steam.of(list).joining(",", "(", ")"));
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
    void testGroupingBy() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Map<String, List<Integer>> groupingBy = Steam.of(list).groupingBy(String::valueOf);
        Assertions.assertEquals(
                new HashMap<String, List<Integer>>() {{
                    put("1", singletonList(1));
                    put("2", singletonList(2));
                    put("3", singletonList(3));
                }}, groupingBy);
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
    void testForeachIndex() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        Steam.SteamBuilder<String> builder = Steam.builder();
        Steam.of(list).forEachIndex((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
        // 并行流时为-1
        Steam.of(1, 2, 3).parallel().forEachIndex((e, i) -> Assertions.assertEquals(-1, i));
    }

    @Test
    void testForEachOrderedIndex() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        Steam.SteamBuilder<String> builder = Steam.builder();
        Steam.of(list).forEachOrderedIndex((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
    }

    @Test
    void testFlatMapIter() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> flatMapIter = Steam.of(list).<Integer>flatMapIter(e -> null).toList();
        Assertions.assertEquals(Collections.emptyList(), flatMapIter);
    }

    @Test
    void testNonNull() {
        List<Integer> list = Arrays.asList(1, null, 2, 3);
        List<Integer> nonNull = Steam.of(list).nonNull().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), nonNull);
    }
}
