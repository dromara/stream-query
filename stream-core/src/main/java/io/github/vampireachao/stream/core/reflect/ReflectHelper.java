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
package io.github.vampireachao.stream.core.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 反射工具类
 *
 * @author VampireAchao
 * @since 2022/6/2 17:02
 */
public class ReflectHelper {

    private ReflectHelper() {
        /* Do not new me! */
    }


    /**
     * Returns all declared methods of a class including methods of superclasses.
     *
     * @param clazz The class to get the declared methods for.
     * @return An array of all declared methods of the class.
     */
    public static List<Method> getAllDeclaredMethods(Class<?> clazz) {
        List<Method> result = new ArrayList<>();
        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            Collections.addAll(result, methods);
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    /**
     * return accessible accessibleObject
     *
     * @param accessibleObject     accessibleObject method
     * @param <$ACCESSIBLE_OBJECT> accessibleObject type
     * @return accessibleObject
     */
    public static <$ACCESSIBLE_OBJECT extends AccessibleObject> $ACCESSIBLE_OBJECT accessible($ACCESSIBLE_OBJECT accessibleObject) {
        return AccessController.doPrivileged((PrivilegedAction<$ACCESSIBLE_OBJECT>) () -> {
            accessibleObject.setAccessible(true);
            return accessibleObject;
        });
    }

    /**
     * return descriptor of executable
     *
     * @param executable executable
     * @return descriptor
     */
    public static String getDescriptor(Executable executable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        Class<?>[] parameters = executable.getParameterTypes();
        for (Class<?> parameter : parameters) {
            appendDescriptor(parameter, stringBuilder);
        }
        if (executable instanceof Method) {
            Method method = (Method) executable;
            stringBuilder.append(')');
            appendDescriptor(method.getReturnType(), stringBuilder);
            return stringBuilder.toString();
        } else if (executable instanceof Constructor) {
            return stringBuilder.append(")V").toString();
        }
        throw new IllegalArgumentException("Unknown Executable: " + executable);
    }

    /**
     * append descriptor
     *
     * @param clazz         clazz
     * @param stringBuilder stringBuilder
     */
    private static void appendDescriptor(Class<?> clazz, StringBuilder stringBuilder) {
        Class<?> currentClass;
        for (currentClass = clazz;
             currentClass.isArray();
             currentClass = currentClass.getComponentType()) {
            stringBuilder.append('[');
        }
        if (currentClass.isPrimitive()) {
            char descriptor;
            if (long.class == currentClass) {
                descriptor = 'J';
            } else if (int.class == currentClass) {
                descriptor = 'I';
            } else if (short.class == currentClass) {
                descriptor = 'S';
            } else if (char.class == currentClass) {
                descriptor = 'C';
            } else if (byte.class == currentClass) {
                descriptor = 'B';
            } else if (double.class == currentClass) {
                descriptor = 'D';
            } else if (float.class == currentClass) {
                descriptor = 'F';
            } else if (boolean.class == currentClass) {
                descriptor = 'Z';
            } else if (void.class == currentClass) {
                descriptor = 'V';
            } else {
                throw new AssertionError();
            }
            stringBuilder.append(descriptor);
        } else {
            stringBuilder.append('L').append(getInternalName(currentClass)).append(';');
        }

    }

    /**
     * get internal name
     *
     * @param clazz clazz
     * @return internal name
     */
    public static String getInternalName(Class<?> clazz) {
        return clazz.getName().replace('.', '/');
    }
}
