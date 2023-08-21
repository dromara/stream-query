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

import lombok.SneakyThrows;
import lombok.val;
import org.dromara.streamquery.stream.core.lambda.function.*;
import org.dromara.streamquery.stream.core.reflect.AbstractTypeReference;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.invoke.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * LambdaHelper测试
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/31 19:51
 */
class LambdaHelperTest {

  @Test
  <T> void testResolve() {
    Assertions.assertEquals(
        Integer[][].class,
        LambdaHelper.resolve((Serializable & BiConsumer<Integer[][], Integer>) (i, a) -> {})
            .getParameterTypes()[0]);
    Assertions.assertEquals(
        Integer.class,
        LambdaHelper.resolve((Serializable & BiConsumer<Integer[][], Integer>) (i, a) -> {})
            .getParameterTypes()[1]);
    Assertions.assertEquals(
        Integer.class,
        LambdaHelper.resolve((Serializable & BiConsumer<Integer, Integer[][][]>) (i, a) -> {})
            .getParameterTypes()[0]);
    Assertions.assertEquals(
        Integer[][][].class,
        LambdaHelper.resolve((Serializable & BiConsumer<Integer, Integer[][][]>) (i, a) -> {})
            .getParameterTypes()[1]);
    Assertions.assertEquals(
        0, LambdaHelper.<SerSupp<Object>>resolve(Object::new).getParameterTypes().length);
    Assertions.assertEquals(
        Object.class, LambdaHelper.<SerSupp<Object>>resolve(Object::new).getReturnType());
    Assertions.assertEquals(
        int.class,
        LambdaHelper.<SerFunc<Integer, Integer[]>>resolve(Integer[]::new).getParameterTypes()[0]);
    Assertions.assertEquals(
        Integer[].class,
        LambdaHelper.<SerFunc<Integer, Integer[]>>resolve(Integer[]::new).getReturnType());
    Assertions.assertEquals(
        int.class,
        LambdaHelper.<SerFunc<Integer, Integer[][]>>resolve(Integer[][]::new)
            .getParameterTypes()[0]);
    Assertions.assertEquals(
        Integer[][].class,
        LambdaHelper.<SerFunc<Integer, Integer[][]>>resolve(Integer[][]::new).getReturnType());
    Assertions.assertEquals(
        Object.class,
        LambdaHelper.<SerCons<Object>>resolve(System.out::println).getParameterTypes()[0]);
    Assertions.assertEquals(
        void.class, LambdaHelper.<SerCons<Object>>resolve(System.out::println).getReturnType());
    Assertions.assertEquals(
        String[].class,
        LambdaHelper.<SerCons<String[]>>resolve((String[] stringList) -> {})
            .getParameterTypes()[0]);
    Assertions.assertEquals(
        String[].class,
        LambdaHelper.<SerCons<String[]>>resolve((String[] stringList) -> {})
            .getParameterTypes()[0]);
    Assertions.assertEquals(
        void.class,
        LambdaHelper.<SerCons<String[]>>resolve((String[] stringList) -> {}).getReturnType());
    Assertions.assertEquals(
        List.class,
        LambdaHelper.<SerCons<List<String>>>resolve((List<String> stringList) -> {})
            .getParameterTypes()[0]);
    Assertions.assertEquals(
        void.class,
        LambdaHelper.<SerCons<List<String>>>resolve((List<String> stringList) -> {})
            .getReturnType());
    Assertions.assertEquals(
        0,
        LambdaHelper.<SerFunc<Object, String>>resolve(Object::toString).getParameterTypes().length);
    Assertions.assertEquals(
        String.class,
        LambdaHelper.<SerFunc<Object, String>>resolve(Object::toString).getReturnType());
    Assertions.assertEquals(
        Void.class,
        LambdaHelper.resolve((Serializable & Function<Void, Void>) w -> w).getReturnType());
    Assertions.assertEquals(
        new AbstractTypeReference<T[]>() {}.getTypeName(),
        LambdaHelper.resolve((SerArgsFunc<Object, Steam<?>>) Steam::of)
            .getParameterTypes()[0]
            .getTypeName());
    Assertions.assertEquals(
        Steam.class.getName(),
        LambdaHelper.resolve((SerArgsFunc<Object, Steam<?>>) Steam::of)
            .getReturnType()
            .getTypeName());
  }

