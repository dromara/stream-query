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

import org.dromara.streamquery.stream.plugin.mybatisplus.engine.annotation.EnableMybatisPlusPlugin;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean definition registrar for {@link StreamScannerConfigurer}.
 *
 * @author <a href = "kamtohung@gmail.com">KamTo Hung</a>
 */
public class StreamScannerRegistrar implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(
          AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
      AnnotationAttributes annotationAttributes =
              AnnotationAttributes.fromMap(
                      importingClassMetadata.getAnnotationAttributes(
                              EnableMybatisPlusPlugin.class.getName()));
      if (Objects.isNull(annotationAttributes)) {
          return;
      }
      BeanDefinitionBuilder builder =
              BeanDefinitionBuilder.genericBeanDefinition(StreamScannerConfigurer.class);
      Set<String> basePackages = new HashSet<>();
      basePackages.addAll(
              Arrays.stream(annotationAttributes.getStringArray("value"))
                      .filter(StringUtils::hasText)
                      .collect(Collectors.toSet()));
      basePackages.addAll(
              Arrays.stream(annotationAttributes.getStringArray("basePackages"))
                      .filter(StringUtils::hasText)
                      .collect(Collectors.toSet()));
      basePackages.addAll(
              Arrays.stream(annotationAttributes.getClassArray("basePackageClasses"))
                      .filter(Objects::nonNull)
                      .map(ClassUtils::getPackageName)
                      .collect(Collectors.toSet()));
      builder.addPropertyValue("basePackages", basePackages);

      Set<Class<?>> classes =
              Arrays.stream(annotationAttributes.getClassArray("classes"))
                      .filter(Objects::nonNull)
                      .collect(Collectors.toSet());
      builder.addPropertyValue("classes", classes);

      Class<? extends Annotation> annotation = annotationAttributes.getClass("annotation");
      if (!Annotation.class.equals(annotation)) {
          builder.addPropertyValue("annotation", annotation);
      }

      Class<?> scanInterface = annotationAttributes.getClass("interfaceClass");
    if (!Class.class.equals(scanInterface)) {
      builder.addPropertyValue("interfaceClass", scanInterface);
    }

    registry.registerBeanDefinition("streamScannerConfigurer", builder.getBeanDefinition());
  }
}
