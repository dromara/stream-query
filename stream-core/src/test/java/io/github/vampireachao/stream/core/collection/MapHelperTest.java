package io.github.vampireachao.stream.core.collection;

import io.github.vampireachao.stream.core.collector.Collective;
import io.github.vampireachao.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author VampireAchao
 * @since 2022/10/21 15:49
 */
class MapHelperTest {

    @Test
    void testEntry() {
        final Map.Entry<String, String> entry = MapHelper.entry("key", "value");
        Assertions.assertEquals("key", entry.getKey());
        Assertions.assertEquals("value", entry.getValue());
    }

    @Test
    void testOneToManyToOne() {
        final Map<String, List<String>> map = MapHelper.oneToManyToOne(
                new HashMap<String, List<String>>() {{
                    put("key", Arrays.asList("value", null));
                }},
                new HashMap<String, String>() {{
                    put("value", "Good");
                }},
                Steam::nonNull).collect(Collective.entryToMap());
        Assertions.assertEquals(1, map.get("key").size());
        Assertions.assertEquals("Good", map.get("key").get(0));
    }

    @Test
    void testOneToOneToOne() {
        final Map<String, String> map = MapHelper.oneToOneToOne(
                new HashMap<String, String>() {{
                    put("key", "value");
                }},
                new HashMap<String, String>() {{
                    put("value", "Good");
                }},
                String::toUpperCase
        ).collect(Collective.entryToMap());
        Assertions.assertEquals("GOOD", map.get("key"));
    }

    @Test
    void testOneToOneToMany() {
        final Map<String, List<String>> map = MapHelper.oneToOneToMany(
                new HashMap<String, String>() {{
                    put("key", "value");
                }},
                new HashMap<String, List<String>>() {{
                    put("value", Arrays.asList("Good", "Bad"));
                }},
                Steam::nonNull
        ).collect(Collective.entryToMap());
        Assertions.assertEquals(2, map.get("key").size());
        Assertions.assertEquals("Good", map.get("key").get(0));
        Assertions.assertEquals("Bad", map.get("key").get(1));
    }
}
