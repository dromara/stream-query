package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 一对多
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt;
 * @since 2022/5/23 17:40
 */
public class OneToMany {

    private OneToMany() {
        /* Do not new me! */
    }

    // dataList key

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, List<$ENTITY>> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, false, peeks);
    }

    // wrapper dataList key

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, List<$ENTITY>> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, dataList, keyFunction, false, peeks);
    }

    // dataList key parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, List<$ENTITY>> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, isParallel, peeks);
    }

    // wrapper dataList key parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, List<$ENTITY>> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(dataList, keyFunction).map(queryOperator).map(wrapper -> SimpleQuery.group(wrapper, keyFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }

    // dataList key value

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, List<$VALUE>> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, false, peeks);
    }

    // wrapper dataList key value

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, List<$VALUE>> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, dataList, keyFunction, valueFunction, false, peeks);
    }

    // dataList key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, List<$VALUE>> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, isParallel, peeks);
    }

    // wrapper dataList key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, List<$VALUE>> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(dataList, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> SimpleQuery.group(wrapper, keyFunction, Collectors.mapping(valueFunction, Collectors.toList()), isParallel, peeks)).orElseGet(HashMap::new);
    }

    // dataList key collector

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, A, $ENTITY> Map<$KEY, $VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Collector<$ENTITY, A, $VALUE> downstream, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, downstream, false, peeks);
    }

    // wrapper dataList key collector

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, A, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Collector<$ENTITY, A, $VALUE> downstream, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, dataList, keyFunction, downstream, false, peeks);
    }

    // dataList key collector parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, A, $ENTITY> Map<$KEY, $VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Collector<$ENTITY, A, $VALUE> downstream, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, downstream, isParallel, peeks);
    }

    // wrapper dataList key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, A, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Collector<$ENTITY, A, $VALUE> downstream, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(dataList, keyFunction).map(queryOperator).map(wrapper -> SimpleQuery.group(wrapper, keyFunction, downstream, isParallel, peeks)).orElseGet(HashMap::new);
    }


    // data key

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, List<$ENTITY>> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, false, peeks);
    }

    // wrapper data key

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, List<$ENTITY>> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, data, keyFunction, false, peeks);
    }

    // data key parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, List<$ENTITY>> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, isParallel, peeks);
    }

    // wrapper data key parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, List<$ENTITY>> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(data, keyFunction).map(queryOperator).map(wrapper -> SimpleQuery.group(wrapper, keyFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }

    // data key value

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, List<$VALUE>> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, false, peeks);
    }

    // wrapper data key value

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, List<$VALUE>> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, data, keyFunction, valueFunction, false, peeks);
    }

    // data key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, List<$VALUE>> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, isParallel, peeks);
    }

    // wrapper data key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, List<$VALUE>> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(data, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> SimpleQuery.group(wrapper, keyFunction, Collectors.mapping(valueFunction, Collectors.toList()), isParallel, peeks)).orElseGet(HashMap::new);
    }

    // data key collector

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, A, $ENTITY> Map<$KEY, $VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, Collector<$ENTITY, A, $VALUE> downstream, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, downstream, false, peeks);
    }

    // wrapper data key collector

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, A, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, Collector<$ENTITY, A, $VALUE> downstream, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, data, keyFunction, downstream, false, peeks);
    }

    // data key collector parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, A, $ENTITY> Map<$KEY, $VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, Collector<$ENTITY, A, $VALUE> downstream, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, downstream, isParallel, peeks);
    }

    // wrapper data key collector parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, A, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, Collector<$ENTITY, A, $VALUE> downstream, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(data, keyFunction).map(queryOperator).map(wrapper -> SimpleQuery.group(wrapper, keyFunction, downstream, isParallel, peeks)).orElseGet(HashMap::new);
    }
}
