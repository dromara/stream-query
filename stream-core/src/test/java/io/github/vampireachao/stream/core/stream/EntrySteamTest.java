package io.github.vampireachao.stream.core.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * test for {@link EntrySteam}
 *
 * @author huangchengxing 
 * @date 2022/8/29 16:44
 */
class EntrySteamTest {

	@Test
	void testOf() {
		Map<String, String> map = new HashMap<>();
		map.put("1", "1");
		Assertions.assertEquals(1, EntrySteam.of(map).count());
		Assertions.assertEquals(1, EntrySteam.of(map.entrySet()).count());
		Assertions.assertEquals(1, EntrySteam.of(map.entrySet().stream()).count());
		Assertions.assertEquals(1, EntrySteam.of(map.values(), Function.identity(), Function.identity()).count());
	}

	@Test
	void testEmpty() {
		Assertions.assertEquals(0, EntrySteam.empty().count());
	}

	@Test
	void testDistinctByKey() {
		long count = EntrySteam.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.distinctByKey()
			.count();
		Assertions.assertEquals(2, count);
	}

	@Test
	void testDistinctByValue() {
		long count = EntrySteam.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.distinctByValue()
			.count();
		Assertions.assertEquals(2, count);
	}

	@Test
	void testFilter() {
		long count = EntrySteam.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.filter((k, v) -> k == 1 && v == 1)
			.count();
		Assertions.assertEquals(1, count);
	}

	@Test
	void testFilterByKey() {
		long count = EntrySteam.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.filterByKey(k -> k == 1)
			.count();
		Assertions.assertEquals(2, count);
	}

	@Test
	void testFilterByValue() {
		long count = EntrySteam.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.filterByValue(v -> v == 1)
			.count();
		Assertions.assertEquals(2, count);
	}

	@Test
	void testPeekKey() {
		List<Integer> keys = new ArrayList<>();
		EntrySteam.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.peekKey(keys::add)
			.count();
		Assertions.assertEquals(Arrays.asList(1, 1, 2, 2), keys);
	}

	@Test
	void testPeekValue() {
		List<Integer> values = new ArrayList<>();
		EntrySteam.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.peekValue(values::add)
			.count();
		Assertions.assertEquals(Arrays.asList(1, 2, 1, 2), values);
	}

	@Test
	void testSortByKey() {
		List<Map.Entry<Integer, Integer>> entries = EntrySteam.of(Arrays.asList(new Entry<>(3, 1), new Entry<>(2, 1), new Entry<>(4, 1), new Entry<>(1, 1)))
			.sortByKey(Comparator.comparingInt(Integer::intValue))
			.collect(Collectors.toList());
		Assertions.assertEquals(
			Arrays.asList(1, 2, 3, 4),
			entries.stream().map(Map.Entry::getKey).collect(Collectors.toList())
		);
	}

	@Test
	void testSortByValue() {
		List<Map.Entry<Integer, Integer>> entries = EntrySteam.of(Arrays.asList(new Entry<>(4, 4), new Entry<>(2, 2), new Entry<>(1, 1), new Entry<>(3, 3)))
			.sortByValue(Comparator.comparingInt(Integer::intValue))
			.collect(Collectors.toList());
		Assertions.assertEquals(
			Arrays.asList(1, 2, 3, 4),
			entries.stream().map(Map.Entry::getValue).collect(Collectors.toList())
		);
	}

