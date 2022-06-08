package io.github.vampireachao.stream.core.lambda.function;

import java.io.Serializable;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 可序列化的Supplier
 *
 * @author VampireAchao
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface SerSupp<T> extends Supplier<T>, Serializable {

    /**
     * last
     *
     * @param serSups lambda
     * @param <T>     type
     * @return lambda
     */
    @SafeVarargs
    static <T> SerSupp<T> last(SerSupp<T>... serSups) {
        return Stream.of(serSups).reduce((l, r) -> r).orElseGet(() -> () -> null);
    }

}