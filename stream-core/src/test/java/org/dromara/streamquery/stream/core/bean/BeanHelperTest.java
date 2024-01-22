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
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BeanHelperTest
 *
 * @author VampireAchao
 * @since 2023/3/15
 */
class BeanHelperTest {

  @Test
  void testGetSetterName() {
    assertEquals("setName", BeanHelper.getSetterName("name"));
    assertEquals("setLambda", BeanHelper.getSetterName("lambda"));
  }

  @Test
  void testGetGetterName() {
    assertEquals("getName", BeanHelper.getGetterName("name"));
    assertEquals("getLambda", BeanHelper.getGetterName("lambda"));
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

  @Data
  public static class Artist implements Serializable {
    private static final long serialVersionUID = 1L;
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
    Person person = BeanHelper.copyProperties(source, Person.class);
    assertEquals(source.getName(), person.getName());

    Artist artist =
        new Artist() {
          private static final long serialVersionUID = 6276191330280044345L;

          {
            setName("test");
          }
        };
    User user = BeanHelper.copyProperties(source, User.class);
    assertEquals(artist.getName(), user.getName());
  }

  @Data
  public static class EntityWithStringId {
    private String id;
  }

  @Data
  public static class EntityWithIntegerId {
    private Integer id;
  }

  @Test
  void testCopyPropertiesWithConverter() {
    val source = new EntityWithStringId();
    source.setId("1");
    EntityWithIntegerId target = BeanHelper.copyProperties(source, EntityWithIntegerId.class);
    assertEquals(source.getId(), target.getId().toString());
    assertEquals(
        source.getId(), BeanHelper.copyProperties(target, EntityWithStringId.class).getId());
    assertEquals(
        target.getId(),
        BeanHelper.copyProperties(
                source,
                EntityWithIntegerId.class,
                new CopyOption().addConverter(String.class, Integer.class, Integer::new))
            .getId());
    assertEquals(
        target.getId(),
        BeanHelper.copyProperties(
                source,
                EntityWithIntegerId.class,
                new CopyOption()
                    .addConverter(
                        EntityWithStringId.class,
                        EntityWithIntegerId.class,
                        s -> {
                          val t = new EntityWithIntegerId();
                          t.setId(Integer.valueOf(s.getId()));
                          return t;
                        }))
            .getId());

    assertNull(BeanHelper.copyProperties(null, Object.class));
    assertEquals("1", BeanHelper.copyProperties(1, String.class));
    assertEquals(Integer.valueOf(1), BeanHelper.copyProperties(1L, Integer.class));
    assertEquals(Integer.valueOf(1), BeanHelper.copyProperties("1", Integer.class));
    assertEquals(Long.valueOf(1L), BeanHelper.copyProperties("1", Long.class));
    assertEquals(Long.valueOf(1L), BeanHelper.copyProperties(1, Long.class));
    assertEquals(Float.valueOf(1.0f), BeanHelper.copyProperties(1.0, Float.class));
    assertEquals(Double.valueOf(1.0), BeanHelper.copyProperties("1.0", Double.class));
    assertEquals(Float.valueOf(1.0f), BeanHelper.copyProperties("1.0", Float.class));
    assertEquals(Double.valueOf(1.0), BeanHelper.copyProperties(1.0f, Double.class));
    assertEquals(Double.valueOf(1.0), BeanHelper.copyProperties(1, Double.class));
    assertEquals(Float.valueOf(1.0f), BeanHelper.copyProperties(1, Float.class));
    assertEquals(Float.valueOf(1.0f), BeanHelper.copyProperties(1L, Float.class));
    assertEquals(Double.valueOf(1.0), BeanHelper.copyProperties(1L, Double.class));
    assertEquals(Boolean.TRUE, BeanHelper.copyProperties("true", Boolean.class));
    Date now = new Date();
    assertEquals(now.getTime(), BeanHelper.copyProperties(now, Long.class));
    assertEquals(new Date(now.getTime()), BeanHelper.copyProperties(now.getTime(), Date.class));
    UUID uuid = UUID.randomUUID();
    assertEquals(uuid, BeanHelper.copyProperties(uuid.toString(), UUID.class));
    LocalDateTime ldt = LocalDateTime.now();
    assertEquals(
        Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()),
        BeanHelper.copyProperties(ldt, Date.class));
    LocalDate ld = LocalDate.now();
    assertEquals(
        Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant()),
        BeanHelper.copyProperties(ld, Date.class));
    assertEquals(ld, BeanHelper.copyProperties(ldt, LocalDate.class));
    assertEquals(ld.atStartOfDay(), BeanHelper.copyProperties(ld, LocalDateTime.class));
  }

  @Test
  void testIgnoreError() {
    val source = new EntityWithStringId();
    source.setId("1");
    assertThrows(
        ConvertFailException.class,
        () -> BeanHelper.copyProperties(source, EntityWithIntegerId.class, new CopyOption()));
    assertDoesNotThrow(
        () ->
            BeanHelper.copyProperties(
                source, EntityWithIntegerId.class, new CopyOption().setIgnoreError(true)));
  }
}
