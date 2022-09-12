package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.vampireachao.stream.core.lambda.function.SerBiCons;
import io.github.vampireachao.stream.core.lambda.function.SerUnOp;
import io.github.vampireachao.stream.core.optional.Op;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 多条
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/6/18 21:21
 */
public class Many {

    private Many() {
        /* Do not new me! */
    }

    // data key

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>> List<$ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(SerUnOp.identity(), data, keyFunction, peeks);
    }

    // data key value

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(SerUnOp.identity(), data, keyFunction, valueFunction, peeks);
    }

    // data key parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>> List<$ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean parallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(SerUnOp.identity(), data, keyFunction, null, parallel, peeks);
    }

    // data key value parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean parallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(SerUnOp.identity(), data, keyFunction, valueFunction, parallel, peeks);
    }

    // wrapper data key

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>> List<$ENTITY> query(SerUnOp<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, data, keyFunction, null, false, peeks);
    }

    // wrapper data key value

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(SerUnOp<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, data, keyFunction, valueFunction, false, peeks);
    }

    // wrapper data key parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>> List<$ENTITY> query(SerUnOp<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean parallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, data, keyFunction, null, parallel, peeks);
    }

    // wrapper data key value parallel

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(SerUnOp<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean parallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return Database.lambdaQuery(data, keyFunction).map(queryOperator.compose(w -> Database.select(w, (wr, cols) -> wr.select(cols[1]), keyFunction, valueFunction))).map(wrapper -> Steam.of(SqlHelper.execute(SimpleQuery.getType(keyFunction), m -> m.selectList(wrapper)), parallel).nonNull().peekIdx(SerBiCons.multi(peeks)).map(Op.of(valueFunction).orElse(i -> ($VALUE) i)).toList()).orElseGet(ArrayList::new);
    }

    // dataList key

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>> List<$ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(SerUnOp.identity(), dataList, keyFunction, peeks);
    }

    // dataList key value

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(SerUnOp.identity(), dataList, keyFunction, valueFunction, peeks);
    }

    // dataList key parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>> List<$ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean parallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(SerUnOp.identity(), dataList, keyFunction, null, parallel, peeks);
    }

    // dataList key value parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean parallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(SerUnOp.identity(), dataList, keyFunction, valueFunction, parallel, peeks);
    }

    // wrapper dataList key

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>> List<$ENTITY> query(SerUnOp<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, dataList, keyFunction, null, false, peeks);
    }

    // wrapper dataList key value

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(SerUnOp<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, dataList, keyFunction, valueFunction, false, peeks);
    }

    // wrapper dataList key parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable & Comparable<$KEY>> List<$ENTITY> query(SerUnOp<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean parallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, dataList, keyFunction, null, parallel, peeks);
    }

    // wrapper dataList key value parallel

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(SerUnOp<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean parallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return Database.lambdaQuery(dataList, keyFunction).map(queryOperator.compose(w -> Database.select(w, (wr, cols) -> wr.select(cols[1]), keyFunction, valueFunction))).map(wrapper -> Steam.of(SqlHelper.execute(SimpleQuery.getType(keyFunction), m -> m.selectList(wrapper)), parallel).nonNull().peekIdx(SerBiCons.multi(peeks)).map(Op.of(valueFunction).orElse(i -> ($VALUE) i)).toList()).orElseGet(ArrayList::new);
    }


}
