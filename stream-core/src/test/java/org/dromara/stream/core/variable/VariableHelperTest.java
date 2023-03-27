package org.dromara.stream.core.variable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author VampireAchao
 * @since 2022/10/21 15:56
 */
class VariableHelperTest {

    @Test
    void testFirst() {
        Assertions.assertEquals(1, VariableHelper.first(null, () -> 1));
        Assertions.assertEquals(1, VariableHelper.first(new Integer[]{1}, () -> null));
        Assertions.assertEquals(1, VariableHelper.first(new Integer[]{1, 2}, () -> null));
    }
}
