package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import io.github.vampireachao.stream.core.optional.Opp;
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
public class One<$ENTITY, $KEY extends Serializable & Comparable<$KEY>, $VALUE> extends BaseQuery<$ENTITY, $KEY, $VALUE> {

    public One(SFunction<$ENTITY, $KEY> keyFunction) {
        super(keyFunction);
    }

    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>, $VALUE>
    One<$ENTITY, $KEY, $VALUE> of(SFunction<$ENTITY, $KEY> keyFunction) {
        return new One<>(keyFunction);
    }

    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> $ENTITY query($KEY data, SFunction<$ENTITY, $KEY> keyFunction) {
        return query(UnaryOperator.identity(), data, keyFunction);
    }

    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> $VALUE query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction);
    }

    @SuppressWarnings("unchecked")
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> $VALUE query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction) {
        return Database.lambdaQuery(data, keyFunction).map(queryOperator.compose(w -> Database.select(w, (wr, cols) -> wr.select(cols[1]), keyFunction, valueFunction))).map(Database::getOne).map(Opp.of(valueFunction).orElse(i -> ($VALUE) i)).orElse(null);
    }

    public One<$ENTITY, $KEY, $ENTITY> eq($KEY data) {
        LambdaQueryWrapper<$ENTITY> queryWrapper = Sf.of(wrapper).orGet(() -> Wrappers.lambdaQuery(ClassUtils.newInstance(SimpleQuery.getType(keyFunction))));
        if (Database.isNotActive(queryWrapper)) {
            return (One<$ENTITY, $KEY, $ENTITY>) this;
        }
        wrapper = Sf.of(data).$let(value -> queryWrapper.eq(keyFunction, value)).orGet(() -> Database.notActive(queryWrapper));
        return (One<$ENTITY, $KEY, $ENTITY>) this;
    }


    // data key

    public <$ACTUAL_VALUE> One<$ENTITY, $KEY, $ACTUAL_VALUE> value(SFunction<$ENTITY, $ACTUAL_VALUE> valueFunction) {
        if (Database.isNotActive(wrapper)) {
            return (One<$ENTITY, $KEY, $ACTUAL_VALUE>) this;
        }
        this.valueFunction = (SFunction<$ENTITY, $VALUE>) valueFunction;
        Database.select(wrapper, (w, col) -> w.select(col[1]), keyFunction, valueFunction);
        return (One<$ENTITY, $KEY, $ACTUAL_VALUE>) this;
    }

    // data key value

    public One<$ENTITY, $KEY, $VALUE> condition(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator) {
        this.wrapper = Sf.of(queryOperator.apply(wrapper)).orGet(() -> Database.notActive(wrapper));
        return this;
    }

    // wrapper data key

    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> $ENTITY query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction) {
        return query(queryOperator, data, keyFunction, null);
    }

    // wrapper data key value

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
