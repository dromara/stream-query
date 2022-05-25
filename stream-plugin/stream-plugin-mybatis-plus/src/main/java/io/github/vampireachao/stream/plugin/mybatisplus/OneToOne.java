package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.StreamSupport;

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
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, $ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, false, peeks);
    }

    // wrapper dataList key

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, dataList, keyFunction, false, peeks);
    }

    // dataList key parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, $ENTITY> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, isParallel, peeks);
    }

    // wrapper dataList key parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(dataList, keyFunction).map(queryOperator).map(wrapper -> SimpleQuery.keyMap(wrapper, keyFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }

    // dataList key value

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, false, peeks);
    }

    // wrapper dataList key value

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, dataList, keyFunction, valueFunction, false, peeks);
    }

    // dataList key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), dataList, keyFunction, valueFunction, isParallel, peeks);
    }

    // wrapper dataList key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, Collection<$KEY> dataList, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(dataList, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> SimpleQuery.map(wrapper, keyFunction, valueFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }

    // data key

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, $ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, false, peeks);
    }

    // wrapper data key

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, data, keyFunction, false, peeks);
    }

    // data key parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, $ENTITY> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, isParallel, peeks);
    }

    // wrapper data key parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $ENTITY> Map<$KEY, $ENTITY> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(data, keyFunction).map(queryOperator).map(wrapper -> SimpleQuery.keyMap(wrapper, keyFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }

    // data key value

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, $VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, false, peeks);
    }

    // wrapper data key value

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, Consumer<$ENTITY>... peeks) {
        return query(queryOperator, data, keyFunction, valueFunction, false, peeks);
    }

    // data key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, $VALUE> query($KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return query(UnaryOperator.identity(), data, keyFunction, valueFunction, isParallel, peeks);
    }

    // wrapper data key value parallel

    @SafeVarargs
    public static <$KEY extends Serializable, $VALUE, $ENTITY> Map<$KEY, $VALUE> query(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator, $KEY data, SFunction<$ENTITY, $KEY> keyFunction, SFunction<$ENTITY, $VALUE> valueFunction, boolean isParallel, Consumer<$ENTITY>... peeks) {
        return QueryHelper.lambdaQuery(data, keyFunction).map(queryOperator.compose(w -> QueryHelper.select(w, keyFunction, valueFunction))).map(wrapper -> SimpleQuery.map(wrapper, keyFunction, valueFunction, isParallel, peeks)).orElseGet(HashMap::new);
    }


    public static <$ENTITY, $ENTITY_KEY extends Serializable, $ENTITY_VALUE extends Serializable, $RESULT_VALUE> Map<$ENTITY_KEY, $RESULT_VALUE> query(Query<$ENTITY, $ENTITY_KEY, $ENTITY_VALUE, $RESULT_VALUE> query) {
        return QueryHelper.lambdaQuery(query.getDataList(), query.getData(), query.getKeyFunction()).map(query.getQueryOperator().compose(w -> QueryHelper.select(w, query.getKeyFunction(), query.getValueFunction())))
                .map(wrapper -> StreamSupport.stream(SimpleQuery.selectList(SimpleQuery.getType(query.getKeyFunction()), wrapper).spliterator(), query.getIsParallel()).<HashMap<$ENTITY_KEY, $RESULT_VALUE>>collect(HashMap::new, (m, v) -> m.put(query.getKeyFunction().apply(v),
                        Objects.nonNull(query.getValueFunction()) ? query.getValueFunction().andThen(query.getValueResultFunction()).apply(v) :
                                query.getEntityResultFunction().apply(v)), HashMap::putAll)).orElseGet(HashMap::new);
    }

    @SuppressWarnings("unchecked")
    public static class Query<$ENTITY, $ENTITY_KEY extends Serializable, $ENTITY_VALUE extends Serializable, $RESULT_VALUE> {
        private UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator;
        private $ENTITY_KEY data;
        private Collection<$ENTITY_KEY> dataList;
        private SFunction<$ENTITY, $ENTITY_KEY> keyFunction;
        private SFunction<$ENTITY, $ENTITY_VALUE> valueFunction;
        private Function<$ENTITY, $RESULT_VALUE> entityResultFunction;
        private Function<$ENTITY_VALUE, $RESULT_VALUE> valueResultFunction;
        private Boolean isParallel;
        private Consumer<$ENTITY> peek;

        public Query() {
            this.queryOperator = UnaryOperator.identity();
            this.entityResultFunction = i -> ($RESULT_VALUE) i;
            this.valueResultFunction = i -> ($RESULT_VALUE) i;
            this.isParallel = false;
            this.peek = entity -> {};
        }

        public static QueryBuilder builder() {
            return new QueryBuilder();
        }

        public UnaryOperator<LambdaQueryWrapper<$ENTITY>> getQueryOperator() {
            return queryOperator;
        }

        public void setQueryOperator(UnaryOperator<LambdaQueryWrapper<$ENTITY>> queryOperator) {
            this.queryOperator = queryOperator;
        }

        public $ENTITY_KEY getData() {
            return data;
        }

        public void setData($ENTITY_KEY data) {
            this.data = data;
        }

        public Collection<$ENTITY_KEY> getDataList() {
            return dataList;
        }

        public void setDataList(Collection<$ENTITY_KEY> dataList) {
            this.dataList = dataList;
        }

        public SFunction<$ENTITY, $ENTITY_KEY> getKeyFunction() {
            return keyFunction;
        }

        public void setKeyFunction(SFunction<$ENTITY, $ENTITY_KEY> keyFunction) {
            this.keyFunction = keyFunction;
        }

        public SFunction<$ENTITY, $ENTITY_VALUE> getValueFunction() {
            return valueFunction;
        }

        public void setValueFunction(SFunction<$ENTITY, $ENTITY_VALUE> valueFunction) {
            this.valueFunction = valueFunction;
        }

        public Function<$ENTITY, $RESULT_VALUE> getEntityResultFunction() {
            return entityResultFunction;
        }

        public void setEntityResultFunction(Function<$ENTITY, $RESULT_VALUE> entityResultFunction) {
            this.entityResultFunction = entityResultFunction;
        }

        public Function<$ENTITY_VALUE, $RESULT_VALUE> getValueResultFunction() {
            return valueResultFunction;
        }

        public void setValueResultFunction(Function<$ENTITY_VALUE, $RESULT_VALUE> valueResultFunction) {
            this.valueResultFunction = valueResultFunction;
        }

        public Consumer<$ENTITY> getPeek() {
            return peek;
        }

        public void setPeek(Consumer<$ENTITY> peek) {
            this.peek = peek;
        }

        public Boolean getIsParallel() {
            return isParallel;
        }

        public void setIsParallel(Boolean parallel) {
            isParallel = parallel;
        }

        public static class QueryBuilder {

            private Query<?, ?, ?, ?> query;

            public Query build() {
                return query;
            }

        }

    }


}
