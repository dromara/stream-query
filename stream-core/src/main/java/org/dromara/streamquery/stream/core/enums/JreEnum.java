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
package org.dromara.streamquery.stream.core.enums;

import org.dromara.streamquery.stream.core.reflect.ReflectHelper;

import java.util.logging.Logger;

/**
 * JRE version
 *
 * @author <a href = "mailto:kamtohung@gmail.com">hongjintao</a>
 */
public enum JreEnum {

  /** JRE version */
  JAVA_8,

  JAVA_9,

  JAVA_10,

  JAVA_11,

  JAVA_12,

  JAVA_13,

  JAVA_14,

  JAVA_15,

  JAVA_16,

  JAVA_17,

  JAVA_18,

  JAVA_19;

  private static final Logger LOGGER = Logger.getAnonymousLogger();

  private static final JreEnum VERSION = getJre();

  public static final String DEFAULT_JAVA_VERSION = "1.8";

  /**
   * get current JRE version
   *
   * @return JRE version
   */
  public static JreEnum currentVersion() {
    return VERSION;
  }

  /**
   * is current version
   *
   * @return true if current version
   */
  public boolean isCurrentVersion() {
    return this == VERSION;
  }

  private static JreEnum getJre() {
    String version = System.getProperty("java.version");
    boolean isBlank = version == null || version.trim().isEmpty();
    if (isBlank) {
      LOGGER.info("java.version is blank");
    }
    if (!isBlank) {
      for (JreEnum jre : values()) {
        if (version.startsWith(jre.name().split("_")[1] + ".")) {
          return jre;
        }
      }
      if (version.startsWith(DEFAULT_JAVA_VERSION)) {
        return JAVA_8;
      }
    }
    try {
      // For JDK 9+ and above, use Runtime.version() to get the JRE version
      Object javaRunTimeVersion =
          ReflectHelper.invoke(
              Runtime.getRuntime(), ReflectHelper.getMethod(Runtime.class, "version"));
      int majorVersion =
          ReflectHelper.invoke(
              javaRunTimeVersion, ReflectHelper.getMethod(javaRunTimeVersion.getClass(), "major"));
      return JreEnum.valueOf("JAVA_" + majorVersion);
    } catch (Exception ignore) {
      LOGGER.info("can't determine current JRE version");
    }
    return JAVA_8;
  }
}
