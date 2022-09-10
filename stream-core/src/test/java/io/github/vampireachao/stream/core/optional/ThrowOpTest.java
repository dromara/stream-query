package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @Description: ThrowOpTest
 * @DateTime: 2022/9/9 16:55
 **/
class ThrowOpTest {
    List<String> list = Collections.singletonList("臧臧");
    @Test
    void testOf() {

        ThrowOp<String> of = ThrowOp.of(() -> list.get(0));
        ThrowOp<String> ofTry = ThrowOp.of(() -> list.get(1));
        Assertions.assertEquals("臧臧",of.get());
        Assertions.assertNull(of.getException());
        Assertions.assertTrue(ofTry.getException() instanceof IndexOutOfBoundsException);
    }

    @Test
    void testIsPresent() {
        ThrowOp<String> of = ThrowOp.of(() -> list.get(0));
        ThrowOp<String> ofTry = ThrowOp.of(() -> list.get(1));
        Assertions.assertTrue(ofTry.isPresent());
        Assertions.assertFalse(of.isPresent());
    }

    @Test
    void testIsEmpty(){
        ThrowOp<String> of = ThrowOp.of(() -> list.get(0));
        ThrowOp<String> ofTry = ThrowOp.of(() -> list.get(1));
        Assertions.assertFalse(ofTry.isEmpty());
        Assertions.assertTrue(of.isEmpty());
    }

    @Test
    void testIsEqual(){
        ThrowOp<String> of = ThrowOp.of(() -> list.get(0));
        Assertions.assertTrue(of.isEqual("臧臧"));
    }

    @Test
    void testOrElseM(){
        ThrowOp<String> of = ThrowOp.of(() -> list.get(1));
        AtomicInteger zz = new AtomicInteger(0);
        String orElse = of.orElse("臧臧");
        String orElseGet = of.orElseGet(() -> "臧臧");
        of.orElseRun(() -> zz.set(12));
        Assertions.assertEquals("臧臧", orElse);
        Assertions.assertEquals("臧臧", orElseGet);
        Assertions.assertEquals(12, zz.get());
    }

    @Test
    void testOrElseThrow(){
        ThrowOp<String> of = ThrowOp.of(() -> list.get(1));
        // 获取一个不可能为空的值，否则抛出NoSuchElementException异常
        Assertions.assertThrows(NoSuchElementException.class, of::orElseThrow);
        // 获取一个不可能为空的值，否则抛出自定义异常
        Assertions.assertThrows(IllegalStateException.class,
                () -> of.orElseThrow(IllegalStateException::new));
    }

    @Test
    void testFlatMapToOptional() {
        Optional<String> stringOptional = ThrowOp.of(() -> "ZVerify").flatMapToOptional(a -> Optional.of(a.substring(1, 3)));
        Assertions.assertEquals("Ve", stringOptional.get());
    }

    @Test
    void testMap() {
        Op<Integer> zVerify = ThrowOp.of(() -> "ZVerify").map(String::length);
        Assertions.assertEquals(7, zVerify.get());
    }

    @Test
    void testFlatMap() {
        Op<String> stringOp = ThrowOp.of(() -> "ZVerify").flatMap(a -> Op.of(a.substring(1, 3)));
        Assertions.assertEquals("Ve", stringOp.get());
    }


    @Test
    void testFilter() {
        ThrowOp<String> zVerify = ThrowOp.of(() -> "ZVerify").filter(s -> s.length() > 10);
        Assertions.assertNull(zVerify.get());
    }

    @Test
    void testFilterEqual() {
        ThrowOp<String> zVerify = ThrowOp.of(() -> "ZVerify").filterEqual("zz");
        Assertions.assertNull(zVerify.get());
    }

    @Test
    void testIfPresent() {
        ThrowOp.of(() -> "ZVerify").ifPresent(a -> Assertions.assertEquals("zverify", a.toLowerCase(Locale.ROOT)));
    }

    @Test
    void testOr() {
        final String str = ThrowOp.of(()->"   ").or(() -> ThrowOp.of(() -> "Hello Stream-Query!")).map(String::toUpperCase).orElseThrow();
        Assertions.assertEquals("HELLO STREAM-QUERY!", str);

    }

    @Test
    void testSteam() {
        Steam<String> zVerify = ThrowOp.of(() ->"ZVerify").steam();
        List<String> list = zVerify.toList();
        Assertions.assertEquals(Collections.singletonList("ZVerify"), list);
    }
}
