package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.collector.Collective;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collector;


/**
 * 一对多
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/23 17:40
 */
@SuppressWarnings("unchecked")
public class OneToMany<T, K extends Serializable & Comparable<? super K>, V> extends BaseQueryHelper<OneToMany<T, K, V>, T, K, V> {

    /**
     * <p>Constructor for OneToMany.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     */
    public OneToMany(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    /**
     * <p>of.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <T>         a T class
     * @param <K>         a K class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.OneToMany} object
     */
    public static <T, K extends Serializable & Comparable<? super K>> OneToMany<T, K, T> of(SFunction<T, K> keyFunction) {
        return new OneToMany<>(keyFunction);
    }

    /**
     * <p>value.</p>
     *
     * @param valueFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <R>           a R class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.OneToMany} object
     */
    public <R> OneToMany<T, K, R> value(SFunction<T, R> valueFunction) {
        attachDouble(valueFunction);
        return (OneToMany<T, K, R>) this;
    }

    /**
     * <p>query.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<K, List<V>> query() {
        return query(HashMap::new, Collective.toList());
    }

    /**
     * <p>query.</p>
     *
     * @param mapFactory a {@link java.util.function.IntFunction} object
     * @param downstream a {@link java.util.stream.Collector} object
     * @param <A>        a A class
     * @param <R>        a R class
     * @param <M>        a M class
     * @return a M object
     */
    public <A, R, M extends Map<K, R>> M query(IntFunction<M> mapFactory, Collector<? super V, A, R> downstream) {
        List<T> list = Database.list(wrapper);
        return Steam.of(list).parallel(isParallel).peek(peekConsumer).group(keyFunction, () -> mapFactory.apply(list.size()), Collective.mapping(valueOrIdentity(), downstream));
    }

}
