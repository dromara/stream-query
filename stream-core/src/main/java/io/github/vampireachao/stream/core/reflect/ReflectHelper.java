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


import io.github.vampireachao.stream.core.lambda.function.SerPred;
import io.github.vampireachao.stream.core.optional.Opp;
import io.github.vampireachao.stream.core.stream.Steam;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;


/**
 * 反射工具类
 *
 * @author VampireAchao
 * @since 2022/6/2 17:02
 */
public class ReflectHelper {

    public static final String LEFT_MIDDLE_BRACKET = "[";
    public static final String SEMICOLON = ";";
    public static final String L = "L";
    public static final String V = "V";


    private static final WeakHashMap<Class<?>, List<Field>> CLASS_FIELDS_CACHE = new WeakHashMap<>();
    private static final WeakHashMap<Class<?>, List<Method>> CLASS_METHODS_CACHE = new WeakHashMap<>();

    private ReflectHelper() {
        /* Do not new me! */
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

    public static List<Field> getFields(Class<?> clazz) {
        return CLASS_FIELDS_CACHE.computeIfAbsent(clazz, k -> {
            Steam.Builder<Field> fieldsBuilder = Steam.builder();
            Class<?> currentClass;
            for (currentClass = clazz;
                 currentClass != null;
                 currentClass = currentClass.getSuperclass()) {
                for (Field field : currentClass.getDeclaredFields()) {
                    fieldsBuilder.add(accessible(field));
                }
            }
            return fieldsBuilder.build().toList();
        });
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        return Steam.of(getFields(clazz)).filter(field -> field.getName().equals(fieldName))
                .findFirst().map(ReflectHelper::accessible)
                .orElseThrow(() -> new IllegalArgumentException("No such field: " + fieldName));
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        return Steam.of(getMethods(clazz)).filter(SerPred.multiAnd(
                method -> method.getName().equals(methodName),
                method -> Arrays.equals(method.getParameterTypes(), parameterTypes)
        )).findFirst().map(ReflectHelper::accessible).orElseThrow(() -> new IllegalArgumentException(
                String.format("No such method: %s args: %s",
                        methodName
                        , Steam.of(parameterTypes).map(Type::getTypeName).join(","))));
    }

    /**
     * Returns all declared methods of a class including methods of superclasses.
     *
     * @param clazz The class to get the declared methods for.
     * @return An array of all declared methods of the class.
     */
    public static List<Method> getMethods(Class<?> clazz) {
        return CLASS_METHODS_CACHE.computeIfAbsent(clazz, k -> Steam.<Class<?>>iterate(
                        clazz, Objects::nonNull, Class::getSuperclass)
                .flat(clz -> Steam.of(clz.getDeclaredMethods())).toList());
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
        final String className = methodDescriptor.substring(1, index + 1);
        String[] instantiatedTypeNames = className.split(";");
        final Type[] types = new Type[instantiatedTypeNames.length];
        for (int i = 0; i < instantiatedTypeNames.length; i++) {
            types[i] = forClassName(instantiatedTypeNames[i]);
        }
        return types;
    }

    public static Type getReturnTypeFromDescriptor(final String methodDescriptor) {
        int index = methodDescriptor.indexOf(";)");
        if (index == -1) {
            return null;
        }
        String className = methodDescriptor.substring(index + 2);
        if (V.equals(className)) {
            return null;
        }
        return forClassName(className);
    }

    public static Class<?> loadClass(final String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className.replace("/", "."));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Class<?> forClassName(String className) {
        try {
            boolean isArray = className.startsWith(LEFT_MIDDLE_BRACKET);
            if (isArray && !className.endsWith(SEMICOLON)) {
                className += SEMICOLON;
            }
            if (!isArray && className.startsWith(L)) {
                className = className.substring(1);
            }
            if (!isArray && className.endsWith(SEMICOLON)) {
                className = className.substring(0, className.length() - 1);
            }
            return Class.forName(className.replace("/", "."));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <R> R invoke(Object obj, Method method, Object... args) {
        try {
            return (R) accessible(method).invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void explain(Object obj) {
        System.out.printf("obj: %s class: %s\n", obj, obj.getClass());
        System.out.println("fields: ");
        Steam.of(getFields(obj.getClass())).map(Field::getName).forEach(fieldName -> System.out.println("field " +
                "" + fieldName + ": " + getFieldValue(obj, fieldName)));
        System.out.println("no arg methods: ");
        Steam.of(getMethods(obj.getClass())).map(Method::getName).forEach(methodName -> System.out.println("method " + methodName + ": " + Opp.ofTry(() -> getMethod(obj.getClass(), methodName).invoke(obj))));
    }
}
