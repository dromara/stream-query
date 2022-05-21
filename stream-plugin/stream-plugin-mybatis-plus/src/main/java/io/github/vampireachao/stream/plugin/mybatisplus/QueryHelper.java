package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

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
        return Optional.ofNullable(data).map(value -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition))).eq(condition, value));
    }

    public static <T, E extends Serializable> Optional<LambdaQueryWrapper<T>> lambdaQuery(Collection<E> dataList, SFunction<T, E> condition) {
        return Optional.ofNullable(dataList).filter(values -> !values.isEmpty()).map(value -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(condition))).in(condition, value));
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> LambdaQueryWrapper<T> select(LambdaQueryWrapper<T> wrapper, SFunction<T, ?>... columns) {
        return wrapper.select(Stream.of(columns).filter(func -> PropertyNamer.isGetter(LambdaUtils.extract(func).getImplMethodName())).toArray(SFunction[]::new));
    }
}