  @Test
  void testGetPropertyNames() {
    List<String> propertyNames =
        LambdaHelper.getPropertyNames(LambdaExecutable::getName, LambdaExecutable::getLambda);
    Assertions.assertEquals("name", propertyNames.get(0));
    Assertions.assertEquals("lambda", propertyNames.get(1));
  }

  @Test
  @SneakyThrows
  @SuppressWarnings("unchecked")
  void testProxy() {
    final LambdaExecutable resolve = LambdaHelper.<SerFunc<A, ?>>resolve(A::getR);
    final Type type = resolve.getInstantiatedTypes()[0];
    final Map<String, Type> genericMap = ReflectHelper.getGenericMap(type);
    Assertions.assertEquals(A.class, genericMap.get("R"));
    Assertions.assertEquals(
        int.class,
        LambdaHelper.<SerFunc<Integer, String[]>>resolve(String[]::new).getParameterTypes()[0]);
    Assertions.assertEquals(
        int.class,
        LambdaHelper.<SerFunc<Integer, Integer[][]>>resolve(Integer[][]::new)
            .getParameterTypes()[0]);
    Assertions.assertEquals(
        Object.class,
        LambdaHelper.<SerCons<Object>>resolve(System.out::println).getParameterTypes()[0]);

    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodHandle getR = lookup.findVirtual(B.class, "getR", MethodType.methodType(Object.class));
    SerFunc<A, A> lambda = MethodHandleProxies.asInterfaceInstance(SerFunc.class, getR);
    LambdaExecutable lambdaExecutable = LambdaHelper.resolve(lambda);
    Assertions.assertEquals(B.class, LambdaHelper.resolve(lambda).getInstantiatedTypes()[0]);
  }

  private abstract static class B<T, R> {

    R getR() {
      return null;
    }
  }

  private static class A extends B<Object, A> {}

  @Test
  void testRevert() {
    final LambdaExecutable getName =
        LambdaHelper.<SerFunc<LambdaExecutable, String>>resolve(LambdaExecutable::getName);
    final SerFunc<LambdaExecutable, String> revertedGetName =
        LambdaHelper.revert(SerFunc.class, getName.getExecutable());
    Assertions.assertEquals(revertedGetName.apply(getName), getName.getName());

    final LambdaExecutable constructor =
        LambdaHelper.<SerSupp<LambdaExecutable>>resolve(LambdaExecutable::new);
    final SerSupp<LambdaExecutable> revertedConstructor =
        LambdaHelper.revert(SerSupp.class, constructor.getExecutable());
    Assertions.assertEquals(LambdaExecutable.class, revertedConstructor.get().getClass());
  }

  @Test
  @SneakyThrows
  @SuppressWarnings("unchecked")
  void testVirtual() {
    final MethodHandle virtual =
        MethodHandles.lookup()
            .findVirtual(LambdaExecutable.class, "getName", MethodType.methodType(String.class));
    final SerFunc<LambdaExecutable, String> proxy =
        MethodHandleProxies.asInterfaceInstance(SerFunc.class, virtual);
    InvocationHandler handler = Proxy.getInvocationHandler(proxy);
    MethodHandle methodHandle = ReflectHelper.getFieldValue(handler, "val$target");
    final CallSite callSite =
        LambdaMetafactory.altMetafactory(
            MethodHandles.lookup(),
            "apply",
            MethodType.methodType(SerFunc.class),
            MethodType.methodType(Object.class, Object.class),
            methodHandle,
            MethodType.methodType(String.class, LambdaExecutable.class),
            LambdaMetafactory.FLAG_SERIALIZABLE);
    final MethodHandle target = callSite.getTarget();
    final SerFunc<LambdaExecutable, String> invoke =
        (SerFunc<LambdaExecutable, String>) target.invoke();
    final LambdaExecutable executable = new LambdaExecutable();
    executable.setName("test");
    Assertions.assertEquals("test", invoke.apply(executable));
    Assertions.assertEquals("getName", LambdaHelper.resolve(invoke).getName());
  }

