package io.github.vampireachao.stream.core.async;

import io.github.vampireachao.stream.core.collection.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * AsyncHelperTest
 *
 * @author VampireAchao
 * @since 2022/12/26
 */
class AsyncHelperTest {

    @Test
    void testSupply() {
        List<Integer> list = AsyncHelper.supply(() -> 1, () -> 2, () -> 3);
        Assertions.assertEquals(Lists.of(1, 2, 3), list);
    }
}
