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
public class SerArgsConsTest {

  @Test
  void multiTest() {
    val list = Lists.of();
    SerArgsCons.multi(e -> list.add(e[0]), e -> list.add(e[1])).accept("foo", "bar");
    Assertions.assertEquals("foo", list.get(0));
    Assertions.assertEquals("bar", list.get(1));
  }

  @Test
  void testAcceptingAndAccept() {
    SerArgsCons<Integer> mayThrowException =
        (Integer... args) -> {
          if (args[0] == 0) throw new Exception("Zero");
        };

    Assertions.assertThrows(LambdaInvokeException.class, () -> mayThrowException.accept(0));
    Assertions.assertThrows(Exception.class, () -> mayThrowException.accepting(0));
  }

  @Test
  void testAndThen() throws Throwable {
    val addOne = (SerArgsCons<Integer>) (Integer... args) -> args[0] += 1;
    val addTwo = (SerArgsCons<Integer>) (Integer... args) -> args[0] += 2;
    val combined = addOne.andThen(addTwo);

    val testArr = new Integer[] {0};
    combined.accepting(testArr);

    Assertions.assertEquals(3, testArr[0]);
  }

  @Test
  void testNothing() throws Throwable {
    val doNothing = SerArgsCons.nothing();

    val testArr = new Integer[] {0};
    doNothing.accepting(testArr);

    Assertions.assertEquals(0, testArr[0]);
  }
}
