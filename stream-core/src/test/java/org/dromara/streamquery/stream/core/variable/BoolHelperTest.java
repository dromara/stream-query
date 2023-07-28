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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author VampireAchao
 * @since 2023-07-28
 */
class BoolHelperTest {

  @Test
  void isFalsyTest() {
    Assertions.assertTrue(BoolHelper.isFalsy(false));
    Assertions.assertTrue(BoolHelper.isFalsy(0));
    Assertions.assertTrue(BoolHelper.isFalsy(-0));
    Assertions.assertTrue(BoolHelper.isFalsy(0L));
    Assertions.assertTrue(BoolHelper.isFalsy(0.0D));
    Assertions.assertTrue(BoolHelper.isFalsy(0.00D));
    Assertions.assertTrue(BoolHelper.isFalsy(-0.00D));
    Assertions.assertTrue(BoolHelper.isFalsy(""));
    Assertions.assertTrue(BoolHelper.isFalsy(null));
  }

  @Test
  void isTruthyTest() {
    Assertions.assertTrue(BoolHelper.isTruthy(true));
    Assertions.assertTrue(BoolHelper.isTruthy(1));
    Assertions.assertTrue(BoolHelper.isTruthy(-1));
    Assertions.assertTrue(BoolHelper.isTruthy(" "));
    Assertions.assertTrue(BoolHelper.isTruthy("0"));
    Assertions.assertTrue(BoolHelper.isTruthy("null"));
    Assertions.assertTrue(BoolHelper.isTruthy("undefined"));
    Assertions.assertTrue(BoolHelper.isTruthy(1L));
    Assertions.assertTrue(BoolHelper.isTruthy(1L));
    Assertions.assertTrue(BoolHelper.isTruthy(new Object()));
  }
}
