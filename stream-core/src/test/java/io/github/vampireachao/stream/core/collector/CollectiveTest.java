package io.github.vampireachao.stream.core.collector;

import io.github.vampireachao.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.github.vampireachao.stream.core.collector.Collective.*;

/**
 * Collectors测试
 *
 * @author VampireAchao ZVerify
 * @since 2022/6/22 11:10
 */
class CollectiveTest {

    @Test
    void testToMap() {
        Assertions.assertNull(Stream.of(null, null, null).collect(toMap(Object::hashCode, Object::hashCode)).get(null));
    }

    @Test
    void testGroupingBy() {
        Assertions.assertArrayEquals(new Object[]{}, Stream.of(null, null, null).collect(groupingBy(Object::hashCode)).get(null).toArray());
    }

    @Test
    void testFlatMapping() {
        List<Integer> actual = Stream.iterate(0, i -> ++i).limit(3)
                .collect(flatMapping(i -> Stream.of(i, i), toList()));
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
    void testToEntryStream() {
        Map<String, Integer> map = Stream.of(1, 2, 3, 4, 5)
            // 转为EntryStream
            .collect(Collective.toEntrySteam(Function.identity(), String::valueOf))
            // 过滤偶数
            .filterByKey(k -> (k & 1) == 1)
            .inverse()
            .toMap();
        Assertions.assertEquals((Integer)1, map.get("1"));
        Assertions.assertEquals((Integer)3, map.get("3"));
        Assertions.assertEquals((Integer)5, map.get("5"));
    }

}
