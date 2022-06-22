package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.github.vampireachao.stream.core.collector.Collectors;
import io.github.vampireachao.stream.core.optional.Opp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * 多条
 *
 * @author VampireAchao
 * @since 2022/6/18 21:21
 */
public class Many {

    private Many() {
        /* Do not new me! */
    }

    // data key

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable> List<$ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, peeks);
    }

    // data key value

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, peeks);
    }

    // data key parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable> List<$ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean parallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, null, parallel, peeks);
    }

    // data key value parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean parallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, parallel, peeks);
    }

    // wrapper data key

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable> List<$ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, data, keyFunction, null, false, peeks);
    }

    // wrapper data key value

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, data, keyFunction, valueFunction, false, peeks);
    }

    // wrapper data key parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable> List<$ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean parallel, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, data, keyFunction, null, parallel, peeks);
    }

    // wrapper data key value parallel

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean parallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(data, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> SimpleQuery.peekStream(SqlHelper.execute(SimpleQuery.getType(keyFunction), m -> m.selectList(wrapper)), parallel, peeks).map(Opp.ofNullable(valueFunction).orElse(i -> ($VALUE) i)).collect(Collectors.toList())).orElseGet(ArrayList::new);
    }

    // dataList key

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable> List<$ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, peeks);
    }

    // dataList key value

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, peeks);
    }

    // dataList key parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable> List<$ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean parallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, null, parallel, peeks);
    }

    // dataList key value parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean parallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, parallel, peeks);
    }

    // wrapper dataList key

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable> List<$ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, dataList, keyFunction, null, false, peeks);
    }

    // wrapper dataList key value

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, dataList, keyFunction, valueFunction, false, peeks);
    }

    // wrapper dataList key parallel

    @SafeVarargs
    public static <$ENTITY, $KEY extends Serializable> List<$ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean parallel, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, dataList, keyFunction, null, parallel, peeks);
    }

    // wrapper dataList key value parallel

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <$ENTITY, $KEY extends Serializable, $VALUE> List<$VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean parallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(dataList, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> SimpleQuery.peekStream(SqlHelper.execute(SimpleQuery.getType(keyFunction), m -> m.selectList(wrapper)), parallel, peeks).map(Opp.ofNullable(valueFunction).orElse(i -> ($VALUE) i)).collect(Collectors.toList())).orElseGet(ArrayList::new);
    }


}
