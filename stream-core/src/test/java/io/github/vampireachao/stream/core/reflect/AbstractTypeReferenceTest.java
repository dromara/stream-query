package io.github.vampireachao.stream.core.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author VampireAchao ZVerify
 * @since 2022/7/15 14:44
 */
class AbstractTypeReferenceTest {

    @Test
    void testGetTypeName() {
        Assertions.assertEquals("java.lang.String", new AbstractTypeReference<String>() {}.getTypeName());
        Assertions.assertEquals("java.util.ArrayList<java.lang.String>", new AbstractTypeReference<ArrayList<String>>() {}.getTypeName());
    }

    @Test
    void testGetType() {
        Assertions.assertEquals(String.class, new AbstractTypeReference<String>() {}.getType());
    }
}