  @Test
  void testGetGetter() {
    SerFunc<LambdaExecutable, String> nameGetter =
        LambdaHelper.getGetter(LambdaExecutable.class, "name");
    String name = nameGetter.apply(LambdaHelper.resolve(nameGetter));
    Assertions.assertEquals("getName", name);
    val lambda = LambdaHelper.getGetter(LambdaExecutable.class, "clazz", Function.class);
    Function<LambdaExecutable, Class<?>> clazzGetter =
        SerFunc.<Function<?, ?>, Function<LambdaExecutable, Class<?>>>cast().apply(lambda);
    Class<?> clazz =
        clazzGetter.apply(
            new LambdaExecutable() {
              {
                setClazz(String.class);
              }
            });
    Assertions.assertEquals(String.class, clazz);
  }

  @Test
  void testGetSetter() {
    SerBiCons<LambdaExecutable, String> nameSetter =
        LambdaHelper.getSetter(LambdaExecutable.class, "name");
    LambdaExecutable executable = new LambdaExecutable();
    nameSetter.accept(executable, "kubernetes");
    Assertions.assertEquals("kubernetes", executable.getName());
    val lambda = LambdaHelper.getSetter(LambdaExecutable.class, "clazz", BiConsumer.class);
    BiConsumer<LambdaExecutable, Class<?>> clazzSetter =
        SerFunc.<BiConsumer<?, ?>, BiConsumer<LambdaExecutable, Class<?>>>cast().apply(lambda);
    clazzSetter.accept(executable, String.class);
    Assertions.assertEquals(String.class, executable.getClazz());
    clazzSetter = LambdaHelper.getSetter(LambdaExecutable::getClazz);
    clazzSetter.accept(executable, Object.class);
    Assertions.assertEquals(Object.class, executable.getClazz());
    SerBiCons<LambdaExecutable, String> nameSerSetter =
        LambdaHelper
            .<SerFunc<LambdaExecutable, String>, SerBiCons<LambdaExecutable, String>>getSetter(
                LambdaExecutable::getName, SerBiCons.class);
    nameSerSetter.accept(executable, "serializable");
    Assertions.assertEquals("serializable", executable.getName());
  }

  @Test
  void testGetSetterMap() {
    Map<SerFunc<LambdaExecutable, Object>, Serializable> getterSetterMap =
        LambdaHelper.getGetterSetterMap(LambdaExecutable.class);
    LambdaExecutable lambdaExecutable =
        LambdaHelper.resolve(
            (Serializable & Function<LambdaExecutable, String>) LambdaExecutable::getName);
    LambdaExecutable lambda = new LambdaExecutable();
    getterSetterMap.forEach(
        (getter, setter) -> {
          Object value = getter.apply(lambdaExecutable);
          SerFunc.<Serializable, SerBiCons<LambdaExecutable, Object>>cast()
              .apply(setter)
              .accept(lambda, value);
        });
    Assertions.assertEquals(lambdaExecutable.getLambda(), lambda.getLambda());
  }

  @Test
  void testGetPropertyGetterSetterMap() {
    Map<String, Map.Entry<SerFunc<LambdaExecutable, Object>, Serializable>>
        propertyGetterSetterMap = LambdaHelper.getPropertyGetterSetterMap(LambdaExecutable.class);
    LambdaExecutable executable = new LambdaExecutable();
    SerFunc.<Serializable, SerBiCons<LambdaExecutable, Object>>cast()
        .apply(propertyGetterSetterMap.get("name").getValue())
        .accept(executable, "test");
    Assertions.assertEquals("test", executable.getName());
  }
}
