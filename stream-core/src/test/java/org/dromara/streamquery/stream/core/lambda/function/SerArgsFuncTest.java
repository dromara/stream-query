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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** @author VampireAchao */
public class SerArgsFuncTest {

  @Test
  void lastTest() {
    val last = SerArgsFunc.last().apply("foo", "bar");
    Assertions.assertEquals("bar", last);
  }

  @Test
  void composeTest() {
    val compose = SerArgsFunc.last().compose(e -> e[0] + "bar").apply("foo");
    Assertions.assertEquals("foobar", compose);
  }

  @Test
  void andThenTest() {
    val andThen = SerArgsFunc.last().andThen(e -> e[0] + "bar").apply("foo");
    Assertions.assertEquals("foobar", andThen);
  }

  @Test
  void throwsTest() {
    Assertions.assertThrows(
        LambdaInvokeException.class,
        () -> {
          SerArgsFunc.last()
              .andThen(
                  e -> {
                    throw new LambdaInvokeException("test");
                  })
              .apply("foo", "bar");
        });
  }
}
