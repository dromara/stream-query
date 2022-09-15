package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.vampireachao.stream.core.optional.Opp;

import java.io.Serializable;
import java.util.function.UnaryOperator;

/**
 * 单条
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/6/18 14:47
 */
public class One {

    private One() {
        /* Do not new me! */
    }

    // data key

    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> $ENTITY query($KEY data, SFunction<$ENTITY, $KEY> keyFunction) {
        return query(UnaryOperator.identity(), data, keyFunction);
    }

    // data key value

    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> $VALUE query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction);
    }

    // wrapper data key

    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> $ENTITY query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction) {
        return query(queryOperator, data, keyFunction, null);
    }

    // wrapper data key value

    @SuppressWarnings("unchecked")
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> $VALUE query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction) {
        return Database.lambdaQuery(data, keyFunction).map(queryOperator.compose(w -> Database.select(w, (wr, cols) -> wr.select(cols[1]), keyFunction, valueFunction))).map(wrapper -> SqlHelper.execute(SimpleQuery.getType(keyFunction), m -> m.selectOne(wrapper))).map(Opp.of(valueFunction).orElse(i -> ($VALUE) i)).orElse(null);
    }


}
