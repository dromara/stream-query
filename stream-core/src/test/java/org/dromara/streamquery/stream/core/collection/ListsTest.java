package org.dromara.streamquery.stream.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static java.util.Arrays.asList;

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
        Assertions.assertTrue(Lists.ofColl(null).isEmpty());
    }

    @Test
    void testEmpty() {
        Assertions.assertEquals(Collections.emptyList(), Lists.empty());
    }

    @Test
    void testFirst() {
        Assertions.assertEquals(1, Lists.first(Lists.of(1, 2, 3)));
        Assertions.assertNull(Lists.first(null));
        Assertions.assertNull(Lists.first(Lists.empty()));
    }

    @Test
    void testLast() {
        Assertions.assertEquals(3, Lists.last(Lists.of(1, 2, 3)));
        Assertions.assertNull(Lists.last(null));
        Assertions.assertNull(Lists.last(Lists.empty()));
    }

    @Test
    void testIsEmpty() {
        Assertions.assertTrue(Lists.isEmpty(Collections.emptyList()));
    }

    @Test
    void testIsNotEmpty() {
        Assertions.assertTrue(Lists.isNotEmpty(asList(1, 2, 3)));
    }

    @Test
    void testReverse() {
        Assertions.assertEquals(asList(1, 3, 2), Lists.reverse(asList(2, 3, 1)));
    }

    @Test
    void testAscend() {
        Assertions.assertEquals(asList(1, 2, 3), Lists.ascend(asList(2, 3, 1)));
    }

    @Test
    void testDescend() {
        Assertions.assertEquals(asList(3, 2, 1), Lists.descend(asList(2, 3, 1)));
    }

    @Test
    void testBinarySearch() {
        Assertions.assertEquals(2, Lists.binarySearch(Lists.of(1, 2, 3), 3));
    }

}
