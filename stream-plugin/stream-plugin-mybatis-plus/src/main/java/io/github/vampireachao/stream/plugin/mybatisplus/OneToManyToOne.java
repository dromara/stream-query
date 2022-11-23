package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.collection.Maps;
import io.github.vampireachao.stream.core.collector.Collective;
import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerUnOp;
import io.github.vampireachao.stream.core.optional.Sf;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;


/**
 * 一对多对一
 *
 * @param <T> 主表类型(关联表)
 * @param <K> 主表查询key
 * @param <V> 主表查询value/附表查询key
 * @param <U> 附表类型(关联主数据)
 * @param <A> 附表value
 * @author VampireAchao Cizai_
 * @since 2022/5/24 14:15
 */
@SuppressWarnings("unchecked")
public class OneToManyToOne<T, K extends Serializable & Comparable<K>, V extends Serializable & Comparable<V>, U, A> {

    private final SFunction<T, K> middleKey;
    private SFunction<T, V> middleValue;
    private SFunction<U, V> attachKey;
    private SFunction<U, A> attachValue;
    private LambdaQueryWrapper<T> middleWrapper;
    private UnaryOperator<LambdaQueryWrapper<U>> attachQueryOperator = SerUnOp.identity();

    private boolean isParallel = false;
    private SerCons<T> middlePeek = SerCons.nothing();
    private SerCons<U> attachPeek = SerCons.nothing();

    public OneToManyToOne(SFunction<T, K> middleKey) {
        this.middleKey = middleKey;
        this.middleWrapper = Database.lambdaQuery(middleKey);
    }

    public static <T, K extends Serializable & Comparable<K>, V extends Serializable & Comparable<V>, U>
    OneToManyToOne<T, K, V, U, T> of(SFunction<T, K> keyFunction) {
        return new OneToManyToOne<>(keyFunction);
    }

    public OneToManyToOne<T, K, V, U, A> in(Collection<K> dataList) {
        middleWrapper = Sf.mayColl(dataList).mayLet(HashSet::new).mayLet(values -> middleWrapper.in(middleKey, values)).orGet(() -> Database.notActive(middleWrapper));
        return this;
    }

    public OneToManyToOne<T, K, V, U, A> eq(K data) {
        middleWrapper = Sf.of(data).mayLet(value -> middleWrapper.eq(middleKey, value)).orGet(() -> Database.notActive(middleWrapper));
        return this;
    }

    public <VV extends Serializable & Comparable<VV>> OneToManyToOne<T, K, VV, U, VV> value(SFunction<T, VV> middleValue) {
        this.middleValue = (SFunction<T, V>) middleValue;
        if (Objects.nonNull(middleWrapper)) {
            Database.select(middleWrapper, middleKey, middleValue);
        }
        return (OneToManyToOne<T, K, VV, U, VV>) this;
    }

    public <UU> OneToManyToOne<T, K, V, UU, UU> attachKey(SFunction<UU, V> attachKey) {
        this.attachKey = (SFunction<U, V>) attachKey;
        return (OneToManyToOne<T, K, V, UU, UU>) this;
    }

    public <AA> OneToManyToOne<T, K, V, T, AA> attachValue(SFunction<U, AA> attachValue) {
        this.attachValue = (SFunction<U, A>) attachValue;
        return (OneToManyToOne<T, K, V, T, AA>) this;
    }

    public OneToManyToOne<T, K, V, U, A> condition(UnaryOperator<LambdaQueryWrapper<T>> queryOperator) {
        middleWrapper = Sf.of(queryOperator.apply(middleWrapper)).orGet(() -> Database.notActive(middleWrapper));
        return this;
    }

    public OneToManyToOne<T, K, V, U, A> attachCondition(UnaryOperator<LambdaQueryWrapper<U>> attachQueryOperator) {
        this.attachQueryOperator = attachQueryOperator;
        return this;
    }

    public OneToManyToOne<T, K, V, U, A> parallel(boolean isParallel) {
        this.isParallel = isParallel;
        return this;
    }

    public OneToManyToOne<T, K, V, U, A> parallel() {
        return parallel(true);
    }

    public OneToManyToOne<T, K, V, U, A> sequential() {
        return parallel(false);
    }

    public OneToManyToOne<T, K, V, U, A> peek(SerCons<T> middlePeek) {
        this.middlePeek = this.middlePeek.andThen(middlePeek);
        return this;
    }

    public OneToManyToOne<T, K, V, U, A> attachPeek(SerCons<U> attachPeek) {
        this.attachPeek = this.attachPeek.andThen(attachPeek);
        return this;
    }

    public <R> R query(BiFunction<Map<K, List<V>>, Map<V, A>, R> mapper) {
        Map<K, List<V>> middleKeyValuesMap = Steam.of(Database.list(middleWrapper)).parallel(isParallel).peek(middlePeek)
                .group(middleKey, Collective.mapping(Sf.of(middleValue).orGet(() -> SerFunc.<T, V>castingIdentity()::apply), Collective.toList()));
        if (Objects.isNull(attachKey)) {
            return mapper.apply(middleKeyValuesMap, new HashMap<>(middleKeyValuesMap.size()));
        }
        List<V> relationDataList = Steam.of(middleKeyValuesMap.values()).flat(Function.identity()).toList();
        Map<V, A> attachKeyValue = OneToOne.of(attachKey).in(relationDataList).value(attachValue).parallel(isParallel).peek(attachPeek).condition(attachQueryOperator).query();
        return mapper.apply(middleKeyValuesMap, attachKeyValue);
    }

    public Map<K, List<A>> query() {
        return query((middleKeyValuesMap, attachKeyValue) ->
                Maps.oneToManyToOne(middleKeyValuesMap, attachKeyValue, Steam::nonNull).parallel(isParallel)
                        .collect(Collective.entryToMap()));
    }
}
