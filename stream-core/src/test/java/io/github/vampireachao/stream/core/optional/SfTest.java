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

    private final String Z_VERIFY_NAME = "风向标";

    @Test
    void testOf() {
        Sf<String> stringSf = Sf.of(Z_VERIFY_NAME);
        Assertions.assertEquals("风向标", stringSf.get());
    }

    @Test
    void testOfColl() {
        Assertions.assertFalse(Sf.ofColl(Arrays.asList(null, null, null)).isEmpty());
    }

    @Test
    void testMayOfColl() {
        Assertions.assertTrue(Sf.mayColl(Arrays.asList(null, null, null)).isEmpty());
    }

    @Test
    void testOfStr() {
        Assertions.assertFalse(Sf.ofStr("  ").isEmpty());
    }

    @Test
    void testMayOfStr() {
        Assertions.assertTrue(Sf.mayStr("  ").isEmpty());
    }

    @Test
    void testEmpty() {
        Sf<Object> empty = Sf.empty();
        Assertions.assertNull(empty.get());
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
        Sf<Integer> let = Sf.ofStr("12").let(String::length);
        Assertions.assertEquals(2, let.get());

        Assertions.assertFalse(Sf.of(1).let(s -> null).let(Objects::isNull).let(b -> !b).get());
    }

    @Test
    void testMayLet() {
        Sf<String> stringSf = Sf.mayStr(null).mayLet(a -> a.toString().length()).let(a -> Z_VERIFY_NAME);
        Assertions.assertNull(Sf.of(null).let(s -> null).mayLet(Objects::isNull).let(b -> !b).get());
        Assertions.assertNull(stringSf.get());
    }

    @Test
    void testAlso() {
        AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
        Sf.of(null).also(a -> name.set("ZVerify"));
        Assertions.assertEquals("ZVerify", name.get());
    }

    @Test
    void testMayAlso() {
        AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
        Sf.of(null).mayAlso(a -> name.set("ZVerify"));
        Assertions.assertEquals(Z_VERIFY_NAME, name.get());
    }

    @Test
    void testTakeIf() {
        AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
        Sf.of(name).takeIf((a) -> false).mayAlso(a -> a.set("ZVerify"));
        Assertions.assertEquals(Z_VERIFY_NAME, name.get());
    }

    @Test
    void testMayTakeIf() {
        AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
        Sf.of(name).mayTakeIf((a) -> true).also(a -> a.set("ZVerify"));
        Assertions.assertEquals("ZVerify", name.get());
    }

    @Test
    void testTakeUnless() {
        AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
        Sf.of(name).takeUnless((a) -> false).mayAlso(a -> a.set("ZVerify"));
        Assertions.assertEquals("ZVerify", name.get());
    }

    @Test
    void testMayTakeUnless() {
        AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
        Sf.of(name.get()).mayTakeUnless(Objects::nonNull).mayAlso(a -> name.set("ZVerify"));
        Assertions.assertEquals(Z_VERIFY_NAME, name.get());
    }

    @Test
    void testIs() {
        AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
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
        Sf<String> or = Sf.ofStr(name.get()).or(() -> Sf.ofStr(Z_VERIFY_NAME));
        Assertions.assertEquals(Z_VERIFY_NAME, or.get());
    }

    @Test
    void testOrGet() {
        AtomicReference<String> name = new AtomicReference<>(null);
        String orGet = Sf.ofStr(name.get()).orGet(() -> Z_VERIFY_NAME);
        Assertions.assertEquals(Z_VERIFY_NAME, orGet);
    }

    @Test
    void testOrElse() {
        AtomicReference<String> name = new AtomicReference<>(null);
        String orElse = Sf.ofStr(name.get()).orElse(Z_VERIFY_NAME);
        Assertions.assertEquals(Z_VERIFY_NAME, orElse);
    }


}
