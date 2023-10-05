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
package org.dromara.streamquery.stream.core.async;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * AsyncHelperTest
 *
 * @author VampireAchao
 * @since 2022/12/26
 */
class AsyncHelperTest {

  @Test
  void testSupply() {
    List<Integer> list = AsyncHelper.supply(() -> 1, () -> 2, () -> 3);
    Assertions.assertEquals(Lists.of(1, 2, 3), list);
  }

  @Test
  @Disabled("sleep")
  void testTimeout() {
    final AsyncConfig asyncConfig = AsyncConfig.create();
    asyncConfig.setTimeout(1);
    asyncConfig.setTimeUnit(TimeUnit.SECONDS);
    Assertions.assertThrows(
        TimeoutException.class,
        () -> {
          try {
            AsyncHelper.supply(
                asyncConfig,
                () -> {
                  TimeUnit.SECONDS.sleep(2);
                  return null;
                });
          } catch (LambdaInvokeException e) {
            throw e.getCause();
          }
        });
  }

  @Test
  void testInterceptor() {
    ThreadLocal<Integer> threadLocal = new TransmittableThreadLocal<>();
    AsyncConfig asyncConfig = AsyncConfig.create();
    asyncConfig.setInterceptor(
        new AsyncInterceptor() {
          Object capture;

          @Override
          public void before() {
            threadLocal.set(1);
            capture = TransmittableThreadLocal.Transmitter.capture();
          }

          @Override
          public <T> T execute(Supplier<T> supplier) {
            return TransmittableThreadLocal.Transmitter.runSupplierWithCaptured(capture, supplier);
          }

          @Override
          public void after() {
            threadLocal.remove();
          }

          @Override
          public <T> T onError(Throwable throwable) {
            return AsyncInterceptor.super.onError(throwable);
          }
        });
    final Integer result = AsyncHelper.supply(asyncConfig, threadLocal::get).get(0);
    Assertions.assertEquals(1, result);
  }
}
