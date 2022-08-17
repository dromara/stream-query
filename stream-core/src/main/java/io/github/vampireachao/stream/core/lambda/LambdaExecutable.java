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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
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
    private Type[] instantiatedTypes;
    private Type[] parameterTypes;
    private Type returnType;
    private String name;
    private Class<?> clazz;
    private SerializedLambda lambda;
    private Proxy proxy;

    public LambdaExecutable() {
        // this is an accessible parameterless constructor.
    }

    public LambdaExecutable(final SerializedLambda lambda) {
        try {
            Class<?> implClass = ReflectHelper.loadClass(lambda.getImplClass());
            if (CONSTRUCTOR_METHOD_NAME.equals(lambda.getImplMethodName())) {
                initConstructor(ReflectHelper.getConstructorByDescriptor(implClass, lambda.getImplMethodSignature()));
            } else {
                initMethod(ReflectHelper.getMethodByDescriptor(implClass, lambda.getImplMethodSignature()));
            }
        } catch (IllegalStateException e) {
            this.parameterTypes = ReflectHelper.getArgsFromDescriptor(lambda.getImplMethodSignature());
            this.returnType = ReflectHelper.getReturnTypeFromDescriptor(lambda.getInstantiatedMethodType());
            this.name = lambda.getImplMethodName();
        }
        this.instantiatedTypes = ReflectHelper.getArgsFromDescriptor(lambda.getInstantiatedMethodType());
        this.lambda = lambda;
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
        LambdaExecutable lambdaExecutable;
        try {
            lambdaExecutable = new LambdaExecutable(MethodHandles.reflectAs(Executable.class, methodHandle));
            lambdaExecutable.setInstantiatedTypes(ReflectHelper.getArgsFromDescriptor(methodHandle.type().toMethodDescriptorString()));
        } catch (IllegalArgumentException e) {
            // array constructor reference is not direct method handle
            e.printStackTrace();
            lambdaExecutable = notDirectMethodHandle(methodHandle);
        }
        lambdaExecutable.setProxy(proxy);
        return lambdaExecutable;
    }

    private static LambdaExecutable notDirectMethodHandle(MethodHandle methodHandle) {
///        StackTraceElement stackTraceElement = new RuntimeException().getStackTrace()[3];
        List<Object> internalValues = ReflectHelper.invoke(methodHandle, ReflectHelper.getMethod(methodHandle.getClass(), "internalValues"));
        MethodHandle internalMethodHandle = (MethodHandle) internalValues.get(0);
        Executable internalExecutable = MethodHandles.reflectAs(Executable.class, internalMethodHandle);
        ReflectHelper.getMethod(Array.class, "newInstance", Class.class, int.class);
        LambdaExecutable lambdaExecutable = new LambdaExecutable(internalExecutable);
        lambdaExecutable.setInstantiatedTypes(ReflectHelper.getArgsFromDescriptor(methodHandle.type().toMethodDescriptorString()));
///        lambdaExecutable.setClazz(ReflectHelper.forClassName(stackTraceElement.getClassName()));
///        lambdaExecutable.setName("lambda$" + stackTraceElement.getMethodName() + "$" + Integer.toHexString(proxy.hashCode()) + "$2");
        return lambdaExecutable;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

}
