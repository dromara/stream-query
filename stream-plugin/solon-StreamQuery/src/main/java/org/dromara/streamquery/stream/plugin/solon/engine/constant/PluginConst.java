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
package org.dromara.streamquery.stream.plugin.solon.engine.constant;

import org.dromara.streamquery.stream.plugin.solon.Database;

/**
 * PluginConst interface.
 *
 * @author VampireAchao Cizai_
 */
public interface PluginConst {

  /** script tags */
  String SCRIPT_TAGS = "<script>%s</script>";

  /** default batch commit count */
  int DEFAULT_BATCH_SIZE = 1000;
  /** db keyword default */
  String DEFAULT = "default";
  /** db keyword case */
  String CASE = "case";
  /** db keyword end */
  String END = "end";
  /** mapper non null condition */
  String NON_NULL_CONDITION = "%s != null and %s != null";
  /** mapper not blank condition */
  String NON_BLANK_CONDITION = "%s != null and %s != null and %s != ''";
  /** mapper not empty condition */
  String NON_EMPTY_CONDITION = "%s != null and !%s.isEmpty()";
  /** db keyword when then template */
  String WHEN_THEN = "when %s then %s";
  /** collection parameter name */
  String COLLECTION_PARAM_NAME = "list";
  /** wrapper not active */
  String WRAPPER_NOT_ACTIVE = "WRAPPER_NOT_ACTIVE";
  /** dynamic mapper prefix */
  String DYNAMIC_MAPPER_PREFIX = Database.class.getPackage().getName() + ".dynamicMapper";
}
