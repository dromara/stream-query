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

import org.dromara.streamquery.stream.core.reflect.ReflectHelper;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.List;

/**
 * Similar to a Java 8 Executable but with a return type. reference apache-flink
 *
 * @author VampireAchao Cizai_
 */
public class LambdaExecutable {

  /** Constant <code>{@code CONSTRUCTOR_METHOD_NAME="<init>"}</code> */
  public static final String CONSTRUCTOR_METHOD_NAME = "<init>";
  /** Constant <code>{@code NEW_INSTANCE_METHOD_NAME="newInstance"}</code> */
  public static final String NEW_INSTANCE_METHOD_NAME = "newInstance";

  private Executable executable;
  private Type[] instantiatedTypes;
  private Type[] parameterTypes;
  private Type returnType;
  private String name;
  private Class<?> clazz;
  private SerializedLambda lambda;
  private Proxy proxy;

  /** Constructor for LambdaExecutable. */
  public LambdaExecutable() {
    // this is an accessible parameterless constructor.
  }

  /**
   * Constructor for LambdaExecutable.
   *
   * @param lambda a {@link java.lang.invoke.SerializedLambda} object
   */
  public LambdaExecutable(final SerializedLambda lambda) {
    final MethodType methodType =
        MethodType.fromMethodDescriptorString(
            lambda.getInstantiatedMethodType(), Thread.currentThread().getContextClassLoader());
    final Class<?>[] curInstantiatedTypes = ReflectHelper.getFieldValue(methodType, "ptypes");
    final Class<?> curReturnType = ReflectHelper.getFieldValue(methodType, "rtype");
    try {
      Class<?> implClass = ReflectHelper.loadClass(lambda.getImplClass());
      if (CONSTRUCTOR_METHOD_NAME.equals(lambda.getImplMethodName())) {
        initConstructor(
            ReflectHelper.getConstructorByDescriptor(implClass, lambda.getImplMethodSignature()));
      } else {
        initMethod(
            ReflectHelper.getMethodByDescriptor(
                lambda.getImplMethodName(), implClass, lambda.getImplMethodSignature()));
      }
    } catch (IllegalStateException e) {
      this.setParameterTypes(ReflectHelper.getArgsFromDescriptor(lambda.getImplMethodSignature()));
      this.setName(lambda.getImplMethodName());
    }
    this.setInstantiatedTypes(curInstantiatedTypes);
    this.setReturnType(curReturnType);
    this.setLambda(lambda);
  }

  /**
   * Constructor for LambdaExecutable.
   *
   * @param executable a {@link java.lang.reflect.Executable} object
   */
  public LambdaExecutable(final Executable executable) {
    if (executable instanceof Constructor) {
      initConstructor((Constructor<?>) executable);
    } else {
      initMethod((Method) executable);
    }
  }

  private void initConstructor(Constructor<?> constructor) {
    this.executable = constructor;
    this.parameterTypes = constructor.getGenericParameterTypes();
    this.returnType = constructor.getDeclaringClass();
    this.name = constructor.getName();
    this.clazz = constructor.getDeclaringClass();
  }

  private void initMethod(Method method) {
    this.executable = method;
    this.parameterTypes = method.getGenericParameterTypes();
    this.returnType = method.getGenericReturnType();
    this.name = method.getName();
    this.clazz = method.getDeclaringClass();
  }

  /**
   * initProxy.
   *
   * @param proxy a {@link java.lang.reflect.Proxy} object
   * @return a {@link LambdaExecutable} object
   */
  public static LambdaExecutable initProxy(Proxy proxy) {
    InvocationHandler handler = Proxy.getInvocationHandler(proxy);
    MethodHandle methodHandle = ReflectHelper.getFieldValue(handler, "val$target");
    Executable executable = MethodHandles.reflectAs(Executable.class, methodHandle);
    Class<Serializable> lambdaClazz = ReflectHelper.getFieldValue(handler, "val$intfc");
    Serializable lambda = LambdaHelper.revert(lambdaClazz, executable);
    try {
      final LambdaExecutable resolve = LambdaHelper.resolve(lambda);
      /*final MethodType instantiatedMethodType = methodHandle.type();
      final Type[] parameterTypes = ReflectHelper.invoke(instantiatedMethodType, ReflectHelper.getMethod(MethodType.class, "ptypes"));
      resolve.setInstantiatedTypes(parameterTypes);*/
      return resolve;
    } catch (IllegalArgumentException e) {
      // array constructor reference is not direct method handle
      final LambdaExecutable lambdaExecutable = notDirectMethodHandle(methodHandle);
      lambdaExecutable.setProxy(proxy);
      return lambdaExecutable;
    }
  }

