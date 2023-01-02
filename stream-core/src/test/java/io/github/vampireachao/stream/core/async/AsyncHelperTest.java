package io.github.vampireachao.stream.core.async;

import com.alibaba.ttl.TransmittableThreadLocal;
import io.github.vampireachao.stream.core.collection.Lists;
import io.github.vampireachao.stream.core.lambda.LambdaInvokeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

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
        final AsyncConfig asyncConfig = AsyncConfig.create();
        asyncConfig.setTimeout(1);
        asyncConfig.setTimeUnit(TimeUnit.SECONDS);
        Assertions.assertThrows(TimeoutException.class, () -> {
            try {
                AsyncHelper.supply(asyncConfig, () -> {
                    TimeUnit.SECONDS.sleep(2);
                    return null;
                });
            } catch (LambdaInvokeException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void testInterceptor() {
        ThreadLocal<Integer> threadLocal = new TransmittableThreadLocal<>();
        AsyncConfig asyncConfig = AsyncConfig.create();
        asyncConfig.setInterceptor(new AsyncInterceptor() {
            @Override
            public void before() {
                threadLocal.set(1);
            }

            @Override
            public <T> T execute(Supplier<T> supplier) {
                return TransmittableThreadLocal.Transmitter.runSupplierWithCaptured(TransmittableThreadLocal.Transmitter.capture(), supplier);
            }

            @Override
            public void after() {
                threadLocal.remove();
            }

            @Override
            public <T> T onError(Throwable throwable) {
                return AsyncInterceptor.super.onError(throwable);
            }
        });
        final Integer result = AsyncHelper.supply(asyncConfig, threadLocal::get).get(0);
        Assertions.assertEquals(1, result);
    }
}
