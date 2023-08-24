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
package org.dromara.streamquery.stream.core.bean;

import org.dromara.streamquery.stream.core.lambda.LambdaExecutable;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * BeanHelper class.
 *
 * @author VampireAchao Cizai_
 */
public class BeanHelper {

  /** Constant <code>GETTER_PREFIX="get"</code> */
  public static final String GETTER_PREFIX = "get";
  /** Constant <code>GETTER_BOOLEAN_PREFIX="is"</code> */
  public static final String GETTER_BOOLEAN_PREFIX = "is";
  /** Constant <code>SETTER_PREFIX="set"</code> */
  public static final String SETTER_PREFIX = "set";

  private BeanHelper() {
    /* Do not new me! */
  }

  /**
   * getPropertyName.
   *
   * @param getterOrSetter a {@link java.lang.String} object
   * @return a {@link java.lang.String} object
   */
  public static String getPropertyName(String getterOrSetter) {
    String originProperty = null;
    if (isGetterBoolean(getterOrSetter)) {
      originProperty = getterOrSetter.replaceFirst(GETTER_BOOLEAN_PREFIX, "");
    } else if (isGetter(getterOrSetter)) {
      originProperty = getterOrSetter.replaceFirst(GETTER_PREFIX, "");
    } else if (isSetter(getterOrSetter)) {
      originProperty = getterOrSetter.replaceFirst(SETTER_PREFIX, "");
    }
    if (originProperty == null) {
      throw new UnsupportedOperationException(getterOrSetter + " is not getter or setter");
    }
    return originProperty.substring(0, 1).toLowerCase() + originProperty.substring(1);
  }

  /**
   * isGetter.
   *
   * @param methodName a {@link java.lang.String} object
   * @return a boolean
   */
  public static boolean isGetter(String methodName) {
    return Opp.ofStr(methodName)
        .is(s -> s.startsWith(GETTER_PREFIX) || s.startsWith(GETTER_BOOLEAN_PREFIX));
  }

  /**
   * isGetterBoolean.
   *
   * @param methodName a {@link java.lang.String} object
   * @return a boolean
   */
  public static boolean isGetterBoolean(String methodName) {
    return Opp.ofStr(methodName).is(s -> s.startsWith(GETTER_BOOLEAN_PREFIX));
  }

  /**
   * isSetter.
   *
   * @param methodName a {@link java.lang.String} object
   * @return a boolean
   */
  public static boolean isSetter(String methodName) {
    return Opp.ofStr(methodName).is(s -> s.startsWith(SETTER_PREFIX));
  }

  /**
   * 通过属性名得到setter名
   *
   * @param propertyName 属性名
   * @return setter名
   */
  public static String getSetterName(String propertyName) {
    return SETTER_PREFIX + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
  }

  /**
   * 通过属性名获取getter名
   *
   * @param propertyName 属性名
   * @return getter名
   */
  public static String getGetterName(String propertyName) {
    return GETTER_PREFIX + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
  }

  /**
   * 拷贝属性
   *
   * @param source 源对象
   * @param target 目标对象
   * @param <T> 对象类型
   */
  public static <T, R> R copyProperties(T source, R target) {
    if (source == null || target == null) {
      return target;
    }

    Class<T> sourceType = SerFunc.<Class<?>, Class<T>>cast().apply(source.getClass());
    Map<String, Map.Entry<SerFunc<T, Object>, Serializable>> sourcePropertyGetterSetterMap =
        LambdaHelper.getPropertyGetterSetterMap(sourceType);

    Class<R> targetType = SerFunc.<Class<?>, Class<R>>cast().apply(target.getClass());
    Map<String, Map.Entry<SerFunc<R, Object>, Serializable>> targetPropertyGetterSetterMap =
        LambdaHelper.getPropertyGetterSetterMap(targetType);

    for (Map.Entry<String, Map.Entry<SerFunc<T, Object>, Serializable>> sourceEntry :
        sourcePropertyGetterSetterMap.entrySet()) {
      String property = sourceEntry.getKey();
      Map.Entry<SerFunc<T, Object>, Serializable> sourceGetterSetter = sourceEntry.getValue();
      Map.Entry<SerFunc<R, Object>, Serializable> targetGetterSetter =
          targetPropertyGetterSetterMap.get(property);

      if (targetGetterSetter != null) {
        SerFunc<T, Object> sourceGetter = sourceGetterSetter.getKey();
        SerFunc<R, Object> targetGetter = targetGetterSetter.getKey();

        LambdaExecutable sourceGetterLambda = LambdaHelper.resolve(sourceGetter);
        LambdaExecutable targetGetterLambda = LambdaHelper.resolve(targetGetter);

        if (!Opp.of(sourceGetterLambda.getReturnType())
            .map(Type::getTypeName)
            .equals(Opp.of(targetGetterLambda.getReturnType()).map(Type::getTypeName))) {
          continue;
        }

        Object value = sourceGetter.apply(source);
        Serializable setter = targetGetterSetter.getValue();
        if (BiConsumer.class.isAssignableFrom(setter.getClass())) {
          SerFunc.<Serializable, BiConsumer<R, Object>>cast().apply(setter).accept(target, value);
        } else {
          SerFunc.<Serializable, BiFunction<R, Object, R>>cast().apply(setter).apply(target, value);
        }
      }
    }
    return target;
  }

  public static <T, R> R copyProperties(T source, Class<R> targetType) {
    R target = ReflectHelper.newInstance(targetType);
    if (Objects.isNull(source)) {
      return target;
    }
    return copyProperties(source, target);
  }
}
