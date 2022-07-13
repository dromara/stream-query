package io.github.vampireachao.stream.core.stream;

import io.github.vampireachao.stream.core.lambda.function.SerBiCons;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * stream辅助类
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt;
 * @since 2022/5/26 19:11
 */
public class StreamHelper {

    private StreamHelper() {
        /* Do not new me! */
    }

    @SafeVarargs
    public static <E> Stream<E> peekStream(Collection<E> list, boolean isParallel, SerBiCons<E, Integer>... peeks) {
        AtomicInteger index = new AtomicInteger(-1);
        SerBiCons<E, Integer> peekChain = SerBiCons.multi(peeks);
        return StreamSupport.stream(list.spliterator(), isParallel).peek(v -> peekChain.accept(v, isParallel ? index.get() : index.incrementAndGet()));
    }
}
