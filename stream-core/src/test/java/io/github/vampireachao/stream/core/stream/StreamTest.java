package io.github.vampireachao.stream.core.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Collections.singletonList;

/**
 * @author VampireAchao
 * @since 2022/7/19 14:14
 */
class StreamTest {

    @Test
    void testBuilder() {
        List<Integer> list = Stream.<Integer>builder().add(1).add(2).add(3).build().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), list);
    }

    @Test
    void testOf() {
        Assertions.assertEquals(3, Stream.of(Arrays.asList(1, 2, 3), true).count());
        Assertions.assertEquals(3, Stream.of(1, 2, 3).count());
        Assertions.assertEquals(3, Stream.of(java.util.stream.Stream.builder().add(1).add(2).add(3).build()).count());
    }

    @Test
    void testSplit() {
        List<Integer> list = Stream.split("1,2,3", ",").map(Integer::valueOf).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), list);
    }

    @Test
    void testIterator() {
        List<Integer> list = Stream.iterate(0, i -> i < 3, i -> ++i).toList();
        Assertions.assertEquals(Arrays.asList(0, 1, 2), list);
    }

    @Test
    void testToCollection() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> toCollection = Stream.of(list).map(String::valueOf).toColl(LinkedList::new);
        Assertions.assertEquals(Arrays.asList("1", "2", "3"), toCollection);
    }

    @Test
    void testToList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> toList = Stream.of(list).map(String::valueOf).toList();
        Assertions.assertEquals(Arrays.asList("1", "2", "3"), toList);
    }

    @Test
    void testToSet() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Set<String> toSet = Stream.of(list).map(String::valueOf).toSet();
        Assertions.assertEquals(new HashSet<>(Arrays.asList("1", "2", "3")), toSet);
    }

    @Test
    void testToZip() {
        List<Integer> orders = Arrays.asList(1, 2, 3);
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        Map<Integer, String> toZip = Stream.of(orders).toZip(list);
        Assertions.assertEquals(new HashMap<Integer, String>() {{
            put(1, "dromara");
            put(2, "hutool");
            put(3, "sweet");
        }}, toZip);
    }

    @Test
    void testJoin() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        String joining = Stream.of(list).join();
        Assertions.assertEquals("123", joining);
        Assertions.assertEquals("1,2,3", Stream.of(list).join(","));
        Assertions.assertEquals("(1,2,3)", Stream.of(list).join(",", "(", ")"));
    }

    @Test
    void testToMap() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Map<String, Integer> identityMap = Stream.of(list).toMap(String::valueOf);
        Assertions.assertEquals(new HashMap<String, Integer>() {{
            put("1", 1);
            put("2", 2);
            put("3", 3);
        }}, identityMap);
    }

    @Test
    void testGroup() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Map<String, List<Integer>> group = Stream.of(list).group(String::valueOf);
        Assertions.assertEquals(
                new HashMap<String, List<Integer>>() {{
                    put("1", singletonList(1));
                    put("2", singletonList(2));
                    put("3", singletonList(3));
                }}, group);
    }

    @Test
    void testMapIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> mapIndex = Stream.of(list).mapIdx((e, i) -> i + 1 + "." + e).toList();
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
        // 并行流时为-1
        Assertions.assertEquals(Arrays.asList(-1, -1, -1), Stream.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
    }

    @Test
    void testMapMulti() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> mapMulti = Stream.of(list).<Integer>mapMulti((e, buffer) -> {
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
        List<Integer> distinctBy = Stream.of(list).distinct(String::valueOf).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), distinctBy);
    }

    @Test
    void testForeachIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        Stream.Builder<String> builder = Stream.builder();
        Stream.of(list).forEachIdx((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
        // 并行流时为-1
        Stream.of(1, 2, 3).parallel().forEachIdx((e, i) -> Assertions.assertEquals(-1, i));
    }

    @Test
    void testForEachOrderedIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        Stream.Builder<String> builder = Stream.builder();
        Stream.of(list).forEachOrderedIdx((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
    }

    @Test
    void testFlatMapIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> mapIndex = Stream.of(list).flatMapIdx((e, i) -> Stream.of(i + 1 + "." + e)).toList();
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
        // 并行流时为-1
        Assertions.assertEquals(Arrays.asList(-1, -1, -1), Stream.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
    }

    @Test
    void testFlatMapIter() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> flatMapIter = Stream.of(list).<Integer>flatMapIter(e -> null).toList();
        Assertions.assertEquals(Collections.emptyList(), flatMapIter);
    }

    @Test
    void testFilter() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> filterIndex = Stream.of(list).filter(String::valueOf, "1").toList();
        Assertions.assertEquals(Collections.singletonList(1), filterIndex);
    }

    @Test
    void testFilterIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> filterIndex = Stream.of(list).filterIdx((e, i) -> i < 2).toList();
        Assertions.assertEquals(Arrays.asList("dromara", "hutool"), filterIndex);
        // 并行流时为-1
        Assertions.assertEquals(3L, Stream.of(1, 2, 3).parallel().filterIdx((e, i) -> i == -1).count());
    }

    @Test
    void testNonNull() {
        List<Integer> list = Arrays.asList(1, null, 2, 3);
        List<Integer> nonNull = Stream.of(list).nonNull().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), nonNull);
    }

    @Test
    void testParallel() {
        Assertions.assertTrue(Stream.of(1, 2, 3).parallel(true).isParallel());
        Assertions.assertFalse(Stream.of(1, 2, 3).parallel(false).isParallel());
    }

    @Test
    void testPush() {
        List<Integer> list = Arrays.asList(1, 2);
        List<Integer> push = Stream.of(list).push(3).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), push);
    }

    @Test
    void testUnshift() {
        List<Integer> list = Arrays.asList(2, 3);
        List<Integer> unshift = Stream.of(list).unshift(1).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), unshift);
    }

    @Test
    void testAt() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Assertions.assertEquals(1, Stream.of(list).at(0));
        Assertions.assertEquals(1, Stream.of(list).at(-3));
        Assertions.assertEquals(3, Stream.of(list).at(-1));
        Assertions.assertNull(Stream.of(list).at(-4));
    }

    @Test
    void testSplice() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Assertions.assertEquals(Arrays.asList(1, 2, 2, 3), Stream.of(list).splice(1, 0, 2).toList());
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 3), Stream.of(list).splice(3, 1, 3).toList());
        Assertions.assertEquals(Arrays.asList(1, 2, 4), Stream.of(list).splice(2, 1, 4).toList());
        Assertions.assertEquals(Arrays.asList(1, 2), Stream.of(list).splice(2, 1).toList());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), Stream.of(list).splice(2, 0).toList());
        Assertions.assertEquals(Arrays.asList(1, 2), Stream.of(list).splice(-1, 1).toList());
        Assertions.assertEquals(Arrays.asList(1, 2, 3), Stream.of(list).splice(-2, 2, 2, 3).toList());
    }

    @Test
    void testFindFirst() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Integer find = Stream.of(list).findFirst(Objects::nonNull);
        Assertions.assertEquals(1, find);
    }

    @Test
    void testFindFirstIdx() {
        List<Integer> list = Arrays.asList(null, 2, 3);
        Integer idx = Stream.of(list).findFirstIdx(Objects::nonNull);
        Assertions.assertEquals(1, idx);
        Assertions.assertEquals(-1, Stream.of(list).parallel().findFirstIdx(Objects::nonNull));
    }

    @Test
    void testFindLast() {
        List<Integer> list = Arrays.asList(1, null, 3);
        Integer find = Stream.of(list).findLast(Objects::nonNull);
        Assertions.assertEquals(3, find);
        Assertions.assertEquals(3, Stream.of(list).findLast().orElse(null));
    }

    @Test
    void testFindLastIdx() {
        List<Integer> list = Arrays.asList(1, null, 3);
        Integer idx = Stream.of(list).findLastIdx(Objects::nonNull);
        Assertions.assertEquals(2, idx);
        Assertions.assertEquals(-1, Stream.of(list).parallel().findLastIdx(Objects::nonNull));
    }

    @Test
    void testZip() {
        List<Integer> orders = Arrays.asList(1, 2, 3);
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> zip = Stream.of(orders).zip(list, (e1, e2) -> e1 + "." + e2).toList();
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), zip);
    }
}
