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
package org.dromara.streamquery.stream.core.lambda.function;

import lombok.val;
import org.dromara.streamquery.stream.core.collection.Maps;
import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author VampireAchao
 * @since 2023/5/14 13:03
 */
class SerPredTest {

  @Test
  void entryPredTest() {
    val first =
        Steam.of(Maps.of("foo", "bar"))
            .findFirst(SerPred.entryPred((key, value) -> key.equals("foo") && value.equals("bar")));
    Assertions.assertTrue(first.isPresent());
  }

  @Test
  void isEqualTest() {
    val isEqualToFooOrBar = SerPred.isEqual("foo");
    Assertions.assertTrue(isEqualToFooOrBar.test("foo"));
    Assertions.assertFalse(isEqualToFooOrBar.test("bar"));
  }

  @Test
  void testingTest() throws Throwable {
    SerPred<String> isLengthThree = s -> s.length() == 3;
    Assertions.assertTrue(isLengthThree.testing("foo"));
    Assertions.assertFalse(isLengthThree.testing("fo"));
  }

  @Test
  void testingExceptionTest() {
    SerPred<String> throwException =
        s -> {
          throw new Exception("Test exception");
        };
    Assertions.assertThrows(LambdaInvokeException.class, () -> throwException.test("foo"));
  }

  @Test
  void multiAndTest() {
    SerPred<String> combined = SerPred.multiAnd(s -> s.length() == 3, s -> s.startsWith("f"));

    Assertions.assertTrue(combined.test("foo"));
    Assertions.assertFalse(combined.test("fo"));
    Assertions.assertFalse(combined.test("booo"));
  }

  @Test
  void multiOrTest() {
    SerPred<String> combined = SerPred.multiOr(s -> s.length() == 2, s -> s.startsWith("b"));

    Assertions.assertTrue(combined.test("fo"));
    Assertions.assertTrue(combined.test("boo"));
    Assertions.assertFalse(combined.test("foo"));
  }

  @Test
  void andOrNegateTest() {
    SerPred<String> isLengthThree = s -> s.length() == 3;
    SerPred<String> startsWithF = s -> s.startsWith("f");

    Assertions.assertTrue(isLengthThree.and(startsWithF).test("foo"));
    Assertions.assertTrue(isLengthThree.or(startsWithF).test("fo"));
    Assertions.assertFalse(isLengthThree.negate().test("foo"));
  }
}
