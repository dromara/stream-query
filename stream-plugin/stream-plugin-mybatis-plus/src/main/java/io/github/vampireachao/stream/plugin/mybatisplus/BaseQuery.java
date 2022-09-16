package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author VampireAchao
 * @since 2022/9/16 16:30
 */
public abstract class BaseQuery<$ENTITY, $KEY extends Serializable & Comparable<$KEY>> {

    protected final SFunction<$ENTITY, $KEY> keyFunction;
    protected LambdaQueryWrapper<$ENTITY> wrapper;

    protected BaseQuery(SFunction<$ENTITY, $KEY> keyFunction) {
        this.keyFunction = Objects.requireNonNull(keyFunction);
    }


}
