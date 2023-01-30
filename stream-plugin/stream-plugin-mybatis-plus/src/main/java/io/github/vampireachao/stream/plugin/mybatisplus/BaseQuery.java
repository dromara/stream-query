package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.lambda.function.SerCons;

import java.util.Objects;

/**
 * <p>Abstract BaseQuery class.</p>
 *
 * @author VampireAchao
 * @since 2022/9/26 17:50
 */
public abstract class BaseQuery<T, K, V> extends LambdaQueryWrapper<T> {

    protected final SFunction<T, K> keyFunction;
    protected SFunction<T, V> valueFunction;
    protected boolean isParallel = false;
    protected SerCons<T> peekConsumer = SerCons.nothing();

    /**
     * <p>Constructor for BaseQuery.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     */
    protected BaseQuery(SFunction<T, K> keyFunction) {
        super((Class<T>) LambdaUtils.extract(keyFunction).getInstantiatedClass());
        this.keyFunction = Objects.requireNonNull(keyFunction);
    }
}
