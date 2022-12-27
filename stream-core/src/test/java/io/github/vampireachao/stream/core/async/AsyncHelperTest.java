package io.github.vampireachao.stream.core.async;

import io.github.vampireachao.stream.core.collection.Lists;
import io.github.vampireachao.stream.core.lambda.LambdaInvokeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    @Test
    void testTimeout() {
        AsyncHelper.defaultConfig.setTimeout(1);
        AsyncHelper.defaultConfig.setTimeUnit(TimeUnit.SECONDS);
        Assertions.assertThrows(TimeoutException.class, () -> {
            try {
                AsyncHelper.supply(() -> {
                    TimeUnit.SECONDS.sleep(2);
                    return null;
                });
            } catch (LambdaInvokeException e) {
                throw e.getCause();
            }
        });
    }
}
