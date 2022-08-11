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


import io.github.vampireachao.stream.core.stream.Steam;

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

    private static final WeakHashMap<Class<?>, Field[]> CLASS_FIELDS_CACHE = new WeakHashMap<>();

    private ReflectHelper() {
        /* Do not new me! */
    }


    /**
     * Returns all declared methods of a class including methods of superclasses.
     *
     * @param clazz The class to get the declared methods for.
     * @return An array of all declared methods of the class.
     */
    public static List<Method> getMethods(Class<?> clazz) {
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
            final char descriptor;
            // see sun.invoke.util.Wrapper
            // These must be in the order defined for widening primitive conversions in JLS 5.1.2
            if (currentClass == boolean.class) {
                descriptor = 'Z';
            } else if (currentClass == byte.class) {
                descriptor = 'B';
            } else if (currentClass == short.class) {
                descriptor = 'S';
            } else if (currentClass == char.class) {
                descriptor = 'C';
            } else if (currentClass == int.class) {
                descriptor = 'I';
            } else if (currentClass == long.class) {
                descriptor = 'J';
            } else if (currentClass == float.class) {
                descriptor = 'F';
            } else if (currentClass == double.class) {
                descriptor = 'D';
            } else if (currentClass == void.class) {
                // VOID must be the last type, since it is "assignable" from any other type:
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
     * get descriptor of class
     *
     * @param clazz clazz
     * @return descriptor char
     */
    public static String getDescriptor(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        appendDescriptor(clazz, stringBuilder);
        return stringBuilder.toString();
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
            throw new IllegalArgumentException("obj or fieldName is null");
        }
        try {
            return (T) getField(obj.getClass(), fieldName).get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean hasField(Class<?> clazz, String fieldName) {
        return Steam.of(getFields(clazz)).anyMatch(f -> fieldName.equals(f.getName()));
    }

    public static Field[] getFields(Class<?> clazz) {
        return CLASS_FIELDS_CACHE.computeIfAbsent(clazz, k -> {
            Steam.Builder<Field> fieldsBuilder = Steam.builder();
            Class<?> currentClass;
            for (currentClass = clazz;
                 currentClass != null;
                 currentClass = currentClass.getSuperclass()) {
                for (Field field : currentClass.getDeclaredFields()) {
                    fieldsBuilder.add(field);
                }
            }
            return fieldsBuilder.build().toArray(Field[]::new);
        });
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        return Steam.of(getFields(clazz)).filter(field -> field.getName().equals(fieldName))
                .findFirst().map(ReflectHelper::accessible)
                .orElseThrow(() -> new IllegalArgumentException("No such field: " + fieldName));
    }

    public static Method getMethod(Class<?> clazz, String methodName) {
        return Steam.of(getMethods(clazz)).filter(method -> method.getName().equals(methodName))
                .findFirst().map(ReflectHelper::accessible)
                .orElseThrow(() -> new IllegalArgumentException("No such method: " + methodName));
    }

    public static Type[] getGenericTypes(Type paramType) {
        Type type;
        for (type = paramType;
             type instanceof Class;
             type = ((Class<?>) type).getGenericSuperclass()) {
            if (Object.class.equals(type)) {
                Type[] genericInterfaces = ((Class<?>) type).getGenericInterfaces();
                if (genericInterfaces.length > 0 && Objects.nonNull(genericInterfaces[0])) {
                    type = genericInterfaces[0];
                }
            }
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType ty = (ParameterizedType) type;
            return ty.getActualTypeArguments();
        }
        return new Type[0];
    }

    public static <T> boolean isInstance(T obj, Type t) {
        Type[] sourceTypes = ReflectHelper.getGenericTypes(t);
        if (sourceTypes.length > 0) {
            t = sourceTypes[0];
        }
        if (t instanceof ParameterizedType) {
            t = ((ParameterizedType) t).getRawType();
        }
        if (t instanceof Class) {
            Class<?> clazz = (Class<?>) t;
            return clazz.isInstance(obj);
        }
        return false;
    }

    public static boolean typeOf(Object obj, Type eType) {
        if (eType instanceof Class) {
            Class<?> clazz = (Class<?>) eType;
            return clazz.isInstance(obj);
        }
        return false;
    }

    public static <T> void setFieldValue(T bean, String keyProperty, Object fieldValue) {
        if (Objects.isNull(bean) || Objects.isNull(keyProperty)) {
            return;
        }
        try {
            Field field = bean.getClass().getDeclaredField(keyProperty);
            accessible(field);
            field.set(bean, fieldValue);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Constructor<?> getConstructorByDescriptor(final Class<?> clazz, final String methodDescriptor) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (ReflectHelper.getDescriptor(constructor).equals(methodDescriptor)) {
                return constructor;
            }
        }
        throw new IllegalStateException(String.format("No constructor found with class %s and descriptor %s", clazz, methodDescriptor));
    }

    public static Method getMethodByDescriptor(final Class<?> clazz, final String methodDescriptor) {
        for (Method method : ReflectHelper.getMethods(clazz)) {
            if (ReflectHelper.getDescriptor(method).equals(methodDescriptor)) {
                return method;
            }
        }
        throw new IllegalStateException(String.format("No method found with class %s and descriptor %s", clazz, methodDescriptor));
    }

    public static Type[] getArgsFromDescriptor(final String methodDescriptor) {
        int index = methodDescriptor.indexOf(";)");
        if (index == -1) {
            return new Type[0];
        }
        boolean isArray = methodDescriptor.startsWith("([");
        if (isArray) {
            final String className = methodDescriptor
                    .substring(0, index)
                    .substring(1)
                    + ";";
            return new Type[]{loadClass(className)};
        } else {
            String[] instantiatedTypeNames = methodDescriptor.substring(2, index).split(";L");
            final Type[] types = new Type[instantiatedTypeNames.length];
            for (int i = 0; i < instantiatedTypeNames.length; i++) {
                try {
                    types[i] = Thread.currentThread().getContextClassLoader()
                            .loadClass(instantiatedTypeNames[i].replace("/", "."));
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(e);
                }
            }
            return types;
        }
    }

    public static Class<?> loadClass(final String className) {
        try {
            return Class.forName(className.replace("/", "."),
                    true,
                    Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <R> R invoke(Object obj, String methodName, Object... args) {
        try {
            return (R) accessible(getMethod(obj.getClass(), methodName)).invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
