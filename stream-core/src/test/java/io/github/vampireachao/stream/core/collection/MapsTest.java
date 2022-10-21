package io.github.vampireachao.stream.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author VampireAchao
 * @since 2022/10/21 16:48
 */
class MapsTest {

    @Test
    void testOf() {
        Assertions.assertEquals("value", Maps.of("key", "value").get("key"));
        Assertions.assertEquals("value1", Maps.of("key", "value", "key1", "value1").get("key1"));
        Assertions.assertEquals("value2", Maps.of("key", "value", "key1", "value1", "key2", "value2").get("key2"));
        Assertions.assertEquals("value3", Maps.of("key", "value", "key1", "value1", "key2", "value2", "key3", "value3").get("key3"));
        Assertions.assertEquals("value4", Maps.of("key", "value", "key1", "value1", "key2", "value2", "key3", "value3", "key4", "value4").get("key4"));
    }
}
