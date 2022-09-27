package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Objects;

/**
 * @author VampireAchao
 * @since 2022/9/26 17:50
 */
public abstract class BaseQuery<T, K, V> {

    protected final SFunction<T, K> keyFunction;
    protected boolean isParallel = false;
    protected LambdaQueryWrapper<T> wrapper;
    protected SFunction<T, V> valueFunction;

    protected BaseQuery(SFunction<T, K> keyFunction) {
        this.keyFunction = Objects.requireNonNull(keyFunction);
    }
}
