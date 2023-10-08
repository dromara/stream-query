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
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** @author VampireAchao */
public class SerBiConsTest {

  @Test
  void multiTest() {
    val list = Lists.of();
    SerBiCons.multi((f, b) -> list.add(f), (f, b) -> list.add(b)).accept("foo", "bar");
    Assertions.assertEquals("foo", list.get(0));
    Assertions.assertEquals("bar", list.get(1));
  }

  @Test
  void andThenTest() {
    val list = Lists.of();
    ((SerBiCons<String, String>) (f, b) -> list.add(f))
        .andThen((f, b) -> list.add(b))
        .accept("foo", "bar");
    Assertions.assertEquals("foo", list.get(0));
    Assertions.assertEquals("bar", list.get(1));
  }

  @Test
  void acceptingTest() throws Throwable {
    val list = Lists.of();
    ((SerBiCons<String, String>) (f, b) -> list.add(f)).accepting("foo", "bar");
    Assertions.assertEquals("foo", list.get(0));
  }

  @Test
  void nothingTest() {
    val list = Lists.of();
    SerCons.nothing().andThen(list::add).accept("foo");
    Assertions.assertEquals(1, list.size());
    Assertions.assertEquals("foo", list.get(0));
  }

  @Test
  void throwsTest() {
    Assertions.assertThrows(
        LambdaInvokeException.class,
        () ->
            SerBiCons.multi(
                    SerBiCons.nothing(),
                    (f, b) -> {
                      throw new Exception("test");
                    })
                .accept("foo", "bar"));
  }
}
