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

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * 可序列化的Runnable
 *
 * @author VampireAchao Cizai_
 * @see java.lang.Runnable
 */
@FunctionalInterface
public interface SerRunn extends Runnable, Serializable {

  /**
   * When an object implementing interface <code>Runnable</code> is used to create a thread,
   * starting the thread causes the object's <code>run</code> method to be called in that separately
   * executing thread.
   *
   * <p>The general contract of the method <code>run</code> is that it may take any action
   * whatsoever.
   *
   * @throws java.lang.Exception if any.
   * @see Thread#run()
   */
  @SuppressWarnings("all")
  void running() throws Throwable;

  /**
   * When an object implementing interface <code>Runnable</code> is used to create a thread,
   * starting the thread causes the object's <code>run</code> method to be called in that separately
   * executing thread.
   *
   * <p>The general contract of the method <code>run</code> is that it may take any action
   * whatsoever.
   *
   * @see Thread#run()
   */
  @Override
  default void run() {
    try {
      running();
    } catch (Throwable e) {
      throw new LambdaInvokeException(e);
    }
  }

  /**
   * multi
   *
   * @param serRunns lambda
   * @return lambda
   */
  static SerRunn multi(SerRunn... serRunns) {
    return () -> Stream.of(serRunns).forEach(SerRunn::run);
  }
}
