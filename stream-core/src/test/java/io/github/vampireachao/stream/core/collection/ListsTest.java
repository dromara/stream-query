package io.github.vampireachao.stream.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author VampireAchao
 * @since 2022/10/21 17:09
 */
class ListsTest {

    @Test
    void testOf() {
        Assertions.assertEquals("value", Lists.of("value").get(0));
    }

    @Test
    void testReverse() {
        Assertions.assertEquals(Arrays.asList(1, 3, 2), Lists.reverse(Arrays.asList(2, 3, 1)));
    }

}
