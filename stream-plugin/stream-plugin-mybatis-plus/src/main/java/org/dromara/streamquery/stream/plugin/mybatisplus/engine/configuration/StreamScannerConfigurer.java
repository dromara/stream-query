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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration;


import org.dromara.streamquery.stream.core.clazz.ClassHelper;
import org.springframework.beans.factory.InitializingBean;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * stream scanner configurer
 * from {@link StreamScannerRegistrar}
 * </pre>
 *
 * @author <a href = "kamtohung@gmail.com">KamTo Hung</a>
 */
public class StreamScannerConfigurer implements InitializingBean {

  /**
   * base package
   */
  private Set<String> basePackages;

  /**
   * specify classes
   */
  private Set<Class<?>> classes;

  /**
   * annotation
   */
  private Class<? extends Annotation> annotation;

  /**
   * scan interface
   */
  private Class<?> interfaceClass;

  /**
   * entity class list
   */
  private final Set<Class<?>> entityClassList = new HashSet<>();

  public void setBasePackages(Set<String> basePackages) {
    this.basePackages = basePackages;
  }

  public Set<Class<?>> getClasses() {
    return classes;
  }

  public void setClasses(Set<Class<?>> classes) {
    this.classes = classes;
  }

  public void setAnnotation(Class<? extends Annotation> annotation) {
    this.annotation = annotation;
  }

  public void setInterfaceClass(Class<?> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  public Collection<Class<?>> getEntityClassList() {
    return entityClassList;
  }

  public void register() {
    /// 扫描po包下的所有类，作为entity
    Set<String> basePackages = this.basePackages;
    for (String basePackage : basePackages) {
      this.entityClassList.addAll(ClassHelper.scanClasses(basePackage));
    }
    // 指定类
    this.entityClassList.addAll(this.classes);
    // TODO 注解的类
    // TODO 实现接口的类
  }

  @Override
  public void afterPropertiesSet() {
    register();
  }
}
