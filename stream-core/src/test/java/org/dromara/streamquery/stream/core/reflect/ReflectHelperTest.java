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

import org.dromara.streamquery.stream.core.lambda.LambdaExecutable;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author VampireAchao Cizai_
 * @since 2022/6/7 13:49
 */
class ReflectHelperTest {

  @Test
  void testGetDescriptor() {
    Assertions.assertEquals(
        "()V",
        ReflectHelper.getDescriptor(
            LambdaHelper.resolve((Serializable & Runnable) () -> {}).getExecutable()));
    Assertions.assertEquals("Ljava/lang/Void;", ReflectHelper.getDescriptor(Void.class));
    Assertions.assertEquals("Z", ReflectHelper.getDescriptor(boolean.class));
    Assertions.assertEquals("Ljava/lang/Boolean;", ReflectHelper.getDescriptor(Boolean.class));
  }

  private static class StringArrayList extends ArrayList<String> {
    private static final long serialVersionUID = 5735314375293577082L;
  }

  @Test
  void testGetGenericTypes() {
    Type[] stringType =
        ReflectHelper.getGenericTypes(new AbstractTypeReference<String>() {}.getClass());
    Assertions.assertEquals(String.class, stringType[0]);
    Type[] stringArrayListType = ReflectHelper.getGenericTypes(StringArrayList.class);
    Assertions.assertEquals(String.class, stringArrayListType[0]);
    Type[] hashMapType = ReflectHelper.getGenericTypes(new HashMap<String, Object>() {}.getClass());
    Assertions.assertEquals(String.class, hashMapType[0]);
    Assertions.assertEquals(Object.class, hashMapType[1]);
  }

  @Test
  void testGetGenericMap() {
    final Map<String, Type> genericMap =
        ReflectHelper.getGenericMap(
            new HashMap<String, TreeMap<String, Object>>() {
              private static final long serialVersionUID = -3694145972588238697L;
            }.getClass());
    Assertions.assertEquals(String.class, genericMap.get("K"));
    Type vType = genericMap.get("V");
    if (vType instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) vType;
      Assertions.assertEquals(TreeMap.class, pt.getRawType());
      Assertions.assertEquals(String.class, pt.getActualTypeArguments()[0]);
      Assertions.assertEquals(Object.class, pt.getActualTypeArguments()[1]);
    } else {
      Assertions.fail("Expected V type to be ParameterizedType");
    }
  }

  @Test
  void testIsInstance() {
    Assertions.assertTrue(
        ReflectHelper.isInstance(
            Collections.singletonMap(1, ""), new AbstractTypeReference<Map<?, ?>>() {}.getClass()));
    Assertions.assertTrue(ReflectHelper.isInstance(Collections.singletonMap(1, ""), Map.class));
  }

  @Test
  void testGetMethod() {
    Assertions.assertNotNull(
        ReflectHelper.getMethod(Array.class, "newInstance", Class.class, int.class));
  }

  @Test
  void testInvoke() {
    Assertions.assertNotNull(
        ReflectHelper.invoke(
            null,
            ReflectHelper.getMethod(Array.class, "newInstance", Class.class, int.class),
            Class.class,
            0));
  }

  @Test
  void testNewInstance() {
    LambdaExecutable lambdaExecutable = ReflectHelper.newInstance(LambdaExecutable.class);
    Assertions.assertNotNull(lambdaExecutable);
  }
}
