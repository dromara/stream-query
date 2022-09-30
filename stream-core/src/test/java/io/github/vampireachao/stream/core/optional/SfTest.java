package io.github.vampireachao.stream.core.optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author VampireAchao
 * @since 2022/9/15
 */
class SfTest {

    private final String ZVERIFY_NAME = "风向标";

    @Test
    void testOf() {
        Sf<String> stringSf = Sf.of(ZVERIFY_NAME);
        Assertions.assertEquals("风向标", stringSf.value);
    }

    @Test
    void testOfColl() {

        Assertions.assertFalse(Sf.ofColl(Arrays.asList(null, null, null)).isEmpty());
    }

    @Test
    void test$OfColl() {
        Assertions.assertTrue(Sf.mayColl(Arrays.asList(null, null, null)).isEmpty());
    }

    @Test
    void testOfStr() {
        Assertions.assertFalse(Sf.ofStr("  ").isEmpty());
    }

    @Test
    void test$OfStr() {
        Assertions.assertTrue(Sf.mayStr("  ").isEmpty());
    }

    @Test
    void testEmpty() {
        Sf<Object> empty = Sf.empty();
        Assertions.assertNull(empty.value);
    }

    @Test
    void testIsEmpty() {
        Assertions.assertFalse(Sf.ofStr("  ").isEmpty());
    }

    @Test
    void testIsPresent() {
        Assertions.assertTrue(Sf.ofStr("  ").isPresent());
    }

    @Test
    void testLet() {
        Sf<Integer> let = Sf.ofStr("  ").let(String::length);
        Assertions.assertEquals(2, let.value);
    }

    @Test
    void test$Let() {
        Sf<String> stringSf = Sf.mayStr(null).mayLet(a -> a.toString().length()).let(a -> ZVERIFY_NAME);
        Assertions.assertEquals(ZVERIFY_NAME, stringSf.value);
    }

    @Test
    void testAlso() {
        AtomicReference<String> name = new AtomicReference<>(ZVERIFY_NAME);
        Sf.of(null).also(a -> name.set("ZVerify"));
        Assertions.assertEquals("ZVerify", name.get());
    }

    @Test
    void test$Also() {
        AtomicReference<String> name = new AtomicReference<>(ZVERIFY_NAME);
        Sf.of(null).mayAlso(a -> name.set("ZVerify"));
        Assertions.assertEquals(ZVERIFY_NAME, name.get());
    }

    @Test
    void testTakeIf() {
        AtomicReference<String> name = new AtomicReference<>(ZVERIFY_NAME);
        Sf.of(name).takeIf((a) -> Opp.ofStr(a.get()).isEmpty()).mayAlso(a -> a.set("ZVerify"));
        Assertions.assertEquals(ZVERIFY_NAME, name.get());
    }

    @Test
    void test$TakeIf() {
        AtomicReference<String> name = new AtomicReference<>(ZVERIFY_NAME);
        Sf.of(name).mayTakeIf((a) -> Opp.ofStr(a.get()).isPresent()).also(a -> a.set("ZVerify"));
        Assertions.assertEquals("ZVerify", name.get());
    }

    @Test
    void testTakeUnless() {
        AtomicReference<String> name = new AtomicReference<>(ZVERIFY_NAME);
        Sf.of(name).takeUnless((a) -> Opp.ofStr(a.get()).isEmpty()).mayAlso(a -> a.set("ZVerify"));
        Assertions.assertEquals("ZVerify", name.get());
    }

    @Test
    void test$TakeUnless() {
        AtomicReference<String> name = new AtomicReference<>(ZVERIFY_NAME);
        Sf.of(name.get()).mayTakeUnless(Objects::nonNull).mayAlso(a -> name.set("ZVerify"));
        Assertions.assertEquals(ZVERIFY_NAME, name.get());
    }

    @Test
    void testIs() {
        AtomicReference<String> name = new AtomicReference<>(ZVERIFY_NAME);
        Assertions.assertTrue(Sf.of(name.get()).is(Objects::nonNull));
    }

    @Test
    void testRequire() {
        AtomicReference<String> name = new AtomicReference<>(null);
        Sf<String> stringSf = Sf.ofStr(name.get());
        Assertions.assertThrows(NoSuchElementException.class, stringSf::require);
        Assertions.assertThrows(IllegalStateException.class, () -> stringSf.require(IllegalStateException::new));
    }

    @Test
    void testOrThrow() {
        AtomicReference<String> name = new AtomicReference<>(null);
        Sf<String> stringSf = Sf.ofStr(name.get());
        Assertions.assertThrows(NoSuchElementException.class, stringSf::orThrow);
        Assertions.assertThrows(IllegalStateException.class, () -> stringSf.orThrow(IllegalStateException::new));
    }



    @Test
    void testOr() {
        AtomicReference<String> name = new AtomicReference<>(null);
        Sf<String> or = Sf.ofStr(name.get()).or(() -> Sf.ofStr(ZVERIFY_NAME));
        Assertions.assertEquals(ZVERIFY_NAME, or.get());
    }

    @Test
    void testOrGet() {
        AtomicReference<String> name = new AtomicReference<>(null);
        String orGet = Sf.ofStr(name.get()).orGet(() -> ZVERIFY_NAME);
        Assertions.assertEquals(ZVERIFY_NAME, orGet);
    }

    @Test
    void testOrElse() {
        AtomicReference<String> name = new AtomicReference<>(null);
        String orElse = Sf.ofStr(name.get()).orElse(ZVERIFY_NAME);
        Assertions.assertEquals(ZVERIFY_NAME, orElse);
    }



}
