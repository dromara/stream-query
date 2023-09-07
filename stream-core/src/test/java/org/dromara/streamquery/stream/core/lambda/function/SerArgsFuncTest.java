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

import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** @author VampireAchao */
public class SerArgsFuncTest {

  @Test
  void testLast() {
    Assertions.assertEquals(3, SerArgsFunc.last().apply(1, 2, 3));
  }

  @Test
  void applyingTest() throws Throwable {
    Assertions.assertEquals(
        "foo", ((SerArgsFunc<String, String>) (String... strs) -> strs[0]).applying("foo", "bar"));
  }

  @Test
  void applyingExceptionTest() {
    Assertions.assertThrows(
        LambdaInvokeException.class,
        () ->
            ((SerArgsFunc<String, String>)
                    (String... strs) -> {
                      throw new Exception("Testing");
                    })
                .apply("foo"));
  }

  @Test
  void applyWithNoArgsTest() {
    Assertions.assertNull(SerArgsFunc.<String>last().apply());
  }

  @Test
  void composeNullExceptionTest() {
    Assertions.assertThrows(
        NullPointerException.class, () -> SerArgsFunc.<String>last().compose(null));
  }

  @Test
  void composeFunctionTest() {
    Assertions.assertEquals(
        "foobaz", SerArgsFunc.last().compose(e -> e[0] + "baz").apply("foo", "bar"));
  }

  @Test
  void andThenNullExceptionTest() {
    Assertions.assertThrows(NullPointerException.class, () -> SerArgsFunc.last().andThen(null));
  }

  @Test
  void andThenTest() {
    Assertions.assertEquals(
        "bazbar", SerArgsFunc.last().andThen(e -> e[0] + "bar").apply("foo", "baz"));
  }
}
