package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.lambda.function.SerCons;

import java.util.Objects;

/**
 * @author VampireAchao
 * @since 2022/9/26 17:50
 */
public abstract class BaseQuery<T, K, V> {

    final SFunction<T, K> keyFunction;
    SFunction<T, V> valueFunction;
    LambdaQueryWrapper<T> wrapper;
    boolean isParallel = false;
    SerCons<T> peekConsumer = SerCons.nothing();

    protected BaseQuery(SFunction<T, K> keyFunction) {
        this.keyFunction = Objects.requireNonNull(keyFunction);
        this.wrapper = Database.lambdaQuery(keyFunction);
    }
}
