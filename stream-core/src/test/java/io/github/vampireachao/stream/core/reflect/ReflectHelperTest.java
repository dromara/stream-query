package io.github.vampireachao.stream.core.reflect;

import io.github.vampireachao.stream.core.lambda.LambdaHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author VampireAchao
 * @since 2022/6/7 13:49
 */
class ReflectHelperTest {

    @Test
    void testGetDescriptor() {
        Assertions.assertEquals("()V", ReflectHelper.getDescriptor(LambdaHelper.resolve((Serializable & Runnable) () -> {}).getExecutable()));
        Assertions.assertEquals("Ljava/lang/Void;", ReflectHelper.getDescriptor(Void.class));
        Assertions.assertEquals("Z", ReflectHelper.getDescriptor(boolean.class));
        Assertions.assertEquals("Ljava/lang/Boolean;", ReflectHelper.getDescriptor(Boolean.class));
    }

    @Test
    void testTypeReference() {
        Assertions.assertEquals("java.lang.String", new TypeReference<String>() {}.getTypeName());
        Assertions.assertEquals("java.util.ArrayList<java.lang.String>", new TypeReference<ArrayList<String>>() {}.getTypeName());
    }
}

