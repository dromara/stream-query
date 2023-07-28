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
package org.dromara.streamquery.stream.core.variable;

import java.util.Arrays;
import java.util.List;

/**
 * BoolHelper class.
 *
 * @author VampireAchao
 * @since 2023-07-28
 */
public class BoolHelper {

  private BoolHelper() {
    /* Do not new me! */
  }

  public static final List<Object> FALSY_VALUES =
      Arrays.asList(false, 0, -0, 0L, 0.0D, -0.0D, "", null);

  public static boolean isFalsy(Object value) {
    if (FALSY_VALUES.contains(value)) {
      return true;
    }
    if (value instanceof Double) {
      return Double.isNaN((Double) value);
    }
    return false;
  }

  public static boolean isTruthy(Object value) {
    return !isFalsy(value);
  }
}
