package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.vampireachao.stream.core.collector.Collectors;
import io.github.vampireachao.stream.core.lambda.function.SerBiCons;
import io.github.vampireachao.stream.core.stream.StreamHelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * 一对一
 *
 * @author VampireAchao
 * @since 2022/5/20
 */
public class OneToOne {

    private OneToOne() {
        /* Do not new me! */
    }


    // dataList key

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, false, peeks);
    }

    // wrapper dataList key

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, dataList, keyFunction, false, peeks);
    }

    // dataList key parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, isParallel, peeks);
    }

    // wrapper dataList key parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, dataList, keyFunction, i -> i, isParallel, peeks);
    }

    // dataList key value

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, false, peeks);
    }

    // wrapper dataList key value

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, dataList, keyFunction, valueFunction, false, peeks);
    }

    // dataList key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, isParallel, peeks);
    }

    // wrapper dataList key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return QueryHelper.lambdaQuery(dataList, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> StreamHelper.peekStream(SqlHelper.execute(SimpleQuery.getType(keyFunction), m -> m.selectList(wrapper)), isParallel, peeks).collect(Collectors.toMap(keyFunction, valueFunction))).orElseGet(HashMap::new);
    }

    // data key

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, false, peeks);
    }

    // wrapper data key

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, data, keyFunction, false, peeks);
    }

    // data key parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, isParallel, peeks);
    }

    // wrapper data key parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, data, keyFunction, i -> i, isParallel, peeks);
    }

    // data key value

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, false, peeks);
    }

    // wrapper data key value

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, data, keyFunction, valueFunction, false, peeks);
    }

    // data key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, isParallel, peeks);
    }

    // wrapper data key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return QueryHelper.lambdaQuery(data, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> StreamHelper.peekStream(SqlHelper.execute(SimpleQuery.getType(keyFunction), m -> m.selectList(wrapper)), isParallel, peeks).collect(Collectors.toMap(keyFunction, valueFunction))).orElseGet(HashMap::new);
    }


}
