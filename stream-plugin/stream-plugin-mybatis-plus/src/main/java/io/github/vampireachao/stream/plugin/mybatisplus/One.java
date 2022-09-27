package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.optional.Sf;

import java.io.Serializable;
import java.util.Objects;

/**
 * 单条
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/6/18 14:47
 */
@SuppressWarnings("unchecked")
public class One<T, K extends Serializable & Comparable<K>, V> extends BaseQueryHelper<One<T, K, T>, One<T, K, V>, T, K, V> {

    public One(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>, $VALUE>
    One<$ENTITY, $KEY, $VALUE> of(SFunction<$ENTITY, $KEY> keyFunction) {
        return new One<>(keyFunction);
    }

    public <$ACTUAL_VALUE> One<T, K, $ACTUAL_VALUE> value(SFunction<T, $ACTUAL_VALUE> valueFunction) {
        if (Database.isNotActive(wrapper)) {
            return (One<T, K, $ACTUAL_VALUE>) this;
        }
        this.valueFunction = (SFunction<T, V>) valueFunction;
        Database.select(wrapper, (w, col) -> w.select(col[1]), keyFunction, valueFunction);
        return (One<T, K, $ACTUAL_VALUE>) this;
    }

    public V query() {
        if (Database.isNotActive(wrapper)) {
            return null;
        }
        if (Objects.isNull(valueFunction)) {
            return (V) Database.getOne(wrapper);
        }
        return Sf.of(Database.getOne(wrapper)).$let(valueFunction::apply).get();
    }


}
