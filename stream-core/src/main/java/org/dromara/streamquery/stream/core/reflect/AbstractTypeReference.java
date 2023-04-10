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
package org.dromara.streamquery.stream.core.reflect;

import java.lang.reflect.Type;

/**
 * 单个泛型类型
 *
 * @author VampireAchao Cizai_
 * @since 2022/6/2 18:53
 */
public abstract class AbstractTypeReference<T> implements Type {

  /**
   * Returns a string describing this type, including information about any type parameters.
   *
   * @implSpec The default implementation calls {@code toString}.
   * @since 1.8
   */
  @Override
  public String getTypeName() {
    return ReflectHelper.getGenericTypes(this.getClass())[0].getTypeName();
  }

  /**
   * getType.
   *
   * @return a {@link java.lang.reflect.Type} object
   */
  public Type getType() {
    return ReflectHelper.getGenericTypes(this.getClass())[0];
  }
}
