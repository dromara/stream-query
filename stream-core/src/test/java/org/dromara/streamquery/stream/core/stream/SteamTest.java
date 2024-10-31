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
package org.dromara.streamquery.stream.core.stream;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.dromara.streamquery.stream.core.collection.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * @author VampireAchao Cizai_
 * @since 2022/7/19 14:14
 */
class SteamTest {

  @Test
  void testBuilder() {
    List<Integer> list = Steam.<Integer>builder().add(1).add(2).add(3).build().toList();
    Assertions.assertEquals(asList(1, 2, 3), list);
  }

  @Test
  void testOf() {
    Assertions.assertEquals(3, Steam.of(asList(1, 2, 3), true).count());
    Assertions.assertEquals(3, Steam.of(1, 2, 3).count());
    Assertions.assertEquals(3, Steam.of(Stream.builder().add(1).add(2).add(3).build()).count());
    Assertions.assertEquals(0, Steam.of(Maps.of()).count());
  }

  @Test
  void testSplit() {
    List<Integer> list = Steam.split("1,2,3", ",").map(Integer::valueOf).toList();
    Assertions.assertEquals(asList(1, 2, 3), list);
  }

  @Test
  void testIterator() {
    List<Integer> list = Steam.iterate(0, i -> i < 3, i -> ++i).toList();
    Assertions.assertEquals(asList(0, 1, 2), list);
  }

  @Test
  void testToCollection() {
    List<Integer> list = asList(1, 2, 3);
    List<String> toCollection = Steam.of(list).map(String::valueOf).toCollection(LinkedList::new);
    Assertions.assertEquals(asList("1", "2", "3"), toCollection);
  }

  @Test
  void testToList() {
    List<Integer> list = asList(1, 2, 3);
    List<String> toList = Steam.of(list).map(String::valueOf).toList();
    Assertions.assertEquals(asList("1", "2", "3"), toList);
  }

  @Test
  void testToUnmodifiableList() {
    List<Integer> list = Steam.of(1, 2, 3).toUnmodifiableList();
    Assertions.assertThrows(UnsupportedOperationException.class, () -> list.remove(0));
  }

  @Test
  void testToSet() {
    List<Integer> list = asList(1, 2, 3);
    Set<String> toSet = Steam.of(list).map(String::valueOf).toSet();
    Assertions.assertEquals(new HashSet<>(asList("1", "2", "3")), toSet);
  }

  @Test
  void testToUnmodifiableSet() {
    Set<Integer> set = Steam.of(1, 2, 3).toUnmodifiableSet();
    Assertions.assertThrows(UnsupportedOperationException.class, () -> set.remove(0));
  }

  @Test
  void testToZip() {
    List<Integer> orders = asList(1, 2, 3);
    List<String> list = asList("dromara", "hutool", "sweet");
    Map<Integer, String> toZip = Steam.of(orders).toZip(list);
    Assertions.assertEquals(
        new HashMap<Integer, String>() {
          {
            put(1, "dromara");
            put(2, "hutool");
            put(3, "sweet");
          }
        },
        toZip);
  }

  @Test
  void testJoin() {
    List<Integer> list = asList(1, 2, 3);
    String joining = Steam.of(list).join();
    Assertions.assertEquals("123", joining);
    Assertions.assertEquals("1,2,3", Steam.of(list).join(","));
    Assertions.assertEquals("(1,2,3)", Steam.of(list).join(",", "(", ")"));
  }

  @Test
  void testToMap() {
    List<Integer> list = asList(1, 2, 3);
    Map<String, Integer> identityMap = Steam.of(list).toMap(String::valueOf);
    Assertions.assertEquals(
        new HashMap<String, Integer>() {
          {
            put("1", 1);
            put("2", 2);
            put("3", 3);
          }
        },
        identityMap);
  }

  @Test
  void testToUnmodifiableMap() {
    Map<Integer, Integer> map1 =
        Steam.of(1, 2, 3).toUnmodifiableMap(Function.identity(), Function.identity());
    Assertions.assertThrows(UnsupportedOperationException.class, () -> map1.remove(1));
    Map<Integer, Integer> map2 =
        Steam.of(1, 2, 3)
            .toUnmodifiableMap(Function.identity(), Function.identity(), (t1, t2) -> t1);
    Assertions.assertThrows(UnsupportedOperationException.class, () -> map2.remove(1));
  }

  @Test
  void testGroup() {
    List<Integer> list = asList(1, 2, 3);
    Map<String, List<Integer>> group = Steam.of(list).group(String::valueOf);
    Assertions.assertEquals(
        new HashMap<String, List<Integer>>() {
          {
            put("1", singletonList(1));
            put("2", singletonList(2));
            put("3", singletonList(3));
          }
        },
        group);
  }

