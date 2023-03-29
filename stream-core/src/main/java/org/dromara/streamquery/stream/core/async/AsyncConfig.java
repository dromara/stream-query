package org.dromara.streamquery.stream.core.async;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author VampireAchao
 * @since 2022/12/26 18:01
 */
public class AsyncConfig {

    private AsyncInterceptor interceptor = new AsyncInterceptor() {
    };

    private int timeout = -1;

    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private Executor executor = ForkJoinPool.getCommonPoolParallelism() > 1 ? ForkJoinPool.commonPool() : r -> new Thread(r).start();

    public static AsyncConfig create() {
        return new AsyncConfig();
    }

    public AsyncInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(AsyncInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
