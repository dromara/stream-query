package org.dromara.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.dromara.stream.core.lambda.function.SerFunc;
import org.dromara.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.List;

/**
 * 多条
 *
 * @author VampireAchao Cizai_
 * @since 2022/6/18 21:21
 */
@SuppressWarnings("unchecked")
public class Many<T, K extends Serializable & Comparable<? super K>, V> extends BaseQueryHelper<Many<T, K, V>, T, K, V> {

    /**
     * <p>Constructor for Many.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     */
    public Many(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    /**
     * <p>of.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <T>         a T class
     * @param <K>         a K class
     * @return a {@link Many} object
     */
    public static <T, K extends Serializable & Comparable<? super K>> Many<T, K, T> of(SFunction<T, K> keyFunction) {
        return new Many<>(keyFunction);
    }

    /**
     * <p>value.</p>
     *
     * @param valueFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <R>           a R class
     * @return a {@link Many} object
     */
    public <R> Many<T, K, R> value(SFunction<T, R> valueFunction) {
        attachSingle(valueFunction);
        return (Many<T, K, R>) this;
    }

    /**
     * <p>query.</p>
     *
     * @param mapper a {@link SerFunc} object
     * @param <R>    a R class
     * @return a R object
     */
    public <R> R query(SerFunc<Steam<V>, R> mapper) {
        return mapper.apply(Steam.of(Database.list(wrapper)).peek(peekConsumer).parallel(isParallel).nonNull().map(valueOrIdentity()));
    }

    /**
     * <p>query.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<V> query() {
        return query(Steam::toList);
    }

}
