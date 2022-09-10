package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * StrOpTest
 *
 * @author ZVerify
 * @since 2022/9/5 11:34
 **/
class StrOpTest {

    @Test
    void testOf() {
        String of = StrOp.of("  ").get();
        Assertions.assertNull(of);
    }

    @Test
    void testEmpty() {
        String s = StrOp.empty().get();
        Assertions.assertNull(s);
    }

    @Test
    void testMap() {
        Op<Integer> zVerify = StrOp.of("ZVerify").map(String::length);
        Assertions.assertEquals(7, zVerify.get());
    }

    @Test
    void testMapToThrow() {
        ThrowOp<Integer> zVerify = StrOp.of("").mapToThrow(String::length);
        Assertions.assertNull(zVerify.getException());
    }

    @Test
    void testMapToColl() {
        CollOp<String> zVerify = StrOp.of("ZVerify").mapToColl(Arrays::asList);
        Assertions.assertNotNull(zVerify.get());
    }

    @Test
    void testFlatMap() {
        Op<String> stringOp = StrOp.of("ZVerify").flatMap(a -> Op.of(a.substring(1, 3)));
        Assertions.assertEquals("Ve", stringOp.get());
    }

    @Test
    void testFlatMapToOptional() {
        Optional<String> stringOptional = StrOp.of("ZVerify").flatMapToOptional(a -> Optional.of(a.substring(1, 3)));
        Assertions.assertEquals("Ve", stringOptional.get());
    }

    @Test
    void testFilter() {
        StrOp zVerify = StrOp.of("ZVerify").filter(s -> s.length() > 10);
        Assertions.assertNull(zVerify.get());
    }

    @Test
    void testFilterEqual() {
        StrOp zVerify = StrOp.of("ZVerify").filterEqual("zz");
        Assertions.assertNull(zVerify.get());
    }

    @Test
    void testIfPresent() {
        StrOp.of("ZVerify").ifPresent(a -> Assertions.assertEquals("zverify", a.toLowerCase(Locale.ROOT)));
    }

    @Test
    void testOr() {
        final String str = StrOp.of("   ").or(() -> StrOp.of("Hello Stream-Query!")).map(String::toUpperCase).orElseThrow();
        Assertions.assertEquals("HELLO STREAM-QUERY!", str);

    }

    @Test
    void testSteam() {
        Steam<String> zVerify = StrOp.of("ZVerify").steam();
        List<String> list = zVerify.toList();
        Assertions.assertEquals(Collections.singletonList("ZVerify"), list);
    }

}
