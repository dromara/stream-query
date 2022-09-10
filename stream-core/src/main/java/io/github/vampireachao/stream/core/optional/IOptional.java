package io.github.vampireachao.stream.core.optional;

import java.util.Optional;
import java.util.function.Function;

/**
 * OptionalWrapper
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/9/4
 */
public interface IOptional<T, P extends BaseOp<T>> {

    /**
     * <p>toOptional.</p>
     *
     * @return a {@link java.util.Optional} object
     */
    Optional<T> toOptional();

    /**
     * <p>flatOptional.</p>
     *
     * @param mapper a {@link java.util.function.Function} object
     * @return a P object
     */
    P flatOptional(Function<? super T, Optional<T>> mapper);

    /**
     * <p>orOptional.</p>
     *
     * @param other a {@link java.util.Optional} object
     * @return a P object
     */
    @SuppressWarnings("all")
    P orOptional(Optional<? extends T> other);
}
