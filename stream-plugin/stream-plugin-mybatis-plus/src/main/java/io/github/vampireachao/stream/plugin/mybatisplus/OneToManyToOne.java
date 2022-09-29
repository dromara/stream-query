package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.optional.Sf;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;


/**
 * 一对多对一
 *
 * @param <T> 主表类型(关联表)
 * @param <K> 主表查询key
 * @param <V> 主表查询value/附表查询key
 * @param <U> 附表类型(关联主数据)
 * @param <A> 附表value
 * @param <R> 分组类型，默认为主表查询key
 * @author VampireAchao ZVerify
 * @since 2022/5/24 14:15
 */
@SuppressWarnings("unchecked")
public class OneToManyToOne<T, K extends Serializable & Comparable<K>, V extends Serializable & Comparable<V>, U, A, R extends Serializable & Comparable<R>> {

    protected final SFunction<T, K> middleKey;
    protected SFunction<T, V> middleValue;
    protected SFunction<U, V> attachKey;
    protected SFunction<U, A> attachValue;
    protected SFunction<T, R> groupBy;

    protected LambdaQueryWrapper<T> middleWrapper;
    protected LambdaQueryWrapper<U> attachWrapper;

    protected boolean isParallel = false;
    protected SerCons<T> middlePeek = SerCons.nothing();
    protected SerCons<U> attachPeek = SerCons.nothing();

    public OneToManyToOne(SFunction<T, K> middleKey) {
        this.middleKey = middleKey;
    }

    public static <T, K extends Serializable & Comparable<K>, V extends Serializable & Comparable<V>, U>
    OneToManyToOne<T, K, V, U, U, K> of(SFunction<T, K> keyFunction) {
        OneToManyToOne<T, K, V, U, U, K> query = new OneToManyToOne<>(keyFunction);
        query.middleWrapper = Database.lambdaQuery(keyFunction);
        return query;
    }

    public OneToManyToOne<T, K, V, U, A, R> in(Collection<K> dataList) {
        middleWrapper = Sf.$ofColl(dataList).$let(values -> middleWrapper.in(middleKey, values)).orGet(() -> Database.notActive(middleWrapper));
        return this;
    }

    public OneToManyToOne<T, K, V, U, A, R> eq(K data) {
        middleWrapper = Sf.of(data).$let(value -> middleWrapper.eq(middleKey, value)).orGet(() -> Database.notActive(middleWrapper));
        return this;
    }

    public <VV extends Serializable & Comparable<VV>> OneToManyToOne<T, K, VV, U, T, K> value(SFunction<T, VV> middleValue) {
        this.middleValue = (SFunction<T, V>) middleValue;
        return (OneToManyToOne<T, K, VV, U, T, K>) this;
    }

    public <UU> OneToManyToOne<T, K, V, UU, UU, R> attachKey(SFunction<UU, V> attachKey) {
        this.attachKey = (SFunction<U, V>) attachKey;
        attachWrapper = (LambdaQueryWrapper<U>) Database.lambdaQuery(attachKey);
        return (OneToManyToOne<T, K, V, UU, UU, R>) this;
    }

    public <AA> OneToManyToOne<T, K, V, T, AA, K> attachValue(SFunction<U, AA> attachValue) {
        this.attachValue = (SFunction<U, A>) attachValue;
        return (OneToManyToOne<T, K, V, T, AA, K>) this;
    }

    public OneToManyToOne<T, K, V, U, A, R> condition(UnaryOperator<LambdaQueryWrapper<T>> queryOperator) {
        middleWrapper = Sf.of(queryOperator.apply(middleWrapper)).orGet(() -> Database.notActive(middleWrapper));
        return this;
    }

    public OneToManyToOne<T, K, V, U, A, R> attachCondition(UnaryOperator<LambdaQueryWrapper<U>> queryOperator) {
        attachWrapper = Sf.of(queryOperator.apply(attachWrapper)).orGet(() -> Database.notActive(attachWrapper));
        return this;
    }

    public OneToManyToOne<T, K, V, U, A, R> parallel(boolean isParallel) {
        this.isParallel = isParallel;
        return this;
    }

    public OneToManyToOne<T, K, V, U, A, R> parallel() {
        return parallel(true);
    }

    public OneToManyToOne<T, K, V, U, A, R> sequential() {
        return parallel(false);
    }

    public OneToManyToOne<T, K, V, U, A, R> peek(SerCons<T> middlePeek) {
        this.middlePeek = middlePeek;
        return this;
    }

    public OneToManyToOne<T, K, V, U, A, R> attachPeek(SerCons<U> attachPeek) {
        this.attachPeek = attachPeek;
        return this;
    }

    public <RR extends Serializable & Comparable<RR>> OneToManyToOne<T, K, V, U, A, RR> groupBy(SFunction<T, RR> groupBy) {
        this.groupBy = (SFunction<T, R>) groupBy;
        return (OneToManyToOne<T, K, V, U, A, RR>) this;
    }


    public Map<R, List<A>> query() {

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