  /**
   * Getter for the field <code>executable</code>.
   *
   * @return a {@link java.lang.reflect.Executable} object
   */
  public Executable getExecutable() {
    return executable;
  }

  /**
   * Setter for the field <code>executable</code>.
   *
   * @param executable a {@link java.lang.reflect.Executable} object
   */
  public void setExecutable(Executable executable) {
    this.executable = executable;
  }

  /**
   * Getter for the field <code>instantiatedTypes</code>.
   *
   * @return an array of {@link java.lang.reflect.Type} objects
   */
  public Type[] getInstantiatedTypes() {
    return instantiatedTypes;
  }

  /**
   * Setter for the field <code>instantiatedTypes</code>.
   *
   * @param instantiatedTypes an array of {@link java.lang.reflect.Type} objects
   */
  public void setInstantiatedTypes(Type[] instantiatedTypes) {
    this.instantiatedTypes = instantiatedTypes;
  }

  /**
   * Getter for the field <code>parameterTypes</code>.
   *
   * @return an array of {@link java.lang.reflect.Type} objects
   */
  public Type[] getParameterTypes() {
    return parameterTypes;
  }

  /**
   * Setter for the field <code>parameterTypes</code>.
   *
   * @param parameterTypes an array of {@link java.lang.reflect.Type} objects
   */
  public void setParameterTypes(Type[] parameterTypes) {
    this.parameterTypes = parameterTypes;
  }

  /**
   * Getter for the field <code>returnType</code>.
   *
   * @return a {@link java.lang.reflect.Type} object
   */
  public Type getReturnType() {
    return returnType;
  }

  /**
   * Setter for the field <code>returnType</code>.
   *
   * @param returnType a {@link java.lang.reflect.Type} object
   */
  public void setReturnType(Type returnType) {
    this.returnType = returnType;
  }

  /**
   * Getter for the field <code>name</code>.
   *
   * @return a {@link java.lang.String} object
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for the field <code>name</code>.
   *
   * @param name a {@link java.lang.String} object
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter for the field <code>clazz</code>.
   *
   * @return a {@link java.lang.Class} object
   */
  public Class<?> getClazz() {
    return clazz;
  }

  /**
   * Setter for the field <code>clazz</code>.
   *
   * @param clazz a {@link java.lang.Class} object
   */
  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  /**
   * Getter for the field <code>lambda</code>.
   *
   * @return a {@link java.lang.invoke.SerializedLambda} object
   */
  public SerializedLambda getLambda() {
    return lambda;
  }

  /**
   * Setter for the field <code>lambda</code>.
   *
   * @param lambda a {@link java.lang.invoke.SerializedLambda} object
   */
  public void setLambda(SerializedLambda lambda) {
    this.lambda = lambda;
  }

  private static LambdaExecutable notDirectMethodHandle(MethodHandle methodHandle) {
    List<Object> internalValues =
        ReflectHelper.invoke(
            methodHandle, ReflectHelper.getMethod(methodHandle.getClass(), "internalValues"));
    MethodHandle internalMethodHandle = (MethodHandle) internalValues.get(0);
    Executable internalExecutable = MethodHandles.reflectAs(Executable.class, internalMethodHandle);
    LambdaExecutable lambdaExecutable = new LambdaExecutable(internalExecutable);
    lambdaExecutable.setInstantiatedTypes(
        ReflectHelper.getArgsFromDescriptor(methodHandle.type().toMethodDescriptorString()));
    if (ReflectHelper.getMethod(Array.class, NEW_INSTANCE_METHOD_NAME, Class.class, int.class)
        .equals(internalExecutable)) {
      lambdaExecutable.setParameterTypes(new Type[] {int.class});
      lambdaExecutable.setInstantiatedTypes(new Type[] {Integer.class});
      lambdaExecutable.setReturnType(
          Array.newInstance((Class<?>) internalValues.get(1), 0).getClass());
      StackTraceElement stackTraceElement = new RuntimeException().getStackTrace()[7];
      lambdaExecutable.setClazz(ReflectHelper.forClassName(stackTraceElement.getClassName()));
      lambdaExecutable.setName(
          "lambda$"
              + stackTraceElement.getMethodName()
              + "$"
              + Integer.toHexString(methodHandle.hashCode())
              + "$1");
    }
    return lambdaExecutable;
  }

  /**
   * Getter for the field <code>proxy</code>.
   *
   * @return a {@link java.lang.reflect.Proxy} object
   */
  public Proxy getProxy() {
    return proxy;
  }

  /**
   * Setter for the field <code>proxy</code>.
   *
   * @param proxy a {@link java.lang.reflect.Proxy} object
   */
  public void setProxy(Proxy proxy) {
    this.proxy = proxy;
  }
}