  @Test
  void testPartition() {
    // 是否为奇数
    Predicate<Integer> predicate = t -> (t & 1) == 1;

    Map<Boolean, List<Integer>> map =
        Steam.of(1, 1, 2, 3).partition(predicate, Collectors.toList());
    Assertions.assertEquals(3, map.get(Boolean.TRUE).size());
    Assertions.assertEquals(1, map.get(Boolean.FALSE).size());

    map = Steam.of(1, 1, 2, 3).partition(predicate);
    Assertions.assertEquals(3, map.get(Boolean.TRUE).size());
    Assertions.assertEquals(1, map.get(Boolean.FALSE).size());

    Map<Boolean, Set<Integer>> map2 = Steam.of(1, 1, 2, 3).partition(predicate, HashSet::new);
    Assertions.assertEquals(2, map2.get(Boolean.TRUE).size());
    Assertions.assertEquals(1, map2.get(Boolean.FALSE).size());
  }

  @Test
  void testGenerate() {
    Random random = new SecureRandom();
    Steam<Integer> limit = Steam.generate(() -> random.nextInt(10)).limit(10);
    Assertions.assertEquals(Boolean.TRUE, limit.allMatch(v -> v >= 0 && v < 10));
  }

  @Test
  void testFilterIter() {
    List<Student> list =
        asList(
            Student.builder().name("臧臧").age(23).build(),
            Student.builder().name("阿超").age(21).build());
    List<Student> others =
        asList(Student.builder().age(22).build(), Student.builder().age(23).build());
    List<Student> students = Steam.of(list).filterContains(Student::getAge, others).toList();
    Assertions.assertEquals(singletonList(Student.builder().name("臧臧").age(23).build()), students);
  }

  @Data
  @Builder
  private static class Student {
    private String name;
    private Integer age;
    private Long id;

    @Tolerate
    public Student() {
      // this is an accessible parameterless constructor.
    }
  }

  @Test
  void testMapIdx() {
    List<String> list = asList("dromara", "hutool", "sweet");
    List<String> mapIndex = Steam.of(list).parallel().mapIdx((e, i) -> i + 1 + "." + e).toList();
    Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
  }

  @Test
  void testMapMulti() {
    List<Integer> list = asList(1, 2, 3);
    List<Integer> mapMulti =
        Steam.of(list)
            .<Integer>mapMulti(
                (e, buffer) -> {
                  if (e % 2 == 0) {
                    buffer.accept(e);
                  }
                  buffer.accept(e);
                })
            .toList();
    Assertions.assertEquals(asList(1, 2, 2, 3), mapMulti);
  }

  @Test
  void testDistinct() {
    List<Integer> list = asList(1, 2, 2, 3);
    List<Integer> distinctBy = Steam.of(list).distinct(String::valueOf).toList();
    Assertions.assertEquals(asList(1, 2, 3), distinctBy);
  }

  @Test
  void testForeachIdx() {
    List<String> list = asList("dromara", "hutool", "sweet");
    Steam.Builder<String> builder = Steam.builder();
    Steam.of(list).parallel().forEachIdx((e, i) -> builder.accept(i + 1 + "." + e));
    Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
  }

  @Test
  void testForEachOrderedIdx() {
    List<String> list = asList("dromara", "hutool", "sweet");
    Steam.Builder<String> builder = Steam.builder();
    Steam.of(list).forEachOrderedIdx((e, i) -> builder.accept(i + 1 + "." + e));
    Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
  }

  @Test
  void testFlatIdx() {
    List<String> list = asList("dromara", "hutool", "sweet");
    List<String> mapIndex =
        Steam.of(list).parallel().flatIdx((e, i) -> Steam.of(i + 1 + "." + e)).toList();
    Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
  }

  @Test
  void testFlatIter() {
    List<Integer> list = asList(1, 2, 3);
    List<Integer> flatMapIter = Steam.of(list).<Integer>flat(e -> null).toList();
    Assertions.assertEquals(Collections.emptyList(), flatMapIter);
  }

  @Test
  void testFilter() {
    List<Integer> list = asList(1, 2, 3);
    List<Integer> filterIndex = Steam.of(list).filter(String::valueOf, "1").toList();
    Assertions.assertEquals(singletonList(1), filterIndex);
  }

  @Test
  void testFilterIdx() {
    List<String> list = asList("dromara", "hutool", "sweet");
    List<String> filterIndex = Steam.of(list, true).filterIdx((e, i) -> i < 2).toList();
    Assertions.assertEquals(asList("dromara", "hutool"), filterIndex);
  }

