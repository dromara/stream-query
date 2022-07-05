package io.github.vampireachao.stream.core.collector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Collectors测试
 *
 * @author VampireAchao
 * @since 2022/6/22 11:10
 */
class CollectorsTest {

    @Test
    void testToMap() {
        Assertions.assertNull(Stream.of(null, null, null).collect(Collectors.toMap(Object::hashCode, Object::hashCode)).get(null));
        Assertions.assertArrayEquals(new Object[]{}, Stream.of(null, null, null).collect(Collectors.groupingBy(Object::hashCode)).get(null).toArray());
    }

    @Test
    void testFlatMapping() {
        List<Integer> actual = Stream.iterate(0, i -> ++i).limit(3)
                .collect(Collectors.flatMapping(i -> Stream.of(i, i), Collectors.toList()));
        Assertions.assertEquals(Arrays.asList(0, 0, 1, 1, 2, 2), actual);
    }

}
