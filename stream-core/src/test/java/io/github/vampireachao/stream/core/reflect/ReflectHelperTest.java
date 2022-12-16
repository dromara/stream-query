package io.github.vampireachao.stream.core.reflect;

import io.github.vampireachao.stream.core.lambda.LambdaHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author VampireAchao Cizai_
 * @since 2022/6/7 13:49
 */
class ReflectHelperTest {

    @Test
    void testGetDescriptor() {
        Assertions.assertEquals("()V", ReflectHelper.getDescriptor(LambdaHelper.resolve((Serializable & Runnable) () -> {
        }).getExecutable()));
        Assertions.assertEquals("Ljava/lang/Void;", ReflectHelper.getDescriptor(Void.class));
        Assertions.assertEquals("Z", ReflectHelper.getDescriptor(boolean.class));
        Assertions.assertEquals("Ljava/lang/Boolean;", ReflectHelper.getDescriptor(Boolean.class));
    }

    @Test
    void testGetGenericTypes() {
        class StringArrayList extends ArrayList<String> {
            private static final long serialVersionUID = 5735314375293577082L;
        }
        Type[] stringType = ReflectHelper.getGenericTypes(new AbstractTypeReference<String>() {
        }.getClass());
        Assertions.assertEquals(String.class, stringType[0]);
        Type[] stringArrayListType = ReflectHelper.getGenericTypes(StringArrayList.class);
        Assertions.assertEquals(String.class, stringArrayListType[0]);
        Type[] hashMapType = ReflectHelper.getGenericTypes(new HashMap<String, Object>() {
        }.getClass());
        Assertions.assertEquals(String.class, hashMapType[0]);
        Assertions.assertEquals(Object.class, hashMapType[1]);
    }

    @Test
    void testGetGenericMap() {
        final Map<String, Type> genericMap = ReflectHelper.getGenericMap(new HashMap<String, TreeMap<String, Object>>() {
        }.getClass());
        Assertions.assertEquals(String.class, genericMap.get("K"));
        Assertions.assertEquals(ParameterizedTypeImpl.make(TreeMap.class, new Type[]{String.class, Object.class}, null), genericMap.get("V"));
    }

    @Test
    void testIsInstance() {
        Assertions.assertTrue(ReflectHelper.isInstance(Collections.singletonMap(1, ""), new AbstractTypeReference<Map<?, ?>>() {
        }.getClass()));
        Assertions.assertTrue(ReflectHelper.isInstance(Collections.singletonMap(1, ""), Map.class));
    }

    @Test
    void testGetMethod() {
        Assertions.assertNotNull(ReflectHelper.getMethod(Array.class, "newInstance", Class.class, int.class));
    }

    @Test
    void testInvoke() {
        Assertions.assertNotNull(ReflectHelper.invoke(null, ReflectHelper.getMethod(Array.class, "newInstance", Class.class, int.class), Class.class, 0));
    }
}

