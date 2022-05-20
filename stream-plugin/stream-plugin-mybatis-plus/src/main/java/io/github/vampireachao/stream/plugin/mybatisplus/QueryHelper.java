package io.github.vampireachao.stream.plugin.mybatisplus;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;

/**
 * 辅助类
 *
 * @author VampireAchao
 * @since 1.0-SNAPSHOT
 */
public class QueryHelper {

    private QueryHelper() {
        /* Do not new me! */
    }

    public static <T, E extends Serializable> Optional<LambdaQueryWrapper<T>> lambdaQuery(E data, SFunction<T, E> condition) {
        return Optional.ofNullable(data).map(value -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition))));
    }

    public static <T, E extends Serializable> Optional<LambdaQueryWrapper<T>> lambdaQuery(Collection<E> dataList, SFunction<T, E> condition) {
        return Optional.ofNullable(dataList).filter(values -> !values.isEmpty()).map(value -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition))));
    }
}
