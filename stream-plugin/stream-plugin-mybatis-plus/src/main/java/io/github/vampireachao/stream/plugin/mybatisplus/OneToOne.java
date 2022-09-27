package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.vampireachao.stream.core.lambda.function.SerBiCons;
import io.github.vampireachao.stream.core.lambda.function.SerBiOp;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

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

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, false, peeks);
    }

    public Map<K, V> query() {
        return query(HashMap::new);
    }


    // dataList key

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, dataList, keyFunction, false, peeks);
    }

    // wrapper dataList key

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, isParallel, peeks);
    }

    // dataList key parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, dataList, keyFunction, i -> i, isParallel, peeks);
    }

    // wrapper dataList key parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, false, peeks);
    }

    // dataList key value

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, dataList, keyFunction, valueFunction, false, peeks);
    }

    // wrapper dataList key value

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, isParallel, peeks);
    }

    // dataList key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return Database.lambdaQuery(dataList, keyFunction).map(queryOperator.compose(w -> Database.select(w, keyFunction, valueFunction))).map(wrapper -> Steam.of(SqlHelper.execute(SimpleQuery.getType(keyFunction), m -> m.selectList(wrapper)), isParallel).nonNull().peekIdx(SerBiCons.multi(peeks)).toMap(keyFunction, valueFunction)).orElseGet(HashMap::new);
    }

    // wrapper dataList key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, false, peeks);
    }

    // data key

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, data, keyFunction, false, peeks);
    }

    // wrapper data key

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, isParallel, peeks);
    }

    // data key parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, data, keyFunction, i -> i, isParallel, peeks);
    }

    // wrapper data key parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, false, peeks);
    }

    // data key value

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(queryOperator, data, keyFunction, valueFunction, false, peeks);
    }

    // wrapper data key value

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, isParallel, peeks);
    }

    // data key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable & Comparable<$KEY>, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, SerBiCons<$ENTITY, Integer>... peeks) {
        return Database.lambdaQuery(data, keyFunction).map(queryOperator.compose(w -> Database.select(w, keyFunction, valueFunction))).map(wrapper -> Steam.of(SqlHelper.execute(SimpleQuery.getType(keyFunction), m -> m.selectList(wrapper)), isParallel).nonNull().peekIdx(SerBiCons.multi(peeks)).toMap(keyFunction, valueFunction)).orElseGet(HashMap::new);
    }

    // wrapper data key value parallel

    public <R extends Map<K, V>> R query(IntFunction<R> mapFactory) {
        List<T> list = Database.list(wrapper);
        return Steam.of(list).parallel(isParallel).toMap(keyFunction, valueOrIdentity(), SerBiOp.justAfter(), () -> mapFactory.apply(list.size()));
    }

}
