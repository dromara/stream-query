package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.optional.Sf;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * 单条
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/6/18 14:47
 */
@SuppressWarnings("unchecked")
public class One<$ENTITY, $KEY extends Serializable & Comparable<$KEY>, $VALUE> extends BaseQueryHelper<One<$ENTITY, $KEY, $ENTITY>, $ENTITY, $KEY, $VALUE> {

    private One(SFunction<$ENTITY, $KEY> keyFunction) {
        super(keyFunction);
    }

    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>, $VALUE>
    One<$ENTITY, $KEY, $VALUE> of(SFunction<$ENTITY, $KEY> keyFunction) {
        return new One<>(keyFunction);
    }

    public <$ACTUAL_VALUE> One<$ENTITY, $KEY, $ACTUAL_VALUE> value(SFunction<$ENTITY, $ACTUAL_VALUE> valueFunction) {
        if (Database.isNotActive(wrapper)) {
            return (One<$ENTITY, $KEY, $ACTUAL_VALUE>) this;
        }
        this.valueFunction = (SFunction<$ENTITY, $VALUE>) valueFunction;
        Database.select(wrapper, (w, col) -> w.select(col[1]), keyFunction, valueFunction);
        return (One<$ENTITY, $KEY, $ACTUAL_VALUE>) this;
    }

    public One<$ENTITY, $KEY, $VALUE> condition(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator) {
        this.wrapper = Sf.of(queryOperator.apply(wrapper)).orGet(() -> Database.notActive(wrapper));
        return this;
    }

    public $VALUE query() {
        if (Database.isNotActive(wrapper)) {
            return null;
        }
        if (Objects.isNull(valueFunction)) {
            return ($VALUE) Database.getOne(wrapper);
        }
        return Sf.of(Database.getOne(wrapper)).$let(valueFunction::apply).get();
    }


}
