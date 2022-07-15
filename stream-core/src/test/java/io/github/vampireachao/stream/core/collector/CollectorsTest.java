package io.github.vampireachao.stream.core.collector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.github.vampireachao.stream.core.collector.Collectors.*;

/**
 * Collectors测试
 *
 * @author VampireAchao
 * @since 2022/6/22 11:10
 */
class CollectorsTest {

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

}
