package io.github.vampireachao.stream.core.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author VampireAchao
 * @since 2022/7/15 14:44
 */
class TypeReferenceTest {

    @Test
    void testGetTypeName() {
        Assertions.assertEquals("java.lang.String", new TypeReference<String>() {}.getTypeName());
        Assertions.assertEquals("java.util.ArrayList<java.lang.String>", new TypeReference<ArrayList<String>>() {}.getTypeName());
    }

    @Test
    void testGetType() {
        Assertions.assertEquals(String.class, new TypeReference<String>() {}.getType());
    }
}