  @Test
  void testNonNull() {
    List<Integer> list = asList(1, null, 2, 3);
    List<Integer> nonNull = Steam.of(list).nonNull().toList();
    Assertions.assertEquals(asList(1, 2, 3), nonNull);
  }

  @Test
  void testNonNullMapping() {
    List<Student> original =
        asList(
            Student.builder().name("臧臧").age(23).build(),
            Student.builder().name("阿超").age(21).build());
    List<Student> nonNull =
        Steam.of(original)
            .push(Student.builder() /*.name(null)*/.age(21).build())
            .unshift(Student.builder().name("阿郎") /*.age(null)*/.build())
            .nonNull(Student::getName)
            .nonNull(Student::getAge)
            .toList();
    Assertions.assertEquals(original, nonNull);
  }

  @Test
  void testPush() {
    List<Integer> list = asList(1, 2);
    List<Integer> push = Steam.of(list).push(3).toList();
    Assertions.assertEquals(asList(1, 2, 3), push);

    List<Integer> lastPust = Steam.of(push).push(2, 1).toList();
    Assertions.assertEquals(asList(1, 2, 3, 2, 1), lastPust);
  }

  @Test
  void testParallel() {
    Assertions.assertTrue(Steam.of(1, 2, 3).parallel(true).isParallel());
    Assertions.assertFalse(Steam.of(1, 2, 3).parallel(false).isParallel());
  }

  @Test
  void testUnshift() {
    List<Integer> list = asList(2, 3);
    List<Integer> unshift = Steam.of(list).unshift(1).toList();
    Assertions.assertEquals(asList(1, 2, 3), unshift);

    List<Integer> lastUnshift = Steam.of(unshift).unshift(3, 2).toList();
    Assertions.assertEquals(asList(3, 2, 1, 2, 3), lastUnshift);
  }

  @Test
  void testAt() {
    List<Integer> list = asList(1, 2, 3);
    Assertions.assertEquals(1, Steam.of(list).at(0).orElse(null));
    Assertions.assertEquals(2, Steam.of(list).at(1).orElse(null));
    Assertions.assertEquals(1, Steam.of(list).at(-3).orElse(null));
    Assertions.assertEquals(3, Steam.of(list).at(-1).orElse(null));
    Assertions.assertFalse(Steam.of(list).at(-4).isPresent());
  }

  @Test
  void testSplice() {
    List<Integer> list = asList(1, 2, 3);
    Assertions.assertEquals(asList(1, 2, 2, 3), Steam.of(list).splice(1, 0, 2).toList());
    Assertions.assertEquals(asList(1, 2, 3, 3), Steam.of(list).splice(3, 1, 3).toList());
    Assertions.assertEquals(asList(1, 2, 4), Steam.of(list).splice(2, 1, 4).toList());
    Assertions.assertEquals(asList(1, 2), Steam.of(list).splice(2, 1).toList());
    Assertions.assertEquals(asList(1, 2, 3), Steam.of(list).splice(2, 0).toList());
    Assertions.assertEquals(asList(1, 2), Steam.of(list).splice(-1, 1).toList());
    Assertions.assertEquals(asList(1, 2, 3), Steam.of(list).splice(-2, 2, 2, 3).toList());
  }

  @Test
  void testFindFirst() {
    List<Integer> list = asList(1, 2, 3);
    Optional<Integer> find = Steam.of(list).findFirst(Objects::nonNull);
    Assertions.assertEquals(1, find.orElse(null));
  }

  @Test
  void testFindFirstIdx() {
    List<Integer> list = asList(null, 2, 3);
    Integer idx = Steam.of(list).findFirstIdx(Objects::nonNull);
    Assertions.assertEquals(1, idx);
    Assertions.assertNotEquals(-1, Steam.of(list).parallel().findFirstIdx(Objects::nonNull));
  }

  @Test
  void testFindLast() {
    List<Integer> list = asList(1, null, 3);
    Optional<Integer> find = Steam.of(list).findLast(Objects::nonNull);
    Assertions.assertEquals(3, find.orElse(null));
    Assertions.assertEquals(3, Steam.of(list).findLast().orElse(null));
  }

  @Test
  void testFindLastIdx() {
    List<Integer> list = asList(1, null, 3);
    Integer idx = Steam.of(list).findLastIdx(Objects::nonNull);
    Assertions.assertEquals(2, idx);
    Assertions.assertNotEquals(-1, Steam.of(list).parallel().findLastIdx(Objects::nonNull));
  }

