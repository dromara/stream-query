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
public class OneToMany<T, K extends Serializable & Comparable<K>, V> extends BaseQueryHelper<OneToMany<T, K, V>, T, K, V> {

    public OneToMany(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    public static <T, K extends Serializable & Comparable<K>> OneToMany<T, K, T> of(SFunction<T, K> keyFunction) {
        return new OneToMany<>(keyFunction);
    }

    public <R> OneToMany<T, K, R> value(SFunction<T, R> valueFunction) {
        attachDouble(valueFunction);
        return (OneToMany<T, K, R>) this;
    }

    public Map<K, List<V>> query() {
        return query(HashMap::new, Collective.toList());
    }

    public <A, R, M extends Map<K, R>> M query(IntFunction<M> mapFactory, Collector<? super V, A, R> downstream) {
        List<T> list = Database.list(wrapper);
        return Steam.of(list).parallel(isParallel).peek(peekConsumer).group(keyFunction, () -> mapFactory.apply(list.size()), Collective.mapping(valueOrIdentity(), downstream));
    }

}
