package io.github.vampireachao.stream.core.collector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collector;
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
    void testReducing() {
        Set<Map<String, Integer>> nameScoreMapList = Stream.of(
                new HashMap<String, Integer>() {{
                    put("苏格拉底", 1);
                    put("特拉叙马霍斯", 3);
                }},
                Collections.singletonMap("苏格拉底", 2),
                Collections.singletonMap("特拉叙马霍斯", 1),
                Collections.singletonMap("特拉叙马霍斯", 2)
        ).collect(java.util.stream.Collectors.toSet());
        Collector<Map<String, Integer>, ?, Map<String, List<Integer>>> reducing = Collectors.reducing(new HashMap<>(),
                value -> {
                    Map<String, List<Integer>> result = new HashMap<>();
                    value.forEach((k, v) -> result.computeIfAbsent(k, i -> new ArrayList<>()).add(v));
                    return result;
                }, (l, r) -> {
                    r.forEach((k, v) -> l.computeIfAbsent(k, i -> new ArrayList<>()).addAll(v));
                    return l;
                }
        );
        Assertions.assertEquals(new HashMap<String, List<Integer>>() {{
            put("苏格拉底", Arrays.asList(1, 2));
            put("特拉叙马霍斯", Arrays.asList(3, 1, 2));
        }}, nameScoreMapList.stream().collect(reducing));
    }

    @Test
    void testFlatMapping() {
        List<Integer> actual = Stream.iterate(0, i -> ++i).limit(3)
                .collect(Collectors.flatMapping(i -> Stream.of(i, i), Collectors.toList()));
        Assertions.assertEquals(Arrays.asList(0, 0, 1, 1, 2, 2), actual);
    }

}
