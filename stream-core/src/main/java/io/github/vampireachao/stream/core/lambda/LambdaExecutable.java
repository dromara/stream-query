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

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Similar to a Java 8 Executable but with a return type.
 * reference apache-flink
 *
 * @author VampireAchao
 */
public class LambdaExecutable {

    private final Type[] instantiatedTypes;
    private final Type[] parameterTypes;
    private final Type returnType;
    private final String name;
    private final Executable executable;
    private final Class<?> clazz;
    private final SerializedLambda lambda;

    public LambdaExecutable(Executable executable, SerializedLambda lambda) {
        if (executable instanceof Method) {
            Method method = (Method) executable;
            this.parameterTypes = method.getGenericParameterTypes();
            this.returnType = method.getGenericReturnType();
            this.name = method.getName();
        } else if (executable instanceof Constructor) {
            Constructor<?> constructor = (Constructor<?>) executable;
            this.parameterTypes = constructor.getGenericParameterTypes();
            this.returnType = constructor.getDeclaringClass();
            this.name = constructor.getName();
        } else {
            throw new IllegalArgumentException("Unsupported executable type: " + executable.getClass());
        }
        int index = lambda.getInstantiatedMethodType().indexOf(";)");
        if (index > -1) {
            boolean isArray = lambda.getInstantiatedMethodType().startsWith("([");
            if (isArray) {
                try {
                    this.instantiatedTypes = new Type[]{Class.forName(lambda.getInstantiatedMethodType().replace("/", ".").substring(0, index).substring(1) + ";", true, Thread.currentThread().getContextClassLoader())};
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                String[] instantiatedTypeNames = lambda.getInstantiatedMethodType().substring(2, index).split(";L");
                this.instantiatedTypes = new Type[instantiatedTypeNames.length];
                for (int i = 0; i < instantiatedTypeNames.length; i++) {
                    try {
                        this.instantiatedTypes[i] = Thread.currentThread().getContextClassLoader().loadClass(instantiatedTypeNames[i].replace("/", "."));
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        } else {
            instantiatedTypes = new Type[0];
        }
        this.clazz = ReflectHelper.getFieldValue(executable, "clazz");
        this.executable = executable;
        this.lambda = lambda;
    }

    public Type[] getInstantiatedTypes() {
        return instantiatedTypes;
    }

    public Type[] getParameterTypes() {
        return parameterTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public Executable getExecutable() {
        return executable;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public SerializedLambda getLambda() {
        return lambda;
    }
}
