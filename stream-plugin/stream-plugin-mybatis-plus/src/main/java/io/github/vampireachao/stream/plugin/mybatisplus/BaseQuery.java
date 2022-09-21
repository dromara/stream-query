package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import io.github.vampireachao.stream.core.optional.Sf;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * @author VampireAchao
 * @since 2022/9/16 16:30
 */
@SuppressWarnings("unchecked")
public abstract class BaseQuery<$ENGINE extends BaseQuery<$ENGINE, $ENTITY, $KEY, $ENTITY>, $ENTITY, $KEY extends Serializable & Comparable<$KEY>, $VALUE> {

    protected final SFunction<$ENTITY, $KEY> keyFunction;
    protected SFunction<$ENTITY, $VALUE> valueFunction;
    protected LambdaQueryWrapper<$ENTITY> wrapper;

    protected BaseQuery(SFunction<$ENTITY, $KEY> keyFunction) {
        this.keyFunction = Objects.requireNonNull(keyFunction);
    }

    protected $ENGINE eq($KEY data) {
        LambdaQueryWrapper<$ENTITY> queryWrapper = Sf.of(wrapper).orGet(() -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(keyFunction))));
        if (Database.isNotActive(queryWrapper)) {
            return ($ENGINE) this;
        }
        wrapper = Sf.of(data).$let(value -> queryWrapper.eq(keyFunction, value)).orGet(() -> Database.notActive(queryWrapper));
        return ($ENGINE) this;
    }

    protected $ENGINE in(Collection<$KEY> dataList) {
        LambdaQueryWrapper<$ENTITY> queryWrapper = Sf.of(wrapper).orGet(() -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(keyFunction))));
        if (Database.isNotActive(queryWrapper)) {
            return ($ENGINE) this;
        }
        wrapper = Sf.$ofColl(dataList).$let(values -> queryWrapper.in(keyFunction, values)).orGet(() -> Database.notActive(queryWrapper));
        return ($ENGINE) this;
    }


}
