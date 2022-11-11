package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.optional.Sf;

import java.io.Serializable;

/**
 * 单条
 *
 * @author VampireAchao ZVerify
 * @since 2022/6/18 14:47
 */
@SuppressWarnings("unchecked")
public class One<T, K extends Serializable & Comparable<K>, V> extends BaseQueryHelper<One<T, K, V>, T, K, V> {

    public static <T, K extends Serializable & Comparable<K>>
    One<T, K, T> of(SFunction<T, K> keyFunction) {
        return new One<>(keyFunction);
    }

    public One(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    public <R> One<T, K, R> value(SFunction<T, R> valueFunction) {
        attachSingle(valueFunction);
        return (One<T, K, R>) this;
    }

    public V query() {
        return query(false);
    }

    public V query(boolean throwEx) {
        return Sf.of(Database.getOne(wrapper, throwEx)).mayAlso(peekConsumer).mayLet(valueOrIdentity()).get();
    }


}
