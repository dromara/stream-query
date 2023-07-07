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
package org.dromara.streamquery.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author VampireAchao
 * @since 2023/7/7 16:34
 */
public abstract class InterceptorTest {

  @BeforeEach
  void init(@Autowired SqlSessionFactory sessionFactory) {
    sessionFactory
        .getConfiguration()
        .getInterceptors()
        .forEach(
            interceptor -> {
              if (interceptor instanceof MybatisPlusInterceptor) {
                List<InnerInterceptor> interceptors =
                    ReflectHelper.getFieldValue(interceptor, "interceptors");
                interceptors.clear();
                interceptors.addAll(interceptors());
              }
            });
  }

  @AfterEach
  void clear(@Autowired SqlSessionFactory sessionFactory) {
    sessionFactory
        .getConfiguration()
        .getInterceptors()
        .forEach(
            interceptor -> {
              if (interceptor instanceof MybatisPlusInterceptor) {
                List<InnerInterceptor> interceptors =
                    ReflectHelper.getFieldValue(interceptor, "interceptors");
                interceptors.clear();
              }
            });
  }

  public abstract List<InnerInterceptor> interceptors();
}
