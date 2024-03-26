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
package org.dromara.streamquery.stream.core.clazz;

import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.lambda.function.SerPred;
import org.dromara.streamquery.stream.core.lambda.function.SerSupp;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * ClassHelper
 *
 * @author VampireAchao
 * @since 2023/1/9
 */
public class ClassHelper {

  private ClassHelper() {
    /* Do not new me! */
  }

  /**
   * 扫描对应包下的类
   *
   * @param packageName 包名，例如org.dromara.stream.core.clazz
   * @return 包下的类
   */
  public static List<Class<?>> scanClasses(String packageName) {
    String packagePath = packageName.replace(".", "/");
    Enumeration<URL> resources =
        ((SerSupp<Enumeration<URL>>)
                () -> Thread.currentThread().getContextClassLoader().getResources(packagePath))
            .get();
    return Steam.of(Collections.list(resources))
        .flat(
            (SerFunc<URL, Steam<String>>)
                (url -> {
                  if ("file".equals(url.getProtocol())) {
                    File dir =
                        new File(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name()));
                    if (!dir.exists() || !dir.isDirectory()) {
                      return null;
                    }
                    return Steam.of(dir.listFiles())
                        .map(File::getName)
                        .filter(path -> path.endsWith(".class"))
                        .map(
                            path ->
                                path.substring(
                                    path.lastIndexOf(File.separatorChar) + 1, path.length() - 6))
                        .map(name -> packageName + "." + name);
                  }
                  JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                  JarFile jarFile = urlConnection.getJarFile();
                  return Steam.of(Collections.list(jarFile.entries()))
                      .filter(
                          SerPred.multiAnd(
                              e -> e.getName().startsWith(packagePath),
                              e -> e.getName().endsWith(".class"),
                              e -> !e.isDirectory()))
                      .map(ZipEntry::getName)
                      .map(
                          name ->
                              name.substring(0, name.length() - 6).replace(File.separator, "."));
                }))
        .filter(className -> !className.contains("$"))
        .<Class<?>>map(ReflectHelper::loadClass)
        .filter(
            clazz ->
                !Modifier.isAbstract(clazz.getModifiers())
                    && !Modifier.isInterface(clazz.getModifiers()))
        .toList();
  }

  /**
   * Cast obj type.
   *
   * @param obj obj
   * @param <T> T
   * @return T
   */
  public static <T> T cast(Object obj) {
    return SerFunc.<Object, T>cast().apply(obj);
  }
}
