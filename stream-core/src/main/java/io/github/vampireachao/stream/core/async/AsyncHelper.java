package io.github.vampireachao.stream.core.async;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerRunn;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author VampireAchao
 * @since 2022/12/26 16:58
 */
public class AsyncHelper {

    public static AsyncConfig defaultConfig = AsyncConfig.create();

    private AsyncHelper() {
        /* Do not new me! */
    }

    @SafeVarargs
    public static <T> List<T> supply(SerSupp<T>... suppliers) {
        return supply(defaultConfig, suppliers);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> List<T> supply(AsyncConfig asyncConfig, SerSupp<T>... suppliers) {
        final AsyncInterceptor interceptor = asyncConfig.getInterceptor();
        interceptor.before();
        final CompletableFuture<T>[] futures = Steam.of(suppliers)
                .map(supplier -> CompletableFuture.supplyAsync(() -> interceptor.execute(supplier), asyncConfig.getExecutor()))
                .toArray(CompletableFuture[]::new);
        interceptor.after();
        ((SerRunn) () -> CompletableFuture.allOf(futures).exceptionally(interceptor::onError).get(asyncConfig.getTimeout(), asyncConfig.getTimeUnit())).run();
        return Steam.of(futures).map((SerFunc<CompletableFuture<T>, T>) CompletableFuture::get).toList();
    }

}


