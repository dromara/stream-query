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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.tx;

import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.tx.annotation.DsTxEventListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

/**
 * DsTxEventListener Factory
 *
 * @author zp
 */
public class DsTxEventListenerFactory implements EventListenerFactory, Ordered {
  private static final int EVENT_ORDER = 100;

  @Override
  public boolean supportsMethod(Method method) {
    return AnnotatedElementUtils.hasAnnotation(method, DsTxEventListener.class);
  }

  @Override
  public ApplicationListener<?> createApplicationListener(
      String beanName, Class<?> type, Method method) {
    return new DsTxListenerMethodAdapter(beanName, type, method);
  }

  @Override
  public int getOrder() {
    return EVENT_ORDER;
  }
}
