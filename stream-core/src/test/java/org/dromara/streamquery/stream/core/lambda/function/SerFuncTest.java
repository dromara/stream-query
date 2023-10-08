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

import java.util.function.Function;

/**
 * SerBiFuncTest
 *
 * @author VampireAchao Cason
 * @since 2023/5/13
 */
class SerFuncTest {
  @Test
  void entryFuncTest() {
    val first =
        Steam.of(Maps.of("foo", "bar"))
            .map(SerFunc.entryFunc((key, value) -> key + value))
            .findFirst();
    Assertions.assertTrue(first.isPresent());
    Assertions.assertEquals("foobar", first.get());
  }

  @Test
  void applyingTest() throws Throwable {
    SerFunc<String, Integer> func = String::length;
    Assertions.assertEquals(3, func.applying("foo"));
  }

  @Test
  void applyingExceptionTest() {
    SerFunc<String, Integer> func =
        s -> {
          throw new Exception("Test exception");
        };
    Assertions.assertThrows(LambdaInvokeException.class, () -> func.apply("foo"));
  }

  @Test
  void identityTest() {
    SerFunc<String, String> identity = SerFunc.identity();
    Assertions.assertEquals("foo", identity.apply("foo"));
  }

  @Test
  void castTest() {
    Function<String, Object> castFunc = SerFunc.cast();
    Assertions.assertEquals("foo", castFunc.apply("foo"));
  }
}
