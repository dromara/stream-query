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
package org.dromara.streamquery.stream.core.optional;

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
    Sf<String> stringSf =
        Sf.mayStr(null).mayLet(a -> a.toString().length()).let(a -> Z_VERIFY_NAME);
    Sf<Integer> isNotNull = Sf.mayStr(Z_VERIFY_NAME).mayLet(String::length);

    Assertions.assertNull(stringSf.get());
    Assertions.assertEquals(Z_VERIFY_NAME.length(), isNotNull.get());
  }

  @Test
  void testAlso() {
    AtomicReference<String> name = new AtomicReference<>(Z_VERIFY_NAME);
    Sf.of(null).also(a -> name.set("ZVerify"));
    Sf.of(null).mayAlso(a -> name.set("ZVerify")).also(a -> name.set("CiZai"));
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
    Sf<String> isNotNullStr =
        Sf.of(name.get()).mayTakeUnless(Objects::nonNull).mayAlso(a -> name.set("ZVerify"));

    name.set("   ");
    Sf<String> isNullStr =
        Sf.mayStr(name.get()).mayTakeUnless(Objects::isNull).mayAlso(a -> name.set("ZVerify"));
    Assertions.assertNull(isNotNullStr.get());
    Assertions.assertNull(isNullStr.get());
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

    name.set(Z_VERIFY_NAME);
    Sf<String> sfNotNull = Sf.ofStr(name.get());
    Assertions.assertThrows(NoSuchElementException.class, stringSf::require);
    Assertions.assertThrows(
        IllegalStateException.class, () -> stringSf.require(IllegalStateException::new));
    Assertions.assertEquals(Z_VERIFY_NAME, sfNotNull.require(IllegalStateException::new).get());
  }

  @Test
  void testOrThrow() {
    AtomicReference<String> name = new AtomicReference<>(null);
    Sf<String> stringSf = Sf.ofStr(name.get());
    name.set(Z_VERIFY_NAME);
    Sf<String> sfNotNull = Sf.ofStr(name.get());

    Assertions.assertThrows(NoSuchElementException.class, stringSf::orThrow);
    Assertions.assertThrows(
        IllegalStateException.class, () -> stringSf.orThrow(IllegalStateException::new));
    Assertions.assertEquals(Z_VERIFY_NAME, sfNotNull.orThrow(IllegalStateException::new));
  }

  @Test
  void testOr() {
    AtomicReference<String> name = new AtomicReference<>(null);
    Sf<String> or = Sf.ofStr(name.get()).or(() -> Sf.ofStr(Z_VERIFY_NAME));
    Sf<String> isThis = Sf.ofStr(Z_VERIFY_NAME).or(() -> Sf.ofStr("NULL"));
    Assertions.assertEquals(Z_VERIFY_NAME, or.get());
    Assertions.assertEquals(Z_VERIFY_NAME, isThis.get());
  }

  @Test
  void testOrGet() {
    AtomicReference<String> name = new AtomicReference<>(null);
    String orGet = Sf.ofStr(name.get()).orGet(() -> Z_VERIFY_NAME);
    String isNotNull = Sf.ofStr(Z_VERIFY_NAME).orGet(() -> Z_VERIFY_NAME + Z_VERIFY_NAME);
    Assertions.assertEquals(Z_VERIFY_NAME, orGet);
    Assertions.assertEquals(Z_VERIFY_NAME, isNotNull);
  }

  @Test
  void testOrElse() {
    AtomicReference<String> name = new AtomicReference<>(null);
    String orElse = Sf.ofStr(name.get()).orElse(Z_VERIFY_NAME);
    String isNotNull = Sf.ofStr(Z_VERIFY_NAME).orElse(Z_VERIFY_NAME + Z_VERIFY_NAME);
    Assertions.assertEquals(Z_VERIFY_NAME, orElse);
    Assertions.assertEquals(Z_VERIFY_NAME, isNotNull);
  }

  @Test
  void testOrRun() {
    AtomicReference<String> isNullStr = new AtomicReference<>();
    AtomicReference<String> isNotNullStr = new AtomicReference<>();
    Sf.mayStr("   ").orRun(() -> isNullStr.set(Z_VERIFY_NAME));
    Sf.mayStr(Z_VERIFY_NAME).orRun(() -> isNotNullStr.set(" " + Z_VERIFY_NAME));

    Assertions.assertEquals(isNullStr.get(), Z_VERIFY_NAME);
    Assertions.assertNull(isNotNullStr.get());
  }

  @Test
  void testRecover() {
    // 测试非激活状态的恢复
    Sf<String> empty = Sf.empty();
    Assertions.assertEquals(
        "recovered", empty.recover(e -> "recovered").get(), "Should recover from empty Sf");

    // 测试激活状态不需要恢复
    Sf<String> active = Sf.of("active");
    Assertions.assertEquals(
        "active", active.recover(e -> "recovered").get(), "Should not recover when Sf is active");

    // 测试恢复处理器抛出异常的情况
    Sf<String> recovered =
        empty.recover(
            e -> {
              throw new RuntimeException("handler error");
            });
    Assertions.assertTrue(
        recovered.isEmpty(), "Should return empty Sf when handler throws exception");

    // 测试不同类型的异常处理
    Sf<String> handledByType =
        empty.recover(
            e -> {
              if (e instanceof NoSuchElementException) {
                return "no element";
              }
              return "other error";
            });
    Assertions.assertEquals(
        "no element", handledByType.get(), "Should handle NoSuchElementException correctly");

    // 测试 null 处理器
    Assertions.assertThrows(
        NullPointerException.class,
        () -> empty.recover(null),
        "Should throw NPE when handler is null");

    // 测试链式调用
    String result =
        Sf.<String>empty().recover(e -> "first").mayLet(String::toUpperCase).orElse("default");
    Assertions.assertEquals("FIRST", result, "Should work in method chain");
  }
}
