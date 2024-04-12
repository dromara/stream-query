/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    Assertions.assertNull(
        Stream.of(null, null, null).collect(toMap(Object::hashCode, Object::hashCode)).get(null));
  }

  @Test
  void testGroupingBy() {
    Assertions.assertArrayEquals(
        new Object[] {},
        Stream.of(null, null, null)
            .parallel()
            .collect(groupingBy(Object::hashCode))
            .get(null)
            .toArray());

    Map<? extends Class<?>, Integer> group =
        Steam.of(null, null, null).group(Object::getClass, summingInt(Object::hashCode));
    Assertions.assertFalse(group.isEmpty());

    Assertions.assertFalse(
        Steam.of(null, 1)
            .parallel()
            .group(Object::hashCode, mapping(Function.identity(), toList()))
            .isEmpty());
  }

  @Test
  void testSubList() {
    List<Integer> list = Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2);
    Map<Integer, List<Integer>> map1 = list.stream().collect(groupingBy(Function.identity(), subList(1, 2)));
    Assertions.assertEquals(3, map1.size());
    Assertions.assertEquals(2, map1.get(0).size());
    Assertions.assertEquals(2, map1.get(1).size());
    Assertions.assertEquals(2, map1.get(2).size());

    Map<Integer, List<Integer>> map2 = list.stream().collect(groupingBy(Function.identity(), subList(10, 2)));
    Assertions.assertEquals(3, map2.size());
    Assertions.assertEquals(0, map2.get(0).size());
    Assertions.assertEquals(0, map2.get(1).size());
    Assertions.assertEquals(0, map2.get(2).size());



    Assertions.assertThrows(, () -> {
      list.stream().collect(groupingBy(Function.identity(), subList(1, 0)));
    });
  }

  @Test
  void testFlatMapping() {
    List<Integer> actual =
        Stream.iterate(0, i -> ++i).limit(3).collect(flatMapping(i -> Stream.of(i, i), toList()));
    Assertions.assertEquals(Arrays.asList(0, 0, 1, 1, 2, 2), actual);
  }

  @Test
  void testFlatMappingIter() {
    List<Integer> actual =
        Stream.iterate(0, i -> ++i)
            .limit(3)
            .collect(flatMappingIter(i -> Steam.of(i, i), toList()));
    Assertions.assertEquals(Arrays.asList(0, 0, 1, 1, 2, 2), actual);
  }

  @Test
  void testTransform() {
    Stream<Integer> stream = Stream.of(1, 2, 3, 4).collect(Collective.transform(Steam::of));
    Assertions.assertEquals(Steam.class, stream.getClass());

    stream = Stream.of(1, 2, 3, 4).collect(Collective.transform(HashSet::new, Steam::of));
    Assertions.assertEquals(Steam.class, stream.getClass());
  }

  @Test
  void testToEasyStream() {
    Stream<Integer> stream = Stream.of(1, 2, 3, 4).collect(Collective.toSteam());
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
    HashSet<Integer> list =
        Steam.of(1, 2, 3, 4, 5).parallel().collect(Collective.toCollection(HashSet::new));
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
    String list =
        Steam.of(1, 2, 3, 4, 5).map(String::valueOf).parallel().collect(Collective.joining());
    Assertions.assertEquals("12345", list);

    Assertions.assertEquals(
        "1,2,3,4,5",
        Steam.of(1, 2, 3, 4, 5).map(String::valueOf).parallel().collect(Collective.joining(",")));
  }

  @Test
  void testFiltering() {
    final Map<Integer, Long> map =
        Stream.of(1, 2, 3)
            .collect(
                Collectors.groupingBy(
                    Function.identity(), Collective.filtering(i -> i > 1, Collectors.counting())));
    Assertions.assertEquals(
        new HashMap<Integer, Long>() {
          {
            put(1, 0L);
            put(2, 1L);
            put(3, 1L);
          }
        },
        map);
  }
}
