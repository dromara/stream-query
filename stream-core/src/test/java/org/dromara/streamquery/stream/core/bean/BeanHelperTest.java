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
package org.dromara.streamquery.stream.core.bean;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * BeanHelperTest
 *
 * @author VampireAchao
 * @since 2023/3/15
 */
class BeanHelperTest {

  @Test
  void testGetSetterName() {
    Assertions.assertEquals("setName", BeanHelper.getSetterName("name"));
    Assertions.assertEquals("setLambda", BeanHelper.getSetterName("lambda"));
  }

  @Test
  void testGetGetterName() {
    Assertions.assertEquals("getName", BeanHelper.getGetterName("name"));
    Assertions.assertEquals("getLambda", BeanHelper.getGetterName("lambda"));
  }

  @Data
  public static class User {
    private String name;
  }

  @Data
  @Accessors(chain = true)
  public static class Person {
    private String name;
  }

  @Test
  void testCopyProperties() {
    User source =
        new User() {
          {
            setName("test");
          }
        };
    Person target = BeanHelper.copyProperties(source, Person.class);
    Assertions.assertEquals(source.getName(), target.getName());
  }
}
