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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.utils;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Cason
 * @since 2023-06-29
 */
public class SqlInjectionUtilSq {
  private static final Pattern SQL_SYNTAX_PATTERN =
      Pattern.compile(
          "(insert|delete|update|select|create|drop|truncate|grant|alter|deny|revoke|call|execute|exec|declare|show|rename|set)\\s+.*(into|from|set|where|table|database|view|index|on|cursor|procedure|trigger|for|password|union|and|or)|(select\\s*\\*\\s*from\\s+)",
          Pattern.CASE_INSENSITIVE);
  private static final Pattern SQL_COMMENT_PATTERN =
      Pattern.compile("(['\"]?.*(\\bor\\b|\\bunion\\b|--|#|\\/\\*|;))", Pattern.CASE_INSENSITIVE);

  public SqlInjectionUtilSq() {}

  public static boolean check(String value) {
    Objects.requireNonNull(value);
    return SQL_COMMENT_PATTERN.matcher(value).find() || SQL_SYNTAX_PATTERN.matcher(value).find();
  }
}
