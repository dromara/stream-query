package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.collector.Collective;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collector;

/**
 * 一对多对一
 *
 * @author VampireAchao ZVerify
 * @since 2022/5/24 14:15
 */
@SuppressWarnings("unchecked")
public class OneToManyToOne<T, TK extends Serializable & Comparable<TK>, TV, A, AK extends Comparable<AK>, AV> extends BaseQueryHelper<OneToManyToOne<T, TK, T, A, ?, AV>, OneToManyToOne<T, TK, TV, A, AK, AV>, T, TK, TV> {

    protected SFunction<A, AK> attachKey;
    protected SFunction<A, AV> attachValue;
    protected SerFunc<T, TK> groupBy;
    protected Consumer<A> attachPeek;

    public OneToManyToOne(SFunction<T, TK> keyFunction) {
        super(keyFunction);
    }

    public static <T, TK extends Serializable & Comparable<TK>>
    OneToManyToOne<T, TK, ?, ?, ?, ?> of(SFunction<T, TK> keyFunction) {
        return new OneToManyToOne<>(keyFunction);
    }

    public <RS extends Serializable & Comparable<RS>> OneToManyToOne<T, TK, RS, A, RS, AV> value(SFunction<T, RS> valueFunction) {
        attachDouble(valueFunction);
        return (OneToManyToOne<T, TK, RS, A, RS, AV>) this;
    }

    public <U> OneToManyToOne<T, TK, TV, U, AK, AV> attachKey(SFunction<U, TV> attachKey) {
        this.attachKey = (SFunction<A, AK>) attachKey;
        this.groupBy = (SerFunc<T, TK>) attachKey;
        return (OneToManyToOne<T, TK, TV, U, AK, AV>) this;
    }

    public <R> OneToManyToOne<T, TK, TV, A, AK, R> attachValue(SFunction<A, R> attachValue) {
        this.attachValue = (SFunction<A, AV>) attachValue;
        return (OneToManyToOne<T, TK, TV, A, AK, R>) this;
    }

    public OneToManyToOne<T, TK, TV, A, AK, AV> attachPeek(Consumer<A> attachPeek) {
        this.attachPeek = attachPeek;
        return this;
    }

    public <D extends Serializable & Comparable<D>> OneToManyToOne<T, D, TV, A, AK, AV> groupBy(SerFunc<T, D> groupBy) {
        this.groupBy = (SerFunc<T, TK>) groupBy;
        return (OneToManyToOne<T, D, TV, A, AK, AV>) this;
    }

    public <R> Map<TK, R> query(Collector<? super AV, ?, R> downstream) {
        /*Map<TK, List<TV>> tkTvsMap = Steam.of(Database.list(wrapper)).group(groupBy, Collective.mapping(valueFunction, Collective.toList()));
        OneToOne.of(attachKey)
        tkTvsMap.in(Steam.of(mkVsMap.values()).flat(Function.identity()).toList());
        return Steam.of(list).parallel(isParallel).peek(peekConsumer).group(keyFunction, () -> mapFactory.apply(list.size()), Collective.mapping(valueOrIdentity(), downstream));*/
        return null;
    }

    public Map<TK, List<AV>> query() {
        return query(Collective.toList());
    }


    /*private OneToManyToOne() {
     *//* Do not new me! *//*
    }

    // mainDataList middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // mainDataList middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // mainDataList middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        Set<$RELATION_FIELD> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Opp.of(OneToMany.query(middleQueryOperator, mainDataList, middleKey, middleValue, isParallel, middleConsumer, (middle, index) -> Opp.of(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Opp.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, isParallel, attachConsumer)).filter(keyAttachMap -> !keyAttachMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        Set<$RELATION_FIELD> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Opp.of(OneToMany.query(middleQueryOperator, mainDataList, middleKey, middleValue, isParallel, middleConsumer, (middle, index) -> Opp.of(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Opp.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, attachValue, isParallel, attachConsumer)).filter(attachKeyValueMap -> !attachKeyValueMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }

    // mainData middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // mainData middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // mainData middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // mainData middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        Set<$RELATION_FIELD> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Opp.of(OneToMany.query(middleQueryOperator, mainData, middleKey, middleValue, isParallel, middleConsumer, (middle, index) -> Opp.of(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Opp.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, isParallel, attachConsumer)).filter(keyAttachMap -> !keyAttachMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $RELATION_FIELD> middleValue, SFunction<$ATTACH, $RELATION_FIELD> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        Set<$RELATION_FIELD> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Opp.of(OneToMany.query(middleQueryOperator, mainData, middleKey, middleValue, isParallel, middleConsumer, (middle, index) -> Opp.of(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Opp.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, attachValue, isParallel, attachConsumer)).filter(attachKeyValueMap -> !attachKeyValueMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }


    private static <$ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $RELATION_FIELD extends Serializable & Comparable<$RELATION_FIELD>> Function<Map<$RELATION_FIELD, $ATTACH>, Map<$MIDDLE_KEY, List<$ATTACH>>> convertMiddleToAttachFunction(boolean isParallel, Map<$MIDDLE_KEY, List<$RELATION_FIELD>> middleKeyValuesMap) {
        return keyAttachMap -> StreamSupport.stream(middleKeyValuesMap.entrySet().spliterator(), isParallel).collect(Collective.toMap(Map.Entry::getKey, middleKeyValuesEntry -> Opp.of(middleKeyValuesEntry.getValue()).filter(middleValues -> !middleValues.isEmpty()).map(middleValues -> StreamSupport.stream(middleValues.spliterator(), isParallel).map(keyAttachMap::get).filter(Objects::nonNull).collect(Collective.toList())).orElseGet(ArrayList::new)));
    }*/
}
