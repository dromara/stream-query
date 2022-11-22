package io.github.vampireachao.stream.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

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
    void testOfColl() {
        Assertions.assertTrue(Lists.ofColl(Collections.emptySet()).isEmpty());
    }

    @Test
    void testReverse() {
        Assertions.assertEquals(Arrays.asList(1, 3, 2), Lists.reverse(Arrays.asList(2, 3, 1)));
    }

    @Test
    void testAscend() {
        Assertions.assertEquals(Arrays.asList(1, 2, 3), Lists.ascend(Arrays.asList(2, 3, 1)));
    }

    @Test
    void testDescend() {
        Assertions.assertEquals(Arrays.asList(3, 2, 1), Lists.descend(Arrays.asList(2, 3, 1)));
    }

    @Test
    void testBinarySearch() {
        Assertions.assertEquals(2, Lists.binarySearch(Lists.of(1, 2, 3), 3));
    }




}
