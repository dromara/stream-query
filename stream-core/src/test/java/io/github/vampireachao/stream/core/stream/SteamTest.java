package io.github.vampireachao.stream.core.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author VampireAchao
 * @since 2022/7/19 14:14
 */
class SteamTest {

    @Test
    void testOf() {
        Assertions.assertEquals(3, Steam.of(Arrays.asList(1, 2, 3)).count());
        Assertions.assertEquals(3, Steam.of(1, 2, 3).count());
    }

    @Test
    void testToList() {
        List<Integer> toList = Steam.of(Arrays.asList(1, 2, 3)).toList();
        Assertions.assertEquals(Arrays.asList(1, 2, 3), toList);
    }

}
