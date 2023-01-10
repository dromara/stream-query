package io.github.vampireachao.stream.core.clazz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * ClassHelperTest
 *
 * @author VampireAchao
 * @since 2023/1/10
 */
class ClassHelperTest {

    @Test
    void testScanClasses() {
        List<Class<?>> classes = ClassHelper.scanClasses(ClassHelper.class.getPackage().getName());
        Assertions.assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(ClassHelper.class.getName())));
    }

}
