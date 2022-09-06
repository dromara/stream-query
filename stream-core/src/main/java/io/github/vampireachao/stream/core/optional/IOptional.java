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

    Optional<T> toOptional();

    P flatOptional(Function<? super T, Optional<T>> mapper);

    @SuppressWarnings("all")
    P orOptional(Optional<? extends T> other);
}
