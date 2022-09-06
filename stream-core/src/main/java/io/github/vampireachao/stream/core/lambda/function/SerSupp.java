package io.github.vampireachao.stream.core.lambda.function;

import io.github.vampireachao.stream.core.lambda.LambdaInvokeException;

import java.io.Serializable;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 可序列化的Supplier
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface SerSupp<T> extends Supplier<T>, Serializable {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T getting() throws Exception;

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    default T get() {
        try {
            return getting();
        } catch (Exception e) {
            throw new LambdaInvokeException(e);
        }
    }

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