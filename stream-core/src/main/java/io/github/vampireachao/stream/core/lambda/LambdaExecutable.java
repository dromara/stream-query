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

    private final Type[] parameterTypes;
    private final Type returnType;
    private final String name;
    private final Executable executable;

    public LambdaExecutable(Constructor<?> constructor) {
        this.parameterTypes = constructor.getGenericParameterTypes();
        this.returnType = constructor.getDeclaringClass();
        this.name = constructor.getName();
        this.executable = constructor;
    }

    public LambdaExecutable(Method method) {
        this.parameterTypes = method.getGenericParameterTypes();
        this.returnType = method.getGenericReturnType();
        this.name = method.getName();
        this.executable = method;
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

    public boolean executablesEquals(Method m) {
        return executable.equals(m);
    }

    public boolean executablesEquals(Constructor<?> c) {
        return executable.equals(c);
    }
}
