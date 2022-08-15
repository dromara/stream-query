/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import io.github.vampireachao.stream.core.stream.Steam;
import sun.invoke.WrapperInstance;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.List;

/**
 * Similar to a Java 8 Executable but with a return type.
 * reference apache-flink
 *
 * @author VampireAchao
 */
public class LambdaExecutable {

    public static final String CONSTRUCTOR_METHOD_NAME = "<init>";

    private Executable executable;
    private MethodHandle methodHandle;
    private Type[] instantiatedTypes;
    private Type[] parameterTypes;
    private Type returnType;
    private String name;
    private Class<?> clazz;
    private SerializedLambda lambda;

    public LambdaExecutable() {
        // this is an accessible parameterless constructor.
    }

    public LambdaExecutable(final SerializedLambda lambda) {
        this(ReflectHelper.loadClass(lambda.getImplClass()), lambda.getImplMethodName(), lambda.getImplMethodSignature());
        this.lambda = lambda;
    }

    public LambdaExecutable(final Class<?> implClass, final String methodName, final String methodDescriptor) {
        if (CONSTRUCTOR_METHOD_NAME.equals(methodName)) {
            initConstructor(ReflectHelper.getConstructorByDescriptor(implClass, methodDescriptor));
        } else {
            initMethod(ReflectHelper.getMethodByDescriptor(implClass, methodDescriptor));
        }
        this.instantiatedTypes = ReflectHelper.getArgsFromDescriptor(methodDescriptor);
    }

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


    public Executable getExecutable() {
        return executable;
    }

    public void setExecutable(Executable executable) {
        this.executable = executable;
    }

    public MethodHandle getMethodHandle() {
        return methodHandle;
    }

    public void setMethodHandle(MethodHandle methodHandle) {
        this.methodHandle = methodHandle;
    }

    public Type[] getInstantiatedTypes() {
        return instantiatedTypes;
    }

    public void setInstantiatedTypes(Type[] instantiatedTypes) {
        this.instantiatedTypes = instantiatedTypes;
    }

    public Type[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Type[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public SerializedLambda getLambda() {
        return lambda;
    }

    public void setLambda(SerializedLambda lambda) {
        this.lambda = lambda;
    }

    public static LambdaExecutable initProxy(Proxy proxy) {
        InvocationHandler handler = Proxy.getInvocationHandler(proxy);
        MethodHandle methodHandle = ReflectHelper.getFieldValue(handler, "val$target");
        MethodType type = methodHandle.type();
        LambdaExecutable lambdaExecutable;
        try {
            lambdaExecutable = new LambdaExecutable(MethodHandles.reflectAs(Executable.class, methodHandle));
        } catch (IllegalArgumentException e) {
            // array constructor reference is not direct method handle
            // TODO fixing
            try {
                methodHandle = (MethodHandle) handler.invoke(proxy, ReflectHelper.getMethodByName(WrapperInstance.class, "getWrapperInstanceTarget"), null);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
            lambdaExecutable = new LambdaExecutable(MethodHandles.reflectAs(Executable.class, methodHandle));
        }
        lambdaExecutable.setMethodHandle(methodHandle);
        lambdaExecutable.setInstantiatedTypes(ReflectHelper.getArgsFromDescriptor(type.toMethodDescriptorString()));
        return lambdaExecutable;
    }

    private static LambdaExecutable arrayConstructorHandler(MethodHandle methodHandle, MethodType type) {
        LambdaExecutable lambdaExecutable = new LambdaExecutable();
        List<Object> internalValues = ReflectHelper.invoke(methodHandle, "internalValues");
        Class<?> arrayType = (Class<?>) Steam.of(internalValues).findLast().orElseThrow(() -> new RuntimeException("clazz not found"));
        lambdaExecutable.setParameterTypes(type.parameterArray());
        lambdaExecutable.setClazz(Array.newInstance(arrayType, 0).getClass());
        return lambdaExecutable;
    }

}
