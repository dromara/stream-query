package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.optional.Sf;

import java.io.Serializable;

/**
 * 单条
 *
 * @author VampireAchao Cizai_
 * @since 2022/6/18 14:47
 */
@SuppressWarnings("unchecked")
public class One<T, K extends Serializable & Comparable<? super K>, V> extends BaseQueryHelper<One<T, K, V>, T, K, V> {

    /**
     * <p>of.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <T>         a T class
     * @param <K>         a K class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.One} object
     */
    public static <T, K extends Serializable & Comparable<? super K>>
    One<T, K, T> of(SFunction<T, K> keyFunction) {
        return new One<>(keyFunction);
    }

    /**
     * <p>Constructor for One.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     */
    public One(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    /**
     * <p>value.</p>
     *
     * @param valueFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <R>           a R class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.One} object
     */
    public <R> One<T, K, R> value(SFunction<T, R> valueFunction) {
        attachSingle(valueFunction);
        return (One<T, K, R>) this;
    }

    /**
     * <p>query.</p>
     *
     * @return a V object
     */
    public V query() {
        return Sf.of(Database.getOne(wrapper)).mayAlso(peekConsumer).mayLet(valueOrIdentity()).get();
    }

    /**
     * <p>query.</p>
     *
     * @param throwEx a boolean
     * @return a V object
     */
    public V query(boolean throwEx) {
        return Sf.of(Database.getOne(wrapper, throwEx)).mayAlso(peekConsumer).mayLet(valueOrIdentity()).get();
    }


}
