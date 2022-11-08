package io.github.vampireachao.stream.core.collector;

import io.github.vampireachao.stream.core.stream.Steam;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.vampireachao.stream.core.collector.Collective.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Collectors测试
 *
 * @author VampireAchao ZVerify
 * @since 2022/6/22 11:10
 */
class CollectiveTest {

    @Test
    void testToMap() {
        Assertions.assertNull(Stream.of(null, null, null).collect(toMap(Object::hashCode, Object::hashCode)).get(null));
    }

    @Test
    void testGroupingBy() {
        Assertions.assertArrayEquals(new Object[]{}, Stream.of(null, null, null).parallel()
                .collect(groupingBy(Object::hashCode)).get(null).toArray());

        Map<? extends Class<?>, Integer> group = Steam.of(null, null, null).group(Object::getClass, summingInt(Object::hashCode));
        Assertions.assertFalse(group.isEmpty());

        Assertions.assertFalse(Steam.of(null, 1).parallel().group(Object::hashCode, mapping(Function.identity(), toList())).isEmpty());
    }

    @Test
    void testFlatMapping() {
        List<Integer> actual = Stream.iterate(0, i -> ++i).limit(3)
                .collect(flatMapping(i -> Stream.of(i, i), toList()));
        Assertions.assertEquals(Arrays.asList(0, 0, 1, 1, 2, 2), actual);
    }

    @Test
    void testFlatMappingIter() {
        List<Integer> actual = Stream.iterate(0, i -> ++i).limit(3)
                .collect(flatMappingIter(i -> Steam.of(i, i), toList()));
        Assertions.assertEquals(Arrays.asList(0, 0, 1, 1, 2, 2), actual);
    }

    @Test
    void testTransform() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4)
                .collect(Collective.transform(Steam::of));
        Assertions.assertEquals(Steam.class, stream.getClass());

        stream = Stream.of(1, 2, 3, 4)
                .collect(Collective.transform(HashSet::new, Steam::of));
        Assertions.assertEquals(Steam.class, stream.getClass());
    }

    @Test
    void testToEasyStream() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4)
                .collect(Collective.toSteam());
        Assertions.assertEquals(Steam.class, stream.getClass());
    }

    @Test
    void testToEntryStream() {
        Map<String, Integer> map = Stream.of(1, 2, 3, 4, 5)
                // 转为EntryStream
                .collect(Collective.toEntrySteam(Function.identity(), String::valueOf))
                // 过滤偶数
                .filterByKey(k -> (k & 1) == 1)
                .inverse()
                .toMap();
        Assertions.assertEquals((Integer) 1, map.get("1"));
        Assertions.assertEquals((Integer) 3, map.get("3"));
        Assertions.assertEquals((Integer) 5, map.get("5"));
    }

    @Test
    void testToCollection() {
        HashSet<Integer> list = Steam.of(1, 2, 3, 4, 5).parallel()
                .collect(Collective.toCollection(HashSet::new));
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
        String list = Steam.of(1, 2, 3, 4, 5).map(String::valueOf).parallel().collect(Collective.joining());
        Assertions.assertEquals("12345", list);

        Assertions.assertEquals("1,2,3,4,5", Steam.of(1, 2, 3, 4, 5).map(String::valueOf).parallel()
                .collect(Collective.joining(",")));
    }

    @Test
    void testFiltering() {
        final Map<Integer, Long> map = Stream.of(1, 2, 3)
                .collect(Collectors.groupingBy(Function.identity(),
                        Collective.filtering(i -> i > 1, Collectors.counting())
                ));
        Assertions.assertEquals(new HashMap<Integer, Long>() {
            {
                put(1, 0L);
                put(2, 1L);
                put(3, 1L);
            }
        }, map);
    }
    @Test
    void testToTree() {
        Consumer<Object> test = o -> {
            List< Student> studentTree = Steam
                    .of(
                             Student.builder().id(1L).name("dromara").build(),
                             Student.builder().id(2L).name("baomidou").build(),
                             Student.builder().id(3L).name("hutool").parentId(1L).build(),
                             Student.builder().id(4L).name("sa-token").parentId(1L).build(),
                             Student.builder().id(5L).name("mybatis-plus").parentId(2L).build(),
                             Student.builder().id(6L).name("looly").parentId(3L).build(),
                             Student.builder().id(7L).name("click33").parentId(4L).build(),
                             Student.builder().id(8L).name("jobob").parentId(5L).build()
                    )
                    .collect(Collective.toTree( Student::getId,  Student::getParentId,  Student::setChildren,Boolean.TRUE));;
            Assertions.assertEquals(asList(
                     Student.builder().id(1L).name("dromara")
                            .children(asList(
                                     Student.builder().id(3L).name("hutool").parentId(1L)
                                            .children(singletonList( Student.builder().id(6L).name("looly").parentId(3L).build()))
                                            .build(),
                                     Student.builder().id(4L).name("sa-token").parentId(1L)
                                            .children(singletonList( Student.builder().id(7L).name("click33").parentId(4L).build()))
                                            .build()))
                            .build(),
                     Student.builder().id(2L).name("baomidou")
                            .children(singletonList(
                                     Student.builder().id(5L).name("mybatis-plus").parentId(2L)
                                            .children(singletonList(
                                                     Student.builder().id(8L).name("jobob").parentId(5L).build()
                                            ))
                                            .build()))
                            .build()
            ), studentTree);
        };
        test = test.andThen(o -> {
            List< Student> studentTree = Steam
                    .of(
                             Student.builder().id(1L).name("dromara").matchParent(true).build(),
                             Student.builder().id(2L).name("baomidou").matchParent(true).build(),
                             Student.builder().id(3L).name("hutool").parentId(1L).build(),
                             Student.builder().id(4L).name("sa-token").parentId(1L).build(),
                             Student.builder().id(5L).name("mybatis-plus").parentId(2L).build(),
                             Student.builder().id(6L).name("looly").parentId(3L).build(),
                             Student.builder().id(7L).name("click33").parentId(4L).build(),
                             Student.builder().id(8L).name("jobob").parentId(5L).build()
                    )
                    .collect(Collective.toTree(Student::getId,  Student::getParentId,  Student::setChildren,  Student::getMatchParent, Boolean.TRUE));
            Assertions.assertEquals(asList(
                     Student.builder().id(1L).name("dromara").matchParent(true)
                            .children(asList(
                                     Student.builder().id(3L).name("hutool").parentId(1L)
                                            .children(singletonList( Student.builder().id(6L).name("looly").parentId(3L).build()))
                                            .build(),
                                     Student.builder().id(4L).name("sa-token").parentId(1L)
                                            .children(singletonList( Student.builder().id(7L).name("click33").parentId(4L).build()))
                                            .build()))
                            .build(),
                     Student.builder().id(2L).name("baomidou").matchParent(true)
                            .children(singletonList(
                                     Student.builder().id(5L).name("mybatis-plus").parentId(2L)
                                            .children(singletonList(
                                                     Student.builder().id(8L).name("jobob").parentId(5L).build()
                                            ))
                                            .build()))
                            .build()
            ), studentTree);
        });
    }

    @Data
    @Builder
    public static class Student {
        @Tolerate
        public Student() {
            // this is an accessible parameterless constructor.
        }

        private String name;
        private Integer age;
        private Long id;
        private Long parentId;
        private List<Student> children;
        private Boolean matchParent;
    }

}
