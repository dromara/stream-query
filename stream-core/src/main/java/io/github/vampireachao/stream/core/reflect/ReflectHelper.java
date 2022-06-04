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

import sun.invoke.util.Wrapper;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;


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
            char descriptor = 0;
            for (Wrapper wrapper : Wrapper.class.getEnumConstants()) {
                if (currentClass.getSimpleName().toUpperCase(Locale.ROOT).equals(wrapper.name())) {
                    descriptor = wrapper.basicTypeChar();
                    break;
                }
            }
            if (descriptor == 0) {
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


    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object obj, String fieldName) {
        if (Objects.isNull(obj) || Objects.isNull(fieldName)) {
            return null;
        }
        try {
            return (T) getField(obj.getClass(), fieldName).get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Field getField(Class<?> aClass, String fieldName) {
        try {
            return accessible(aClass.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
