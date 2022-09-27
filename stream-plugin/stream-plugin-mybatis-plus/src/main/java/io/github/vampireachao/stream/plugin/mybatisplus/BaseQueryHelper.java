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
 * 公共方法抽象
 *
 * @param <TR> 原返回值
 * @param <VR> value改变后的返回值
 * @param <T>  实体类型
 * @param <K>  key值类型
 * @param <V>  value或后续操作返回值类型
 * @author VampireAchao
 * @since 2022/9/16 16:30
 */
@SuppressWarnings("unchecked")
public abstract class BaseQueryHelper<
        TR extends BaseQueryHelper<TR, ?, T, K, T>,
        VR extends BaseQueryHelper<TR, VR, T, K, V>,
        T,
        K extends Serializable & Comparable<K>,
        V
        > extends BaseQuery<T, K, V> {

    protected BaseQueryHelper(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    protected TR eq(K data) {
        LambdaQueryWrapper<T> queryWrapper = Sf.of(wrapper).orGet(() -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(keyFunction))));
        if (Database.isNotActive(queryWrapper)) {
            return (TR) this;
        }
        wrapper = Sf.of(data).$let(value -> queryWrapper.eq(keyFunction, value)).orGet(() -> Database.notActive(queryWrapper));
        return (TR) this;
    }

    protected TR in(Collection<K> dataList) {
        LambdaQueryWrapper<T> queryWrapper = Sf.of(wrapper).orGet(() -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(keyFunction))));
        if (Database.isNotActive(queryWrapper)) {
            return (TR) this;
        }
        wrapper = Sf.$ofColl(dataList).$let(values -> queryWrapper.in(keyFunction, values)).orGet(() -> Database.notActive(queryWrapper));
        return (TR) this;
    }

    protected VR condition(UnaryOperator<LambdaQueryWrapper<T>> queryOperator) {
        this.wrapper = Sf.of(queryOperator.apply(wrapper)).orGet(() -> Database.notActive(wrapper));
        return (VR) this;
    }


}
