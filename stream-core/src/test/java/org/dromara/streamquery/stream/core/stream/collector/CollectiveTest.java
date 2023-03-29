package org.dromara.streamquery.stream.core.stream.collector;

import org.dromara.streamquery.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.dromara.streamquery.stream.core.stream.collector.Collective.*;

/**
 * Collectors测试
 *
 * @author VampireAchao Cizai_
 * @since 2022/6/22 11:10
 */
class CollectiveTest {

    @Test
    void testToMap() {
        Assertions.assertNull(Stream.of(null, null, null).collect(toMap(Object::hashCode, Object::hashCode)).get(null));
    }

    @Test
    void testGroupingBy() {
        Assertions.assertArrayEquals(new Object[]{}, Stream.of(null, null, null).parallel()
                .collect(groupingBy(Object::hashCode)).get(null).toArray());

        Map<? extends Class<?>, Integer> group = Steam.of(null, null, null).group(Object::getClass, summingInt(Object::hashCode));
        Assertions.assertFalse(group.isEmpty());

        Assertions.assertFalse(Steam.of(null, 1).parallel().group(Object::hashCode, mapping(Function.identity(), toList())).isEmpty());
    }

    @Test
    void testFlatMapping() {
        List<Integer> actual = Stream.iterate(0, i -> ++i).limit(3)
                .collect(flatMapping(i -> Stream.of(i, i), toList()));
        Assertions.assertEquals(Arrays.asList(0, 0, 1, 1, 2, 2), actual);
    }

    @Test
    void testFlatMappingIter() {
        List<Integer> actual = Stream.iterate(0, i -> ++i).limit(3)
                .collect(flatMappingIter(i -> Steam.of(i, i), toList()));
        Assertions.assertEquals(Arrays.asList(0, 0, 1, 1, 2, 2), actual);
    }

    @Test
    void testTransform() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4)
                .collect(Collective.transform(Steam::of));
        Assertions.assertEquals(Steam.class, stream.getClass());

        stream = Stream.of(1, 2, 3, 4)
                .collect(Collective.transform(HashSet::new, Steam::of));
        Assertions.assertEquals(Steam.class, stream.getClass());
    }

    @Test
    void testToEasyStream() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4)
                .collect(Collective.toSteam());
        Assertions.assertEquals(Steam.class, stream.getClass());
    }

    @Test
    void testToArray() {
        final Integer[] arrays = Steam.of(1, 2, 3, 4, 5).collect(toArray(Integer[]::new));
        Assertions.assertEquals(5, arrays.length);
        Assertions.assertEquals(5, arrays[4]);
    }

    @Test
    void testToCollection() {
        HashSet<Integer> list = Steam.of(1, 2, 3, 4, 5).parallel()
                .collect(Collective.toCollection(HashSet::new));
        Assertions.assertEquals(5, list.size());
    }

    @Test
    void testToList() {
        List<Integer> list = Steam.of(1, 2, 3, 4, 5).parallel().collect(Collective.toList());
        Assertions.assertEquals(5, list.size());
    }

    @Test
    void testToSet() {
        Set<Integer> list = Steam.of(1, 2, 3, 4, 5).parallel().collect(Collective.toSet());
        Assertions.assertEquals(5, list.size());
    }

    @Test
    void testJoining() {
        String list = Steam.of(1, 2, 3, 4, 5).map(String::valueOf).parallel().collect(Collective.joining());
        Assertions.assertEquals("12345", list);

        Assertions.assertEquals("1,2,3,4,5", Steam.of(1, 2, 3, 4, 5).map(String::valueOf).parallel()
                .collect(Collective.joining(",")));
    }

    @Test
    void testFiltering() {
        final Map<Integer, Long> map = Stream.of(1, 2, 3)
                .collect(Collectors.groupingBy(Function.identity(),
                        Collective.filtering(i -> i > 1, Collectors.counting())
                ));
        Assertions.assertEquals(new HashMap<Integer, Long>() {
            {
                put(1, 0L);
                put(2, 1L);
                put(3, 1L);
            }
        }, map);
    }

}
