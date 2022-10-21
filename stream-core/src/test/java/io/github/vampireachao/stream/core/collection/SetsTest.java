package io.github.vampireachao.stream.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author VampireAchao
 * @since 2022/10/21 17:13
 */
class SetsTest {

    @Test
    void testOf() {
        Assertions.assertEquals("value", Sets.of("value").iterator().next());
    }
}
