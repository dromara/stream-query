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
package org.dromara.streamquery.stream.plugin.solon.scanner;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.dromara.streamquery.stream.core.lambda.function.SerPred;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.util.ClassUtil;
import org.noear.solon.core.util.ScanUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

/**
 * stream class path scanner
 *
 * @author KamToHung
 * @since 1.5.0
 */
public class SolonClassScanner {

  private static final Log LOG = LogFactory.getLog(SolonClassScanner.class);

  /** annotation */
  private Class<? extends Annotation> annotation;

  /** scan interface */
  private Class<?> interfaceClass;

  private final ClassLoader classLoader = Solon.context().getClassLoader();

  public void setAnnotation(Class<? extends Annotation> annotation) {
    this.annotation = annotation;
  }

  public void setInterfaceClass(Class<?> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  public Set<Class<?>> scan(Set<String> basePackages) {
    if (Utils.isEmpty(basePackages)) {
      LOG.warn("basePackages is empty");
      return Collections.emptySet();
    }
    return Steam.of(basePackages)
        .flat(this::scanBasePackages)
        .sorted(Comparator.comparing(String::length))
        .map(
            name -> {
              String className = name.substring(0, name.length() - 6);
              className = className.replace("/", ".");
              return ClassUtil.loadClass(classLoader, className);
            })
        .filter(Objects::nonNull)
        .<Class<?>>map(
            clz -> {
              // 检查是否带有指定的注解
              if (this.annotation != null && !clz.isAnnotationPresent(this.annotation)) {
                return null;
              }
              // 检查是否实现了指定的接口
              if (this.interfaceClass != null && !this.interfaceClass.isAssignableFrom(clz)) {
                return null;
              }
              // 排除名为 "package-info" 的类、所有接口和所有抽象类
              if (clz.getName().endsWith("package-info")
                  || clz.isInterface()
                  || Modifier.isAbstract(clz.getModifiers())) {
                return null;
              }
              return clz;
            })
        .filter(
            SerPred.<Class<?>>multiOr(
                    Class::isMemberClass, Class::isAnonymousClass, Class::isLocalClass)
                .negate())
        .toSet();
  }

  public Set<String> scanBasePackages(String basePackages) {
    return ScanUtil.scan(classLoader, basePackages, n -> n.endsWith(".class"));
  }
}
