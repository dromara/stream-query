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
        final InvocationHandler handler = Proxy.getInvocationHandler(proxy);
        final MethodHandle methodHandle = ReflectHelper.getFieldValue(handler, "val$target");
        final Executable executable = MethodHandles.reflectAs(Executable.class, methodHandle);
        return new LambdaExecutable(executable);
        /*System.out.println("executable: " + executable + " class: " + executable.getClass());
        System.out.println("methodHandle: " + methodHandle + " class: " + methodHandle.getClass());
        System.out.println(Steam.of(ReflectHelper.getFields(methodHandle.getClass())).map(Field::getName).toMap(Function.identity(), name -> ReflectHelper.getFieldValue(methodHandle, name)));
        if (ReflectHelper.hasField(methodHandle.getClass(), "member")) {
            Member member = ReflectHelper.getFieldValue(methodHandle, "member");
            System.out.println("member: " + member);
            System.out.println(Steam.of(member.getClass().getDeclaredFields()).map(Field::getName).toMap(Function.identity(), fieldName -> ReflectHelper.getFieldValue(member, fieldName)));
            Class<?> clazz = member.getDeclaringClass();
            System.out.println("clazz: " + clazz);
            String name = member.getName();
            System.out.println("name: " + name);
            MethodType type = ReflectHelper.getFieldValue(member, "type");
            System.out.println("type: " + type + " type.getClass()): " + type.getClass());
            System.out.println(Steam.of(type.getClass().getDeclaredFields()).map(Field::getName).toMap(Function.identity(), fieldName -> ReflectHelper.getFieldValue(type, fieldName)));
            System.out.println("toMethodDescriptorString: " + type.toMethodDescriptorString());
            lambdaExecutable.setInstantiatedTypes(ReflectHelper.getArgsFromDescriptor(type.toMethodDescriptorString()));
        } else {
            if (methodHandle.getClass().getName().equals("java.lang.invoke.BoundMethodHandle$Species_LL")) {
                final Object speciesData = ReflectHelper.invoke(methodHandle, "speciesData");
                System.out.println("speciesData: " + speciesData + " class: " + speciesData.getClass());
                System.out.println(Steam.of(ReflectHelper.getFields(speciesData.getClass())).map(Field::getName).toMap(Function.identity(), name -> ReflectHelper.getFieldValue(speciesData, name)));
            }
        }
        return lambdaExecutable;*/
    }
}
