package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.optional.Sf;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * 多条
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/6/18 21:21
 */
@SuppressWarnings("unchecked")
public class Many<$ENTITY, $KEY extends Serializable & Comparable<$KEY>, $VALUE> extends BaseQueryHelper<Many<$ENTITY, $KEY, $ENTITY>, $ENTITY, $KEY, $VALUE> {

    private Many(SFunction<$ENTITY, $KEY> keyFunction) {
        super(keyFunction);
    }

    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>, $VALUE>
    Many<$ENTITY, $KEY, $VALUE> of(SFunction<$ENTITY, $KEY> keyFunction) {
        return new Many<>(keyFunction);
    }

    public <$ACTUAL_VALUE> Many<$ENTITY, $KEY, $ACTUAL_VALUE> value(SFunction<$ENTITY, $ACTUAL_VALUE> valueFunction) {
        if (Database.isNotActive(wrapper)) {
            return (Many<$ENTITY, $KEY, $ACTUAL_VALUE>) this;
        }
        this.valueFunction = (SFunction<$ENTITY, $VALUE>) valueFunction;
        Database.select(wrapper, (w, col) -> w.select(col[1]), keyFunction, valueFunction);
        return (Many<$ENTITY, $KEY, $ACTUAL_VALUE>) this;
    }

    public Many<$ENTITY, $KEY, $VALUE> condition(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator) {
        this.wrapper = Sf.of(queryOperator.apply(wrapper)).orGet(() -> Database.notActive(wrapper));
        return this;
    }

    public List<$VALUE> query() {
        if (Database.isNotActive(wrapper)) {
            return new ArrayList<>();
        }
        if (Objects.isNull(valueFunction)) {
            return (List<$VALUE>) Database.list(wrapper);
        }
        return Steam.of(Database.list(wrapper)).parallel(isParallel).nonNull().map(valueFunction).toList();
    }

}
