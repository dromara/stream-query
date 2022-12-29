package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.lambda.function.SerBiOp;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * 一对一
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/20
 */
@SuppressWarnings("unchecked")
public class OneToOne<T, K extends Serializable & Comparable<? super K>, V> extends BaseQueryHelper<OneToOne<T, K, V>, T, K, V> {

    /**
     * <p>of.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <T>         a T class
     * @param <K>         a K class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.OneToOne} object
     */
    public static <T, K extends Serializable & Comparable<? super K>> OneToOne<T, K, T> of(SFunction<T, K> keyFunction) {
        return new OneToOne<>(keyFunction);
    }

    /**
     * <p>Constructor for OneToOne.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     */
    protected OneToOne(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    /**
     * <p>value.</p>
     *
     * @param valueFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <R>           a R class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.OneToOne} object
     */
    public <R> OneToOne<T, K, R> value(SFunction<T, R> valueFunction) {
        attachDouble(valueFunction);
        return (OneToOne<T, K, R>) this;
    }

    /*public <U, A extends Serializable & Comparable<A>, R> OneToOne<T, K, R> and(OneToOne<U, A, R> engine) {

    }*/

    /**
     * <p>query.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<K, V> query() {
        return query(HashMap::new);
    }

    /**
     * <p>query.</p>
     *
     * @param mapFactory a {@link java.util.function.IntFunction} object
     * @param <R>        a R class
     * @return a R object
     */
    public <R extends Map<K, V>> R query(IntFunction<R> mapFactory) {
        List<T> list = Database.list(wrapper);
        return Steam.of(list)
                .parallel(isParallel)
                .peek(peekConsumer)
                .toMap(keyFunction, valueOrIdentity(), SerBiOp.justAfter(), () -> mapFactory.apply(list.size()));
    }

}
