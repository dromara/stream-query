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
import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** @author VampireAchao */
public class SerArgsPredTest {

  @Test
  void multiAndTest() {
    val result =
        SerArgsPred.multiAnd(e -> e[0].equals("foo"), e -> e[1].equals("bar")).test("foo", "bar");
    Assertions.assertTrue(result);
  }

  @Test
  void multiOrTest() {
    val result =
        SerArgsPred.multiOr(e -> !e[0].equals("foo"), e -> !e[1].equals("bar")).test("foo", "bar");
    Assertions.assertFalse(result);
  }

  @Test
  void isEqualTest() {
    val result = SerArgsPred.isEqual("foo", "foo").test("foo", "foo");
    Assertions.assertTrue(result);
    Assertions.assertTrue(SerArgsPred.isEqual(Opp.empty().get()).test(Opp.empty().get()));
    Assertions.assertFalse(SerArgsPred.isEqual("foo").test(Opp.empty().get()));
  }

  @Test
  void negateTest() {
    val result = SerArgsPred.isEqual("foo").negate().test("foo");
    Assertions.assertFalse(result);
  }

  @Test
  void throwsTest() {
    Assertions.assertThrows(
        LambdaInvokeException.class,
        () -> {
          SerArgsPred.isEqual("foo")
              .or(
                  e -> {
                    throw new Exception("test");
                  })
              .test("bar");
        });
  }
}
