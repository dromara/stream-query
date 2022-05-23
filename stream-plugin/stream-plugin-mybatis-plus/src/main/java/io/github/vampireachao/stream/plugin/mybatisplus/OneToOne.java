package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * 一对一
 *
 * @author VampireAchao
 * @since 2022/5/20
 */
public class OneToOne {

    private OneToOne() {
        /* Do not new me! */
    }


    // dataList key

    @SafeVarargs
    public static <K extends Serializable, T> Map<K, T> query(Collection<K> dataList, SFunction<T, K> keyFunction, Consumer<T>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, false, peeks);
    }

    // wrapper dataList key

    @SafeVarargs
    public static <K extends Serializable, T> Map<K, T> query(UnaryOperator<LambdaQueryWrapper<T>> queryOperator, Collection<K> dataList, SFunction<T, K> keyFunction, Consumer<T>... peeks) {
        return query(queryOperator, dataList, keyFunction, false, peeks);
    }

    // dataList key parallel

    @SafeVarargs
    public static <K extends Serializable, T> Map<K, T> query(Collection<K> dataList, SFunction<T, K> keyFunction, boolean isParallel, Consumer<T>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, isParallel, peeks);
    }

    // wrapper dataList key parallel

    @SafeVarargs
    public static <K extends Serializable, T> Map<K, T> query(UnaryOperator<LambdaQueryWrapper<T>> queryOperator, Collection<K> dataList, SFunction<T, K> keyFunction, boolean isParallel, Consumer<T>... peeks) {
        return QueryHelper.lambdaQuery(dataList, keyFunction).map(queryOperator).map(wrapper -> SimpleQuery.keyMap(wrapper, keyFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }

    // dataList key value

    @SafeVarargs
    public static <K extends Serializable, V, T> Map<K, V> query(Collection<K> dataList, SFunction<T, K> keyFunction, SFunction<T, V> valueFunction, Consumer<T>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, false, peeks);
    }

    // wrapper dataList key value

    @SafeVarargs
    public static <K extends Serializable, V, T> Map<K, V> query(UnaryOperator<LambdaQueryWrapper<T>> queryOperator, Collection<K> dataList, SFunction<T, K> keyFunction, SFunction<T, V> valueFunction, Consumer<T>... peeks) {
        return query(queryOperator, dataList, keyFunction, valueFunction, false, peeks);
    }

    // dataList key value parallel

    @SafeVarargs
    public static <K extends Serializable, V, T> Map<K, V> query(Collection<K> dataList, SFunction<T, K> keyFunction, SFunction<T, V> valueFunction, boolean isParallel, Consumer<T>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, isParallel, peeks);
    }

    // wrapper dataList key value parallel

    @SafeVarargs
    public static <K extends Serializable, V, T> Map<K, V> query(UnaryOperator<LambdaQueryWrapper<T>> queryOperator, Collection<K> dataList, SFunction<T, K> keyFunction, SFunction<T, V> valueFunction, boolean isParallel, Consumer<T>... peeks) {
        return QueryHelper.lambdaQuery(dataList, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> SimpleQuery.map(wrapper, keyFunction, valueFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }

    // data key

    @SafeVarargs
    public static <K extends Serializable, T> Map<K, T> query(K data, SFunction<T, K> keyFunction, Consumer<T>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, false, peeks);
    }

    // wrapper data key

    @SafeVarargs
    public static <K extends Serializable, T> Map<K, T> query(UnaryOperator<LambdaQueryWrapper<T>> queryOperator, K data, SFunction<T, K> keyFunction, Consumer<T>... peeks) {
        return query(queryOperator, data, keyFunction, false, peeks);
    }

    // data key parallel

    @SafeVarargs
    public static <K extends Serializable, T> Map<K, T> query(K data, SFunction<T, K> keyFunction, boolean isParallel, Consumer<T>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, isParallel, peeks);
    }

    // wrapper data key parallel

    @SafeVarargs
    public static <K extends Serializable, T> Map<K, T> query(UnaryOperator<LambdaQueryWrapper<T>> queryOperator, K data, SFunction<T, K> keyFunction, boolean isParallel, Consumer<T>... peeks) {
        return QueryHelper.lambdaQuery(data, keyFunction).map(queryOperator).map(wrapper -> SimpleQuery.keyMap(wrapper, keyFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }

    // data key value

    @SafeVarargs
    public static <K extends Serializable, V, T> Map<K, V> query(K data, SFunction<T, K> keyFunction, SFunction<T, V> valueFunction, Consumer<T>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, false, peeks);
    }

    // wrapper data key value

    @SafeVarargs
    public static <K extends Serializable, V, T> Map<K, V> query(UnaryOperator<LambdaQueryWrapper<T>> queryOperator, K data, SFunction<T, K> keyFunction, SFunction<T, V> valueFunction, Consumer<T>... peeks) {
        return query(queryOperator, data, keyFunction, valueFunction, false, peeks);
    }

    // data key value parallel

    @SafeVarargs
    public static <K extends Serializable, V, T> Map<K, V> query(K data, SFunction<T, K> keyFunction, SFunction<T, V> valueFunction, boolean isParallel, Consumer<T>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, isParallel, peeks);
    }

    // wrapper data key value parallel

    @SafeVarargs
    public static <K extends Serializable, V, T> Map<K, V> query(UnaryOperator<LambdaQueryWrapper<T>> queryOperator, K data, SFunction<T, K> keyFunction, SFunction<T, V> valueFunction, boolean isParallel, Consumer<T>... peeks) {
        return QueryHelper.lambdaQuery(data, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> SimpleQuery.map(wrapper, keyFunction, valueFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }


}
