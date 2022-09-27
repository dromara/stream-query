package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static <T, K extends Serializable & Comparable<K>, V> Many<T, K, V> of(SFunction<T, K> keyFunction) {
        return new Many<>(keyFunction);
    }

    public <R> Many<T, K, R> value(SFunction<T, R> valueFunction) {
        if (Database.isNotActive(wrapper)) {
            return (Many<T, K, R>) this;
        }
        this.valueFunction = (SFunction<T, V>) valueFunction;
        Database.select(wrapper, (w, col) -> w.select(col[1]), keyFunction, valueFunction);
        return (Many<T, K, R>) this;
    }

    public List<V> query() {
        if (Database.isNotActive(wrapper)) {
            return new ArrayList<>();
        }
        if (Objects.isNull(valueFunction)) {
            return (List<V>) Database.list(wrapper);
        }
        return Steam.of(Database.list(wrapper)).parallel(isParallel).nonNull().map(valueFunction).toList();
    }

}
