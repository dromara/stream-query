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
package org.dromara.streamquery.stream.core.collection;

import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.core.stream.collector.Collective;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author VampireAchao
 * @since 2022/10/21 16:48
 */
class MapsTest {

  @Test
  void testOf() {
    Assertions.assertEquals("value", Maps.of("key", "value").get("key"));
    Assertions.assertEquals("value1", Maps.of("key", "value", "key1", "value1").get("key1"));
    Assertions.assertEquals(
        "value2", Maps.of("key", "value", "key1", "value1", "key2", "value2").get("key2"));
  }

  @Test
  void testEntry() {
    final Map.Entry<String, String> entry = Maps.entry("key", "value");
    Assertions.assertEquals("key", entry.getKey());
    Assertions.assertEquals("value", entry.getValue());
  }

  @Test
  void testOneToManyToOne() {
    final Map<String, List<String>> map =
        Maps.oneToManyToOne(
                new HashMap<String, Collection<String>>() {
                  {
                    put("key", Arrays.asList("value", null));
                  }
                },
                new HashMap<String, String>() {
                  {
                    put("value", "Good");
                  }
                },
                Steam::nonNull)
            .collect(Collective.entryToMap());
    Assertions.assertEquals(1, map.get("key").size());
    Assertions.assertEquals("Good", map.get("key").stream().findFirst().get());
  }

  @Test
  void testOneToOneToOne() {
    final Map<String, String> map =
        Maps.oneToOneToOne(
                new HashMap<String, String>() {
                  {
                    put("key", "value");
                  }
                },
                new HashMap<String, String>() {
                  {
                    put("value", "Good");
                  }
                },
                String::toUpperCase)
            .collect(Collective.entryToMap());
    Assertions.assertEquals("GOOD", map.get("key"));
    final Map<String, String> treeMap =
        Steam.of(Maps.entry("GOOD", ""), Maps.entry("BAD", ""))
            .collect(Collective.entryToMap(LinkedHashMap::new));
    Assertions.assertEquals("GOOD", Steam.of(treeMap.keySet()).findFirst().orElse(null));
  }

  @Test
  void testOneToOneToMany() {
    final Map<String, List<String>> map =
        Maps.oneToOneToMany(
                new HashMap<String, String>() {
                  {
                    put("key", "value");
                  }
                },
                new HashMap<String, List<String>>() {
                  {
                    put("value", Arrays.asList("Good", "Bad"));
                  }
                },
                Steam::nonNull)
            .collect(Collective.entryToMap());
    Assertions.assertEquals(2, map.get("key").size());
    Assertions.assertEquals("Good", map.get("key").get(0));
    Assertions.assertEquals("Bad", map.get("key").get(1));
  }

  @Test
  public void testMerge() {
    final Map<String, String> map = Maps.of("key", "value", "key1", "value1");
    final Map<String, String> mergeMap = Maps.of("key", "Value", "key2", "value2");
    final Map<String, String> merge = Maps.merge(map, mergeMap, (v1, v2) -> v1 + v2);
    Assertions.assertEquals("valueValue", merge.get("key"));
    Assertions.assertEquals("value1", merge.get("key1"));
    Assertions.assertEquals("value2", merge.get("key2"));
  }

  @Test
  public void filterByValue() {
    Map<String, Integer> map1 = new HashMap<>();
    map1.put("a", 1);
    map1.put("b", 2);
    map1.put("c", 3);
    Map<String, Integer> result = Maps.filterByValue(map1, value -> value > 2);
    Assertions.assertEquals(Maps.of("c", 3), result);

    Map<String, List<Integer>> map2 = new HashMap<>();
    map2.put("a", Arrays.asList(1, 2, 3));
    map2.put("b", Arrays.asList(4, 5, 6));
    map2.put("c", Arrays.asList(7, 8, 9));
    Map<String, List<Integer>> result2 = Maps.filterByValue(map2, data -> data.contains(3));
    Assertions.assertEquals(result2, Maps.of("a", Lists.of(1, 2, 3)));
  }

  @Test
  public void flatten() {
    Map<String, Map<String, Integer>> nestedMap = new HashMap<>();
    Map<String, Integer> innerMap1 = new HashMap<>();
    innerMap1.put("a", 1);
    innerMap1.put("b", 2);
    nestedMap.put("key1", innerMap1);

    Map<String, Integer> innerMap2 = new HashMap<>();
    innerMap2.put("c", 3);
    nestedMap.put("key2", innerMap2);
    final Map<String, Integer> flatten = Maps.flatten(nestedMap, "_");
    Assertions.assertEquals(flatten, Maps.of("key1_a", 1, "key1_b", 2, "key2_c", 3));
  }
}
