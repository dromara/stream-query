package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.List;

/**
 * 多条
 *
 * @author VampireAchao ZVerify
 * @since 2022/6/18 21:21
 */
@SuppressWarnings("unchecked")
public class Many<T, K extends Serializable & Comparable<K>, V> extends BaseQueryHelper<Many<T, K, T>, Many<T, K, V>, T, K, V> {

    public Many(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    public static <T, K extends Serializable & Comparable<K>> Many<T, K, T> of(SFunction<T, K> keyFunction) {
        return new Many<>(keyFunction);
    }

    public <R> Many<T, K, R> value(SFunction<T, R> valueFunction) {
        attachSingle(valueFunction);
        return (Many<T, K, R>) this;
    }

    public <R> R query(SerFunc<Steam<V>, R> mapper) {
        return mapper.apply(Steam.of(Database.list(wrapper)).peek(peekConsumer).parallel(isParallel).nonNull().map(valueOrIdentity()));
    }

    public List<V> query() {
        return query(Steam::toList);
    }

}
