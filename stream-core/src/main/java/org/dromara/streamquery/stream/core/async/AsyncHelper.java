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

import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.lambda.function.SerSupp;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author VampireAchao
 * @since 2022/12/26 16:58
 */
public class AsyncHelper {

  private static final AsyncConfig DEFAULT_CONFIG = AsyncConfig.create();

  private AsyncHelper() {
    /* Do not new me! */
  }

  @SafeVarargs
  public static <T> List<T> supply(SerSupp<T>... suppliers) {
    return supply(DEFAULT_CONFIG, suppliers);
  }

  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T> List<T> supply(AsyncConfig asyncConfig, SerSupp<T>... suppliers) {
    final AsyncInterceptor interceptor = asyncConfig.getInterceptor();
    interceptor.before();
    final CompletableFuture<T>[] futures =
        Steam.of(suppliers)
            .map(
                supplier ->
                    CompletableFuture.supplyAsync(
                        () -> interceptor.execute(supplier), asyncConfig.getExecutor()))
            .toArray(CompletableFuture[]::new);
    final CompletableFuture<Void> exceptionally =
        CompletableFuture.allOf(futures).exceptionally(interceptor::onError);
    ((SerSupp<?>)
            () ->
                asyncConfig.getTimeout() == -1
                    ? exceptionally.get()
                    : exceptionally.get(asyncConfig.getTimeout(), asyncConfig.getTimeUnit()))
        .get();
    interceptor.after();
    return Steam.of(futures)
        .map((SerFunc<CompletableFuture<T>, T>) CompletableFuture::get)
        .toList();
  }
}
