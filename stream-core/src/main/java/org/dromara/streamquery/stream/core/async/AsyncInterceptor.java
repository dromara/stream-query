package org.dromara.streamquery.stream.core.async;

import org.dromara.streamquery.stream.core.lambda.LambdaInvokeException;

import java.util.function.Supplier;

/**
 * @author VampireAchao
 * @since 2022/12/26 18:04
 */
public interface AsyncInterceptor {

    default void before() {
    }

    default <T> T execute(Supplier<T> supplier) {
        return supplier.get();
    }

    default void after() {
    }

    default <T> T onError(Throwable throwable) {
        throw new LambdaInvokeException(throwable);
    }

}
