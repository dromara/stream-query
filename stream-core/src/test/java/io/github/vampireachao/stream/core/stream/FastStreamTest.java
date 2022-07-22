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
class FastStreamTest {

    @Test
    void testBuilder() {
        List<Integer> list = FastStream.<Integer>builder().add(1).add(2).add(3).build().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), list);
    }

    @Test
    void testOf() {
        Assertions.assertEquals(3, FastStream.of(Arrays.asList(1, 2, 3)).count());
        Assertions.assertEquals(3, FastStream.of(1, 2, 3).count());
        Assertions.assertEquals(3, FastStream.of(Stream.builder().add(1).add(2).add(3).build()).count());
    }

    @Test
    void testSplit() {
        List<Integer> list = FastStream.split("1,2,3", ",").map(Integer::valueOf).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), list);
    }

    @Test
    void testIterator() {
        List<Integer> list = FastStream.iterate(0, i -> i < 3, i -> ++i).toList();
        Assertions.assertEquals(Arrays.asList(0, 1, 2), list);
    }

    @Test
    void testToCollection() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> toCollection = FastStream.of(list).map(String::valueOf).toCollection(LinkedList::new);
        Assertions.assertEquals(Arrays.asList("1", "2", "3"), toCollection);
    }

    @Test
    void testToList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<String> toList = FastStream.of(list).map(String::valueOf).toList();
        Assertions.assertEquals(Arrays.asList("1", "2", "3"), toList);
    }

    @Test
    void testToSet() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Set<String> toSet = FastStream.of(list).map(String::valueOf).toSet();
        Assertions.assertEquals(new HashSet<>(Arrays.asList("1", "2", "3")), toSet);
    }

    @Test
    void testToZip() {
        List<Integer> orders = Arrays.asList(1, 2, 3);
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        Map<Integer, String> toZip = FastStream.of(orders).toZip(list);
        Assertions.assertEquals(new HashMap<Integer, String>() {{
            put(1, "dromara");
            put(2, "hutool");
            put(3, "sweet");
        }}, toZip);
    }

    @Test
    void testJoin() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        String joining = FastStream.of(list).join();
        Assertions.assertEquals("123", joining);
        Assertions.assertEquals("1,2,3", FastStream.of(list).join(","));
        Assertions.assertEquals("(1,2,3)", FastStream.of(list).join(",", "(", ")"));
    }

    @Test
    void testToMap() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Map<String, Integer> identityMap = FastStream.of(list).toMap(String::valueOf);
        Assertions.assertEquals(new HashMap<String, Integer>() {{
            put("1", 1);
            put("2", 2);
            put("3", 3);
        }}, identityMap);
    }

    @Test
    void testGroup() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Map<String, List<Integer>> group = FastStream.of(list).group(String::valueOf);
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
        List<String> mapIndex = FastStream.of(list).mapIdx((e, i) -> i + 1 + "." + e).toList();
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
        // 并行流时为-1
        Assertions.assertEquals(Arrays.asList(-1, -1, -1), FastStream.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
    }

    @Test
    void testMapMulti() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> mapMulti = FastStream.of(list).<Integer>mapMulti((e, buffer) -> {
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
        List<Integer> distinctBy = FastStream.of(list).distinct(String::valueOf).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), distinctBy);
    }

    @Test
    void testForeachIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        FastStream.StreamBuilder<String> builder = FastStream.builder();
        FastStream.of(list).forEachIdx((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
        // 并行流时为-1
        FastStream.of(1, 2, 3).parallel().forEachIdx((e, i) -> Assertions.assertEquals(-1, i));
    }

    @Test
    void testForEachOrderedIndex() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        FastStream.StreamBuilder<String> builder = FastStream.builder();
        FastStream.of(list).forEachOrderedIdx((e, i) -> builder.accept(i + 1 + "." + e));
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
    }

    @Test
    void testFlatMapIter() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> flatMapIter = FastStream.of(list).<Integer>flatMapIter(e -> null).toList();
        Assertions.assertEquals(Collections.emptyList(), flatMapIter);
    }

    @Test
    void testFilter() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> filterIndex = FastStream.of(list).filter(String::valueOf, "1").toList();
        Assertions.assertEquals(Collections.singletonList(1), filterIndex);
    }

    @Test
    void testFilterIdx() {
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> filterIndex = FastStream.of(list).filterIdx((e, i) -> i < 2).toList();
        Assertions.assertEquals(Arrays.asList("dromara", "hutool"), filterIndex);
        // 并行流时为-1
        Assertions.assertEquals(3L, FastStream.of(1, 2, 3).parallel().filterIdx((e, i) -> i == -1).count());
    }

    @Test
    void testNonNull() {
        List<Integer> list = Arrays.asList(1, null, 2, 3);
        List<Integer> nonNull = FastStream.of(list).nonNull().toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), nonNull);
    }

    @Test
    void testParallel() {
        Assertions.assertTrue(FastStream.of(1, 2, 3).parallel(true).isParallel());
        Assertions.assertFalse(FastStream.of(1, 2, 3).parallel(false).isParallel());
    }

    @Test
    void testPush() {
        List<Integer> list = Arrays.asList(1, 2);
        List<Integer> push = FastStream.of(list).push(3).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), push);
    }

    @Test
    void testUnshift() {
        List<Integer> list = Arrays.asList(2, 3);
        List<Integer> unshift = FastStream.of(list).unshift(1).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), unshift);
    }

    @Test
    void testAt() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Assertions.assertEquals(1, FastStream.of(list).at(0));
        Assertions.assertEquals(1, FastStream.of(list).at(-3));
        Assertions.assertEquals(3, FastStream.of(list).at(-1));
        Assertions.assertNull(FastStream.of(list).at(-4));
    }

    @Test
    void testFindFirst() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Integer find = FastStream.of(list).findFirst(Objects::nonNull);
        Assertions.assertEquals(1, find);
    }

    @Test
    void testFindLast() {
        List<Integer> list = Arrays.asList(1, null, 3);
        Integer find = FastStream.of(list).findLast(Objects::nonNull);
        Assertions.assertEquals(3, find);
    }

    @Test
    void testZip() {
        List<Integer> orders = Arrays.asList(1, 2, 3);
        List<String> list = Arrays.asList("dromara", "hutool", "sweet");
        List<String> zip = FastStream.of(orders).zip(list, (e1, e2) -> e1 + "." + e2).toList();
        Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), zip);
    }
}
