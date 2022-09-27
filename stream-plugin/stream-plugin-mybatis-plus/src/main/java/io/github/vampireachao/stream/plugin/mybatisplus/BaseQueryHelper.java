package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import io.github.vampireachao.stream.core.optional.Sf;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * @author VampireAchao
 * @since 2022/9/16 16:30
 */
@SuppressWarnings("unchecked")
public abstract class BaseQueryHelper<
        $ENTITY_RESULT extends BaseQueryHelper<$ENTITY_RESULT, ?, $ENTITY, $KEY, $ENTITY>,
        $VALUE_RESULT extends BaseQueryHelper<$ENTITY_RESULT, $VALUE_RESULT, $ENTITY, $KEY, $VALUE>,
        $ENTITY,
        $KEY extends Serializable & Comparable<$KEY>,
        $VALUE
        > extends BaseQuery<$ENTITY, $KEY, $VALUE> {

    protected BaseQueryHelper(SFunction<$ENTITY, $KEY> keyFunction) {
        super(keyFunction);
    }

    protected $ENTITY_RESULT eq($KEY data) {
        LambdaQueryWrapper<$ENTITY> queryWrapper = Sf.of(wrapper).orGet(() -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(keyFunction))));
        if (Database.isNotActive(queryWrapper)) {
            return ($ENTITY_RESULT) this;
        }
        wrapper = Sf.of(data).$let(value -> queryWrapper.eq(keyFunction, value)).orGet(() -> Database.notActive(queryWrapper));
        return ($ENTITY_RESULT) this;
    }

    protected $ENTITY_RESULT in(Collection<$KEY> dataList) {
        LambdaQueryWrapper<$ENTITY> queryWrapper = Sf.of(wrapper).orGet(() -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(keyFunction))));
        if (Database.isNotActive(queryWrapper)) {
            return ($ENTITY_RESULT) this;
        }
        wrapper = Sf.$ofColl(dataList).$let(values -> queryWrapper.in(keyFunction, values)).orGet(() -> Database.notActive(queryWrapper));
        return ($ENTITY_RESULT) this;
    }

    protected $VALUE_RESULT condition(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator) {
        this.wrapper = Sf.of(queryOperator.apply(wrapper)).orGet(() -> Database.notActive(wrapper));
        return ($VALUE_RESULT) this;
    }


}
