package io.github.vampireachao.stream.core.collector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertArrayEquals(new Object[]{null, null, null}, Stream.of(null, null, null).collect(Collectors.groupingBy(Object::hashCode)).get(null).toArray());
    }

}
