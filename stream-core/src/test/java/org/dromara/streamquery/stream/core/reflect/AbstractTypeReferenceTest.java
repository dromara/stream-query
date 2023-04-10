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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author VampireAchao Cizai_
 * @since 2022/7/15 14:44
 */
class AbstractTypeReferenceTest {

  @Test
  void testGetTypeName() {
    Assertions.assertEquals(
        "java.lang.String", new AbstractTypeReference<String>() {}.getTypeName());
    Assertions.assertEquals(
        "java.util.ArrayList<java.lang.String>",
        new AbstractTypeReference<ArrayList<String>>() {}.getTypeName());
  }

  @Test
  void testGetType() {
    Assertions.assertEquals(String.class, new AbstractTypeReference<String>() {}.getType());
  }
}
