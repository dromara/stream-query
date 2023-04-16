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

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href = "kamtohung@gmail.com">KamTo Hung</a>
 */
public class StreamClassPathScanner extends ClassPathScanningCandidateComponentProvider {

  private static final Log LOG = LogFactory.getLog(StreamClassPathScanner.class);

    /** annotation */
  private Class<? extends Annotation> annotation;

    /** scan interface */
  private Class<?> interfaceClass;

  public StreamClassPathScanner(boolean useDefaultFilters) {
    super(useDefaultFilters);
  }

  public void setAnnotation(Class<? extends Annotation> annotation) {
    this.annotation = annotation;
  }

  public void setInterfaceClass(Class<?> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

  public void registerFilters() {
    boolean acceptAllInterfaces = true;

    if (this.annotation != null) {
      addIncludeFilter(new AnnotationTypeFilter(this.annotation));
      acceptAllInterfaces = false;
    }

    if (this.interfaceClass != null) {
        addIncludeFilter(
                new AssignableTypeFilter(this.interfaceClass) {
                    // remove parent entity
                    @Override
                    protected boolean matchClassName(String className) {
                        return false;
                    }
                });
        acceptAllInterfaces = false;
    }

      if (acceptAllInterfaces) {
          // default include filter that accepts all classes
          addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
      }

      // exclude package-info.java
      addExcludeFilter(
              (metadataReader, metadataReaderFactory) -> {
                  String className = metadataReader.getClassMetadata().getClassName();
                  return className.endsWith("package-info");
              });
  }

  public Set<Class<?>> scan(Set<String> basePackages) {
    if (CollectionUtils.isEmpty(basePackages)) {
      LOG.warn("basePackages is empty");
      return Collections.emptySet();
    }
      return basePackages.stream()
              .map(this::findCandidateComponents)
              .flatMap(Collection::stream)
              .map(BeanDefinition::getBeanClassName)
              .filter(Objects::nonNull)
              .map(ReflectHelper::forClassName)
              .collect(Collectors.toSet());
  }
}
