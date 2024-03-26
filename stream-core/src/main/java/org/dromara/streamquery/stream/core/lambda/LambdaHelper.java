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
package org.dromara.streamquery.stream.core.lambda;

import org.dromara.streamquery.stream.core.bean.BeanHelper;
import org.dromara.streamquery.stream.core.collection.Maps;
import org.dromara.streamquery.stream.core.lambda.function.SerBiCons;
import org.dromara.streamquery.stream.core.lambda.function.SerBiFunc;
import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.lambda.function.SerSupp;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.dromara.streamquery.stream.core.stream.collector.Collective;

import java.io.Serializable;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Function;

import static org.dromara.streamquery.stream.core.clazz.ClassHelper.cast;

/**
 * LambdaHelper
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/29 9:19
 */
public class LambdaHelper {

  private static final WeakHashMap<String, LambdaExecutable> SERIALIZED_LAMBDA_EXECUTABLE_CACHE =
      new WeakHashMap<>();
  private static final WeakHashMap<Class<?>, WeakHashMap<Executable, Object>> LAMBDA_REVERT_CACHE =
      new WeakHashMap<>();
  private static final WeakHashMap<
          Class<?>, Map<String, Map.Entry<SerFunc<?, Object>, Serializable>>>
      PROPERTY_GETTER_SETTER_CACHE = new WeakHashMap<>();

  private LambdaHelper() {
    /* Do not new me! */
  }