  @Test
  void testReverse() {
    List<Integer> list = asList(1, 3, 2);
    List<Integer> reverse = Steam.of(list).reverseSorted(Comparator.naturalOrder()).toList();
    Assertions.assertEquals(asList(3, 2, 1), reverse);
  }

  @Test
  void testZip() {
    List<Integer> orders = asList(1, 2, 3);
    List<String> list = asList("dromara", "hutool", "sweet");
    List<String> zip = Steam.of(orders).zip(list, (e1, e2) -> e1 + "." + e2).toList();
    Assertions.assertEquals(asList("1.dromara", "2.hutool", "3.sweet"), zip);
  }

  @Test
  void testListSplit() {
    List<Integer> list = asList(1, 2, 3, 4, 5);
    List<List<Integer>> lists = Steam.of(list).split(2).map(Steam::toList).toList();
    Assertions.assertEquals(asList(asList(1, 2), asList(3, 4), singletonList(5)), lists);
  }

  @Test
  void testSplitList() {
    List<Integer> list = asList(1, 2, 3, 4, 5);
    List<List<Integer>> lists = Steam.of(list).splitList(2).toList();
    Assertions.assertEquals(asList(asList(1, 2), asList(3, 4), singletonList(5)), lists);
  }

  @Test
  void testLog() {
    List<Integer> list = asList(0, 1, 2);
    Assertions.assertEquals(asList(1, 2, 3), Steam.of(list).map(i -> i + 1).log().toList());
  }

  @Test
  void testChunk() {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    // 测试正常分组
    List<List<Integer>> chunks = Steam.of(list).chunk(2).toList();
    Assertions.assertEquals(
        Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4), Arrays.asList(5)), chunks);

    // 测试size为1的情况
    chunks = Steam.of(list).chunk(1).toList();
    Assertions.assertEquals(
        Arrays.asList(
            Arrays.asList(1),
            Arrays.asList(2),
            Arrays.asList(3),
            Arrays.asList(4),
            Arrays.asList(5)),
        chunks);

    // 测试size大于列表长度的情况
    chunks = Steam.of(list).chunk(10).toList();
    Assertions.assertEquals(Arrays.asList(Arrays.asList(1, 2, 3, 4, 5)), chunks);

    // 测试非法参数
    Assertions.assertThrows(IllegalArgumentException.class, () -> Steam.of(list).chunk(0).toList());
  }

  @Test
  public void testCycle() {
    // 测试普通循环
    List<Integer> result = Steam.of(1, 2, 3).cycle().limit(8).toList();
    Assertions.assertEquals(Arrays.asList(1, 2, 3, 1, 2, 3, 1, 2), result);

    // 测试空流
    List<Integer> emptyResult = Steam.<Integer>empty().cycle().limit(5).toList();
    Assertions.assertTrue(emptyResult.isEmpty());

    // 测试单个元素
    List<String> singleResult = Steam.of("a").cycle().limit(3).toList();
    Assertions.assertEquals(Arrays.asList("a", "a", "a"), singleResult);

    // 测试不限制长度时是否能正常获取前几个元素
    List<Integer> unlimitedResult =
        Steam.of(1, 2)
            .cycle()
            .peek(
                i -> {
                  // 确保流确实在运行,但不会无限运行
                  Assertions.assertTrue(i <= 2);
                })
            .limit(4)
            .toList();
    Assertions.assertEquals(Arrays.asList(1, 2, 1, 2), unlimitedResult);
  }

  @Test
  void testSliding() {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    // 测试正常滑动窗口
    List<List<Integer>> windows = Steam.of(list).sliding(3, 1).toList();
    Assertions.assertEquals(
        Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 4), Arrays.asList(3, 4, 5)),
        windows);

    // 测试步长为2的滑动窗口
    windows = Steam.of(list).sliding(2, 2).toList();
    Assertions.assertEquals(Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4)), windows);

    // 测试非法参数
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> Steam.of(list).sliding(0, 1).toList());
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> Steam.of(list).sliding(1, 0).toList());
  }

  @Test
  void testDistinctOptimized() {
    List<Integer> list = Arrays.asList(1, 2, 2, 3, 3, 3, 1);

    // 测试顺序流去重
    List<Integer> result = Steam.of(list).distinct(String::valueOf).toList();
    Assertions.assertEquals(Arrays.asList(1, 2, 3), result);

    // 测试并行流去重
    result = Steam.of(list).parallel().distinct(String::valueOf).toList();
    Assertions.assertEquals(3, result.size());
    Assertions.assertTrue(result.containsAll(Arrays.asList(1, 2, 3)));
  }
}
