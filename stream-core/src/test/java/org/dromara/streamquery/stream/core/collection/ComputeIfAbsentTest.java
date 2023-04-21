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
package org.dromara.streamquery.stream.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ComputeIfAbsentTest
 *
 * @author VampireAchao
 * @since 2023/4/22
 */
class ComputeIfAbsentTest {

  static final int MAP_SIZE = 20;
  static final int THREADS = 20;
  static final ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();

  static {
    for (int i = 0; i < MAP_SIZE; i++) map.put(i, i);
  }

  static class TestThread extends Thread {
    @Override
    public void run() {
      int i = 0;
      int result = 0;
      while (result < Integer.MAX_VALUE) {
        i = (i + 1) % MAP_SIZE;
        // from https://bugs.openjdk.org/browse/JDK-8161372
        //                result += map.computeIfAbsent(i, (key) -> key + key);
        result += Maps.computeIfAbsent(map, i, (key) -> key + key);
      }
    }
  }

  @Test
  void testComputeIfAbsent() {
    if (Boolean.TRUE) {
      return;
    }
    Assertions.assertAll(
        () -> {
          ArrayList<Thread> threads = new ArrayList<>();
          for (int i = 0; i < THREADS; i++) {
            TestThread t = new TestThread();
            threads.add(t);
            t.start();
          }
          threads.get(0).join();
        });
  }
}
