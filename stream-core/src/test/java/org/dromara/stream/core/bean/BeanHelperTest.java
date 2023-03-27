package org.dromara.stream.core.bean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * BeanHelperTest
 *
 * @author VampireAchao
 * @since 2023/3/15
 */
class BeanHelperTest {

    @Test
    void testGetSetterName() {
        Assertions.assertEquals("setName", BeanHelper.getSetterName("name"));
        Assertions.assertEquals("setLambda", BeanHelper.getSetterName("lambda"));
    }

    @Test
    void testGetGetterName() {
        Assertions.assertEquals("getName", BeanHelper.getGetterName("name"));
        Assertions.assertEquals("getLambda", BeanHelper.getGetterName("lambda"));
    }

}
