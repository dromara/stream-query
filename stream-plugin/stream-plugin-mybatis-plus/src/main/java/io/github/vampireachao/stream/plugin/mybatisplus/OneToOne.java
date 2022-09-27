package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.lambda.function.SerBiOp;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 一对一
 *
 * @author VampireAchao ZVerify
 * @since 2022/5/20
 */
@SuppressWarnings("unchecked")
public class OneToOne<T, K extends Serializable & Comparable<K>, V> extends BaseQueryHelper<OneToOne<T, K, T>, OneToOne<T, K, V>, T, K, V> {

    protected OneToOne(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    public static <T, K extends Serializable & Comparable<K>, V> OneToOne<T, K, V> of(SFunction<T, K> keyFunction) {
        return new OneToOne<>(keyFunction);
    }

    public <R> OneToOne<T, K, R> value(SFunction<T, R> valueFunction) {
        attachDouble(valueFunction);
        return (OneToOne<T, K, R>) this;
    }

    public <R extends Map<K, V>> R query(Supplier<R> mapFactory) {
        return Steam.of(Database.list(wrapper)).parallel(isParallel).toMap(keyFunction, valueOrIdentity(), SerBiOp.justAfter(), mapFactory);
    }

    public Map<K, V> query() {
        return query(HashMap::new);
    }


}
