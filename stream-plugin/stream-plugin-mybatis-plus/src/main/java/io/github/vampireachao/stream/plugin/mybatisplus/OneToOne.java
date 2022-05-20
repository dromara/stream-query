package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;

/**
 * MapQuery
 *
 * @author VampireAchao
 * @since 2022/5/20
 */
public class OneToOne {

    private OneToOne() {
        /* Do not new me! */
    }

    public static <K extends Serializable, A, V, T> Map<K, V> queryField(UnaryOperator<LambdaQueryWrapper<T>> queryOperator, Collection<K> dataList, SFunction<T, K> keyFunction, Collector<T, A, V> valueFunction, Consumer<T> peek) {
        return null;
    }
}
