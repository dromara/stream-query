package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Objects;

/**
 * @author VampireAchao
 * @since 2022/9/26 17:50
 */
public abstract class BaseQuery<$ENTITY, $KEY, $VALUE> {

    protected LambdaQueryWrapper<$ENTITY> wrapper;
    protected boolean isParallel = false;
    protected final SFunction<$ENTITY, $KEY> keyFunction;
    protected SFunction<$ENTITY, $VALUE> valueFunction;

    protected BaseQuery(SFunction<$ENTITY, $KEY> keyFunction) {
        this.keyFunction = Objects.requireNonNull(keyFunction);
    }
}