	@Test
	void testToValueStream() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assertions.assertEquals(
			new ArrayList<>(map.values()), EntrySteam.of(map).toValueStream().collect(Collectors.toList())
		);
	}

	@Test
	void testToKeyStream() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assertions.assertEquals(
			new ArrayList<>(map.keySet()), EntrySteam.of(map).toKeyStream().collect(Collectors.toList())
		);
	}

	@Test
	void testCollectKey() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		List<Integer> keys = EntrySteam.of(map).collectKeys(Collectors.toList());
		Assertions.assertEquals(new ArrayList<>(map.keySet()), keys);
	}

	@Test
	void testCollectValue() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		List<Integer> keys = EntrySteam.of(map).collectValues(Collectors.toList());
		Assertions.assertEquals(new ArrayList<>(map.keySet()), keys);
	}

	@Test
	void testMapKeys() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assertions.assertEquals(
			Arrays.asList("1", "2", "3"),
			EntrySteam.of(map)
				.mapKeys(String::valueOf)
				.toKeyStream()
				.collect(Collectors.toList())
		);
	}

	@Test
	void testMapValues() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assertions.assertEquals(
			Arrays.asList("1", "2", "3"),
			EntrySteam.of(map)
				.mapValues(String::valueOf)
				.toValueStream()
				.collect(Collectors.toList())
		);
	}

	@Test
	void testMap() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assertions.assertEquals(
			Arrays.asList("11", "22", "33"),
			EntrySteam.of(map)
				.map((k, v) -> k.toString() + v.toString())
				.collect(Collectors.toList())
		);
	}

	@Test
	void testFlatMap() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		List<Integer> list = EntrySteam.of(map)
			.flatMap(e -> Stream.of(e.getKey(), e.getKey() + 1))
			.collect(Collectors.toList());
		Assertions.assertEquals(Arrays.asList(1, 2, 2, 3, 3, 4), list);
	}

	@Test
	void testFlatMapValue() {
		Map<String, Integer> map = new HashMap<>();
		map.put("class1", 1);
		map.put("class2", 2);
		map.put("class3", 3);
		List<String> values = EntrySteam.of(map)
			.flatMapKey(k -> Stream.of(k + "'s student1", k + "'s student2"))
			.map((k, v) -> v + "=" + k)
			.sorted()
			.collect(Collectors.toList());
		Assertions.assertEquals(
			Arrays.asList(
				"1=class1's student1", "1=class1's student2",
				"2=class2's student1", "2=class2's student2",
				"3=class3's student1", "3=class3's student2"
			),
			values
		);
	}

	@Test
	void testInverse() {
		Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");
		List<String> results = EntrySteam.of(map)
			.inverse()
			.map((k, v) -> k + "=" + v)
			.collect(Collectors.toList());
		Assertions.assertEquals(
			Arrays.asList("value1=key1", "value2=key2", "value3=key3"),
			results
		);
	}

	@Test
	void testFlatMapKey() {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "class1");
		map.put(2, "class2");
		map.put(3, "class3");
		List<String> values = EntrySteam.of(map)
			.flatMapValue(v -> Stream.of(v + "'s student1", v + "'s student2"))
			.map((k, v) -> k + "=" + v)
			.collect(Collectors.toList());
		Assertions.assertEquals(
			Arrays.asList(
				"1=class1's student1", "1=class1's student2",
				"2=class2's student1", "2=class2's student2",
				"3=class3's student1", "3=class3's student2"
			),
			values
		);
	}

	@Test
	void testForEach() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);

		List<Integer> keys = new ArrayList<>();
		List<Integer> values = new ArrayList<>();
		EntrySteam.of(map).forEach((k ,v) -> {
			keys.add(k);
			values.add(v);
		});
		Assertions.assertEquals(Arrays.asList(1, 2, 3), keys);
		Assertions.assertEquals(Arrays.asList(1, 2, 3), values);
	}

	@Test
	void testToMap() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);

		Map<Integer, Integer> result = EntrySteam.of(map).toMap();
		Assertions.assertEquals(map, result);

		result = EntrySteam.of(map).toMap(LinkedHashMap::new);
		Assertions.assertEquals(new LinkedHashMap<>(map), result);

		result = EntrySteam.of(map).toMap(LinkedHashMap::new, (t1, t2) -> t1);
		Assertions.assertEquals(new LinkedHashMap<>(map), result);
	}

	@Test
	void testToTable() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);

		// 按是否偶数分组
		Map<Boolean, Map<Integer, Integer>> table = EntrySteam.of(map).toTable(
			(k ,v) -> (k & 1) == 0, HashMap::new, (t1, t2) -> t1
		);
		Assertions.assertEquals((Integer)1, table.get(false).get(1));
		Assertions.assertEquals((Integer)3, table.get(false).get(3));
		Assertions.assertEquals((Integer)2, table.get(true).get(2));
		Assertions.assertEquals((Integer)4, table.get(true).get(4));

		table = EntrySteam.of(map).toTable((k ,v) -> (k & 1) == 0);
		Assertions.assertEquals((Integer)1, table.get(false).get(1));
		Assertions.assertEquals((Integer)3, table.get(false).get(3));
		Assertions.assertEquals((Integer)2, table.get(true).get(2));
		Assertions.assertEquals((Integer)4, table.get(true).get(4));
	}

	@Test
	void testToTableByKey() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);

		// 按是否偶数分组
		Map<Boolean, Map<Integer, Integer>> table = EntrySteam.of(map).toTableByKey(
			k -> (k & 1) == 0, HashMap::new, (t1, t2) -> t1
		);
		Assertions.assertEquals((Integer)1, table.get(false).get(1));
		Assertions.assertEquals((Integer)3, table.get(false).get(3));
		Assertions.assertEquals((Integer)2, table.get(true).get(2));
		Assertions.assertEquals((Integer)4, table.get(true).get(4));

		table = EntrySteam.of(map).toTableByKey(k -> (k & 1) == 0);
		Assertions.assertEquals((Integer)1, table.get(false).get(1));
		Assertions.assertEquals((Integer)3, table.get(false).get(3));
		Assertions.assertEquals((Integer)2, table.get(true).get(2));
		Assertions.assertEquals((Integer)4, table.get(true).get(4));
	}

	@Test
	void testToTableByValue() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);

		// 按是否偶数分组
		Map<Boolean, Map<Integer, Integer>> table = EntrySteam.of(map).toTableByValue(
			v -> (v & 1) == 0, HashMap::new, (t1, t2) -> t1
		);
		Assertions.assertEquals((Integer)1, table.get(false).get(1));
		Assertions.assertEquals((Integer)3, table.get(false).get(3));
		Assertions.assertEquals((Integer)2, table.get(true).get(2));
		Assertions.assertEquals((Integer)4, table.get(true).get(4));

		table = EntrySteam.of(map).toTableByValue(v -> (v & 1) == 0);
		Assertions.assertEquals((Integer)1, table.get(false).get(1));
		Assertions.assertEquals((Integer)3, table.get(false).get(3));
		Assertions.assertEquals((Integer)2, table.get(true).get(2));
		Assertions.assertEquals((Integer)4, table.get(true).get(4));
	}

	@Test
	void testGroupByKey() {
		Map<Integer, List<Integer>> map1 = EntrySteam.of(Arrays.asList(1, 1, 2, 2), Function.identity(), Function.identity())
			.groupByKey();
		Assertions.assertEquals(2, map1.size());
		Assertions.assertEquals(Arrays.asList(1, 1), map1.get(1));
		Assertions.assertEquals(Arrays.asList(2, 2), map1.get(2));

		Map<Integer, Set<Integer>> map2 = EntrySteam.of(Arrays.asList(1, 1, 2, 2), Function.identity(), Function.identity())
			.groupByKey(Collectors.toSet());
		Assertions.assertEquals(2, map2.size());
		Assertions.assertEquals(Collections.singleton(1), map2.get(1));
		Assertions.assertEquals(Collections.singleton(2), map2.get(2));

		Map<Integer, Set<Integer>> map3 = EntrySteam.of(Arrays.asList(1, 1, 2, 2), Function.identity(), Function.identity())
			.groupByKey(LinkedHashMap::new, Collectors.toSet());
		Assertions.assertEquals(2, map3.size());
		Assertions.assertEquals(Collections.singleton(1), map3.get(1));
		Assertions.assertEquals(Collections.singleton(2), map3.get(2));
	}

	private static class Entry<K, V> implements Map.Entry<K, V> {

		private final K key;
		private final V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			return null;
		}
	}

}
