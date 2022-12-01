package io.github.vampireachao.stream.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * @author VampireAchao
 * @since 2022/10/21 17:13
 */
class SetsTest {

    @Test
    void testOf() {
        Assertions.assertEquals("value", Sets.of("value").iterator().next());
    }

    @Test
    void testOfColl() {
        Assertions.assertTrue(Sets.ofColl(Collections.emptySet()).isEmpty());
    }
}
