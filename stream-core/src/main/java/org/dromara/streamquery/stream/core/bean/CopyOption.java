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

import org.dromara.streamquery.stream.core.lambda.function.SerUnOp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.dromara.streamquery.stream.core.clazz.ClassHelper.cast;

/**
 * CopyOption
 *
 * @author achao@apache.org
 */
public class CopyOption {

  public static final CopyOption GLOBAL_COPY_OPTION = CopyOption.of();
  public static final Converter<Object, Object> NO_OP_CONVERTER = SerUnOp.identity()::apply;

  private final Map<ConverterKey, Converter<Object, Object>> converterMap;

  private boolean ignoreError = false;

  public CopyOption() {
    this.converterMap = new ConcurrentHashMap<>();
  }

  public static CopyOption of() {
    return new CopyOption().fillDefaultConverters();
  }

  public CopyOption fillDefaultConverters() {
    return addConverter(null, String.class, String::valueOf)
        .addConverter(Long.class, Integer.class, Long::intValue)
        .addConverter(String.class, Integer.class, Integer::valueOf)
        .addConverter(String.class, Long.class, Long::valueOf)
        .addConverter(Integer.class, Long.class, Long::valueOf)
        .addConverter(Double.class, Float.class, Double::floatValue)
        .addConverter(String.class, Double.class, Double::valueOf)
        .addConverter(String.class, Float.class, Float::valueOf)
        .addConverter(Float.class, Double.class, Float::doubleValue)
        .addConverter(Integer.class, Double.class, Integer::doubleValue)
        .addConverter(Integer.class, Float.class, Integer::floatValue)
        .addConverter(Long.class, Float.class, Long::floatValue)
        .addConverter(Long.class, Double.class, Long::doubleValue)
        .addConverter(String.class, Boolean.class, Boolean::valueOf)
        .addConverter(Date.class, Long.class, Date::getTime)
        .addConverter(Long.class, Date.class, Date::new)
        .addConverter(String.class, UUID.class, UUID::fromString)
        .addConverter(
            Date.class,
            LocalDateTime.class,
            date -> LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()))
        .addConverter(
            LocalDateTime.class,
            Date.class,
            ldt -> Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()))
        .addConverter(
            Date.class,
            LocalDate.class,
            date -> date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
        .addConverter(
            LocalDate.class,
            Date.class,
            ld -> Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant()))
        .addConverter(LocalDateTime.class, LocalDate.class, LocalDateTime::toLocalDate)
        .addConverter(LocalDate.class, LocalDateTime.class, LocalDate::atStartOfDay);
  }

  public <S, T> CopyOption addConverter(
      Class<S> sourceType, Class<T> targetType, Converter<S, T> converter) {
    getConverterMap().put(formatConverterKey(sourceType, targetType), cast(converter));
    return this;
  }

  public ConverterKey formatConverterKey(Class<?> sourceType, Class<?> targetType) {
    final ConverterKey converterKey = new ConverterKey();
    converterKey.setSourceType(sourceType);
    converterKey.setTargetType(targetType);
    return converterKey;
  }

  public Map<ConverterKey, Converter<Object, Object>> getConverterMap() {
    return converterMap;
  }

  public boolean isIgnoreError() {
    return ignoreError;
  }

  public CopyOption setIgnoreError(boolean ignoreError) {
    this.ignoreError = ignoreError;
    return this;
  }

  public <S, T> Converter<S, T> getConverter(Class<?> sourceType, Class<?> targetType) {
    Converter<Object, Object> converter =
        getConverterMap().get(formatConverterKey(sourceType, targetType));
    if (converter != null) {
      return cast(converter);
    }
    if (Objects.equals(sourceType.getTypeName(), targetType.getTypeName())) {
      converter = NO_OP_CONVERTER;
    } else {
      converter = getConverterMap().get(formatConverterKey(null, targetType));
    }
    return cast(converter);
  }
}