  /**
   * Resolve the lambda to a {@link SerializedLambda} instance.
   *
   * @param lambda The lambda to resolve.
   * @return SerializedLambda
   */
  private static SerializedLambda serialize(Serializable lambda) {
    if (lambda instanceof SerializedLambda) {
      return (SerializedLambda) lambda;
    }
    final Class<? extends Serializable> clazz = lambda.getClass();
    if (!clazz.isSynthetic()) {
      throw new IllegalArgumentException("Not a lambda expression: " + clazz.getName());
    }
    try {
      final Method writeReplace = ReflectHelper.accessible(clazz.getDeclaredMethod("writeReplace"));
      final Object maybeSerLambda = writeReplace.invoke(lambda);
      if (maybeSerLambda instanceof SerializedLambda) {
        return (SerializedLambda) maybeSerLambda;
      }
      throw new IllegalStateException(
          "writeReplace result value is not java.lang.invoke.SerializedLambda");
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Resolve the lambda to a {@link LambdaExecutable} instance.
   *
   * @param lambda The lambda to resolve.
   * @param <T> a T class
   * @return LambdaExecutable
   */
  public static <T extends Serializable> LambdaExecutable resolve(T lambda) {
    Objects.requireNonNull(lambda, "lambda can not be null");
    if (lambda instanceof Proxy) {
      return LambdaExecutable.initProxy((Proxy) lambda);
    }
    return Maps.computeIfAbsent(
        SERIALIZED_LAMBDA_EXECUTABLE_CACHE,
        lambda.getClass().getName(),
        key -> new LambdaExecutable(serialize(lambda)));
  }

  @SuppressWarnings("unchecked")
  public static <T> T revert(Class<? super T> clazz, Executable executable) {
    WeakHashMap<Executable, Object> lambdaCache =
        Maps.computeIfAbsent(LAMBDA_REVERT_CACHE, clazz, key -> new WeakHashMap<>());
    return (T)
        Maps.computeIfAbsent(
            lambdaCache,
            executable,
            key -> {
              final Method funcMethod =
                  Steam.of(clazz.getMethods())
                      .findFirst(method -> Modifier.isAbstract(method.getModifiers()))
                      .orElseThrow(() -> new LambdaInvokeException("not a functional interface"));
              final MethodHandle implMethod;
              final MethodType instantiatedMethodType;
              if (executable instanceof Method) {
                final Method method = (Method) executable;
                implMethod =
                    ((SerSupp<MethodHandle>) () -> MethodHandles.lookup().unreflect(method)).get();
                instantiatedMethodType =
                    MethodType.methodType(
                        method.getReturnType(),
                        method.getDeclaringClass(),
                        method.getParameterTypes());
              } else {
                final Constructor<?> constructor = (Constructor<?>) executable;
                implMethod =
                    ((SerSupp<MethodHandle>)
                            () -> MethodHandles.lookup().unreflectConstructor(constructor))
                        .get();
                instantiatedMethodType =
                    MethodType.methodType(
                        constructor.getDeclaringClass(), constructor.getParameterTypes());
              }
              final CallSite callSite =
                  ((SerSupp<CallSite>)
                          () ->
                              Serializable.class.isAssignableFrom(clazz)
                                  ? LambdaMetafactory.altMetafactory(
                                      MethodHandles.lookup(),
                                      funcMethod.getName(),
                                      MethodType.methodType(clazz),
                                      MethodType.methodType(
                                          funcMethod.getReturnType(),
                                          funcMethod.getParameterTypes()),
                                      implMethod,
                                      instantiatedMethodType,
                                      LambdaMetafactory.FLAG_SERIALIZABLE)
                                  : LambdaMetafactory.metafactory(
                                      MethodHandles.lookup(),
                                      funcMethod.getName(),
                                      MethodType.methodType(clazz),
                                      MethodType.methodType(
                                          funcMethod.getReturnType(),
                                          funcMethod.getParameterTypes()),
                                      implMethod,
                                      instantiatedMethodType))
                      .get();
              final MethodHandle target = callSite.getTarget();
              return ((SerSupp<T>) () -> cast(target.invoke())).get();
            });
  }

  /**
   * getPropertyNames.
   *
   * @param funcs a {@link SerFunc} object
   * @param <T> a T class
   * @return a {@link java.util.List} object
   */
  @SafeVarargs
  public static <T> List<String> getPropertyNames(SerFunc<T, ?>... funcs) {
    return Steam.of(funcs).map(LambdaHelper::getPropertyName).toList();
  }

  /**
   * getPropertyName.
   *
   * @param func a {@link SerFunc} object
   * @param <T> a T class
   * @return a {@link java.lang.String} object
   */
  public static <T> String getPropertyName(SerFunc<T, ?> func) {
    return Opp.of(func)
        .map(LambdaHelper::resolve)
        .map(LambdaExecutable::getName)
        .map(BeanHelper::getPropertyName)
        .get();
  }

  /**
   * 获取getter对应的lambda
   *
   * @param clazz 类
   * @param propertyName 属性名
   * @param <T> 类型
   * @param <R> getter返回值
   * @return 返回getter对应的lambda
   */
  public static <T, R> SerFunc<T, R> getGetter(Class<T> clazz, String propertyName) {
    return SerFunc.<SerFunc<?, ?>, SerFunc<T, R>>cast()
        .apply(getGetter(clazz, propertyName, SerFunc.class));
  }

  /**
   * 获取getter对应的lambda
   *
   * @param clazz 类
   * @param propertyName 属性名
   * @param lambdaType lambda类型
   * @param <T> 类型
   * @param <F> lambda类型
   * @return 返回getter对应的lambda
   */
  public static <T, F> F getGetter(Class<T> clazz, String propertyName, Class<F> lambdaType) {
    return revert(
        lambdaType, ReflectHelper.getMethod(clazz, BeanHelper.getGetterName(propertyName)));
  }

  /**
   * 获取setter对应的lambda
   *
   * @param clazz 类
   * @param propertyName 属性名
   * @param <T> 类型
   * @param <U> setter参数类型
   * @return 返回setter对应的lambda
   */
  public static <T, U> SerBiCons<T, U> getSetter(Class<T> clazz, String propertyName) {
    return SerFunc.<SerBiCons<?, ?>, SerBiCons<T, U>>cast()
        .apply(getSetter(clazz, propertyName, SerBiCons.class));
  }

  /**
   * 获取setter对应的lambda
   *
   * @param clazz 类
   * @param propertyName 属性名
   * @param lambdaType lambda类型
   * @param <T> 类型
   * @param <F> lambda类型
   * @return 返回setter对应的lambda
   */
  public static <T, F> F getSetter(Class<T> clazz, String propertyName, Class<F> lambdaType) {
    return revert(
        lambdaType,
        ReflectHelper.getMethod(
            clazz,
            BeanHelper.getSetterName(propertyName),
            ReflectHelper.getField(clazz, propertyName).getType()));
  }

  /**
   * 通过getter获取setter
   *
   * @param getter getter对应的lambda
   * @param <T> getter参数类型
   * @param <R> property类型
   * @return 返回setter对应的lambda
   */
  public static <T, R> SerBiCons<T, R> getSetter(SerFunc<T, R> getter) {
    return getSetter(getter, SerBiCons.class);
  }

  /**
   * 通过getter获取setter
   *
   * @param getter getter对应的lambda
   * @param lambdaType setter对应的lambda类型
   * @param <F> getter对应的lambda类型
   * @param <C> setter对应的lambda类型
   * @return 返回setter对应的lambda
   */
  public static <F extends Serializable, C> C getSetter(F getter, Class<? super C> lambdaType) {
    LambdaExecutable executable = LambdaHelper.resolve(getter);
    Object setter =
        getSetter(
            executable.getClazz(), BeanHelper.getPropertyName(executable.getName()), lambdaType);
    return cast(setter);
  }

  /**
   * 获取getter和setter组成的map
   *
   * @param clazz 类
   * @param <T> 类型
   * @return 返回getter和setter组成的map
   */
  public static <T> Map<SerFunc<T, Object>, Serializable> getGetterSetterMap(Class<T> clazz) {
    return Steam.of(getPropertyGetterSetterMap(clazz).values()).collect(Collective.entryToMap());
  }

  /**
   * 获取属性名和getter和setter组成的map
   *
   * @param clazz 类
   * @param <T> 类型
   * @return 返回属性名和getter和setter组成的map
   */
  public static <T>
      Map<String, Map.Entry<SerFunc<T, Object>, Serializable>> getPropertyGetterSetterMap(
          Class<T> clazz) {
    Map<String, Map.Entry<SerFunc<?, Object>, Serializable>> propertyGetterSetterMap =
        PROPERTY_GETTER_SETTER_CACHE.computeIfAbsent(
            clazz,
            key -> {
              List<Method> methods = ReflectHelper.getMethods(clazz);
              List<String> methodNames = Steam.of(methods).map(Method::getName).toList();
              List<String> properties =
                  Steam.of(ReflectHelper.getFields(clazz))
                      .map(Field::getName)
                      .filter(
                          name -> {
                            String getterName = BeanHelper.getGetterName(name);
                            String setterName = BeanHelper.getSetterName(name);
                            return methodNames.contains(getterName)
                                && methodNames.contains(setterName);
                          })
                      .toList();
              return Steam.of(properties)
                  .toMap(
                      Function.identity(),
                      property -> {
                        Method method =
                            ReflectHelper.getMethod(
                                clazz,
                                BeanHelper.getSetterName(property),
                                ReflectHelper.getField(clazz, property).getType());
                        final Serializable setter =
                            LambdaHelper.revert(
                                void.class.isAssignableFrom(method.getReturnType())
                                    ? SerBiCons.class
                                    : SerBiFunc.class,
                                method);
                        return Maps.entry(LambdaHelper.getGetter(clazz, property), setter);
                      });
            });
    return SerFunc
        .<Map<String, Map.Entry<SerFunc<?, Object>, Serializable>>,
            Map<String, Map.Entry<SerFunc<T, Object>, Serializable>>>
            cast()
        .apply(propertyGetterSetterMap);
  }
}
