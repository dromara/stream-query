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

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author VampireAchao Cizai_
 * @since 2022/12/26 18:01
 */
public class AsyncConfig {

  private AsyncInterceptor interceptor = new AsyncInterceptor() {};

  private int timeout = -1;

  private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

  private Executor executor =
      ForkJoinPool.getCommonPoolParallelism() > 1
          ? ForkJoinPool.commonPool()
          : r -> new Thread(r).start();

  public static AsyncConfig create() {
    return new AsyncConfig();
  }

  public AsyncInterceptor getInterceptor() {
    return interceptor;
  }

  public void setInterceptor(AsyncInterceptor interceptor) {
    this.interceptor = interceptor;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public TimeUnit getTimeUnit() {
    return timeUnit;
  }

  public void setTimeUnit(TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
  }

  public Executor getExecutor() {
    return executor;
  }

  public void setExecutor(Executor executor) {
    this.executor = executor;
  }
}
