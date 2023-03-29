package org.dromara.streamquery.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.dromara.streamquery.stream.core.lambda.function.SerCons;

import java.util.Objects;

/**
 * <p>Abstract BaseQuery class.</p>
 *
 * @author VampireAchao
 * @since 2022/9/26 17:50
 */
public abstract class BaseQuery<T, K, V> {

    protected final SFunction<T, K> keyFunction;
    protected SFunction<T, V> valueFunction;
    protected LambdaQueryWrapper<T> wrapper;
    protected boolean isParallel = false;
    protected SerCons<T> peekConsumer = SerCons.nothing();

    /**
     * <p>Constructor for BaseQuery.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     */
    protected BaseQuery(SFunction<T, K> keyFunction) {
        this.keyFunction = Objects.requireNonNull(keyFunction);
        this.wrapper = Database.lambdaQuery(keyFunction);
    }
}

