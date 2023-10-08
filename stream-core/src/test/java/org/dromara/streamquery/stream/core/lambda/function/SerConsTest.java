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
import org.dromara.streamquery.stream.core.collection.Maps;
import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author VampireAchao
 * @since 2023/1/30 10:50
 */
class SerConsTest {
  @Test
  void entryConsTest() {
    val list = Lists.of();
    Steam.of(Maps.of("foo", "bar"))
        .forEach(SerCons.entryCons((key, value) -> list.add(key + value)));
    Assertions.assertEquals("foobar", list.get(0));
  }

  @Test
  void multiTest() {
    val list = Lists.of();
    SerCons.<String>multi(list::add, t -> list.add(t + t)).accept("foo");
    Assertions.assertEquals("foo", list.get(0));
    Assertions.assertEquals("foofoo", list.get(1));
  }

  @Test
  void andThenTest() {
    val list = Lists.of();
    ((SerCons<String>) list::add).andThen(t -> list.add(t + t)).accept("foo");
    Assertions.assertEquals("foo", list.get(0));
    Assertions.assertEquals("foofoo", list.get(1));
  }

  @Test
  void acceptingTest() throws Throwable {
    val list = Lists.of();
    ((SerCons<String>) list::add).accepting("foo");
    Assertions.assertEquals("foo", list.get(0));
  }

  @Test
  void throwsTest() {
    Assertions.assertThrows(
        LambdaInvokeException.class,
        () ->
            SerCons.multi(
                    SerCons.nothing(),
                    t -> {
                      throw new Exception("test");
                    })
                .accept("foo"));
  }
}
