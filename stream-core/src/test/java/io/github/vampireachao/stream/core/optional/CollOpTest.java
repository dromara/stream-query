package io.github.vampireachao.stream.core.optional;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;

/**
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/10 8:26
 **/
class CollOpTest {
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
        private Boolean matchParent;
    }


    List<Student> list = asList(
            Student.builder().name("臧臧").age(23).build(),
            Student.builder().name("阿超").age(21).build());


    @Test
    void of(){
        List<Object> list = Collections.emptyList();
        CollOp<Object> of = CollOp.of(list);
        Assertions.assertTrue(of.isEmpty());
    }

    @Test
    void testIsPresent(){
        Assertions.assertTrue(CollOp.of(list).isPresent());
    }

    @Test
    void testIsEqual(){
        List<Student> eList = asList(
                Student.builder().name("臧臧").age(23).build(),
                Student.builder().name("阿超").age(21).build());
        Assertions.assertTrue(CollOp.of(list).isEqual(eList));
    }

    @Test
    void testFilter(){
        CollOp<Student> collOp = CollOp.of(list).filter((a) -> a.getAge() == 21);
        Assertions.assertEquals(1, collOp.get().size());
    }

    @Test
    void testElseM(){
        List<String> list = Collections.emptyList();
        Collection<String> orElse = CollOp.of(list).orElse(Collections.singletonList("a"));
        Assertions.assertEquals(1, orElse.size());
        Collection<String> orElseGet = CollOp.of(list).orElseGet(() -> Collections.singletonList("a"));
        Assertions.assertEquals(1, orElseGet.size());
    }

    @Test
    void testOrElseThrow(){
        CollOp<String> of = CollOp.of(Collections.emptyList());
        // 获取一个不可能为空的值，否则抛出NoSuchElementException异常
        Assertions.assertThrows(NoSuchElementException.class, of::orElseThrow);
        // 获取一个不可能为空的值，否则抛出自定义异常
        Assertions.assertThrows(IllegalStateException.class,
                () -> of.orElseThrow(IllegalStateException::new));
    }

    @Test
    void testMap(){
        CollOp<Integer> map = CollOp.of(list).filter(a -> a.getAge()==21).map(Student::getAge);
        map.steam().forEach(a-> Assertions.assertEquals(a,21));
    }


    @Test
    void testIfPresent() {
        AtomicInteger size = new AtomicInteger(0);
        CollOp.of(list).ifPresent(a -> size.set(a.size()));
        Assertions.assertEquals(size.get(),2);
    }

    @Test
    void testOr() {
        List<Student> eList = asList(
                Student.builder().name("臧臧").age(23).build(),
                Student.builder().name("阿超").age(21).build());
        CollOp<Object> op = CollOp.of(Collections.singletonList(null)).or(() -> CollOp.of(Collections.singleton(eList)));
        Assertions.assertTrue(op.isEqual(Collections.singleton(list)));

    }




}
