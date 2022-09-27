package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;

/**
 * 一对多对一
 *
 * @author VampireAchao ZVerify
 * @since 2022/5/24 14:15
 */
@SuppressWarnings("unchecked")
public class OneToManyToOne<T, U, K extends Serializable & Comparable<K>, V> extends BaseQueryHelper<OneToManyToOne<T, U, K, T>, OneToManyToOne<T, U, K, V>, T, K, V> {

    public OneToManyToOne(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    public static <T, U, K extends Serializable & Comparable<K>> OneToManyToOne<T, U, K, T> of(SFunction<T, K> keyFunction) {
        return new OneToManyToOne<>(keyFunction);
    }

    public <R> OneToManyToOne<T, U, K, R> value(SFunction<T, R> valueFunction) {
        attachDouble(valueFunction);
        return (OneToManyToOne<T, U, K, R>) this;
    }

    public OneToManyToOne<T, U, K, V> attachKey(SFunction<U, V> valueFunction) {
        return this;
    }




    /*private OneToManyToOne() {
     *//* Do not new me! *//*
    }

    // mainDataList middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // mainDataList middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // mainDataList middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // mainDataList middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        Set<$MIDDLE_VALUE> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Opp.of(OneToMany.query(middleQueryOperator, mainDataList, middleKey, middleValue, isParallel, middleConsumer, (middle, index) -> Opp.of(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Opp.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, isParallel, attachConsumer)).filter(keyAttachMap -> !keyAttachMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        Set<$MIDDLE_VALUE> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Opp.of(OneToMany.query(middleQueryOperator, mainDataList, middleKey, middleValue, isParallel, middleConsumer, (middle, index) -> Opp.of(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Opp.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, attachValue, isParallel, attachConsumer)).filter(attachKeyValueMap -> !attachKeyValueMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }

    // mainData middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, false, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, isParallel, SerBiCons.nothing(), SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, SerBiCons.nothing());
    }

    // mainData middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // mainData middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // mainData middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // mainData middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        Set<$MIDDLE_VALUE> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Opp.of(OneToMany.query(middleQueryOperator, mainData, middleKey, middleValue, isParallel, middleConsumer, (middle, index) -> Opp.of(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Opp.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, isParallel, attachConsumer)).filter(keyAttachMap -> !keyAttachMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, SerBiCons<$MIDDLE, Integer> middleConsumer, SerBiCons<$ATTACH, Integer> attachConsumer) {
        Set<$MIDDLE_VALUE> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Opp.of(OneToMany.query(middleQueryOperator, mainData, middleKey, middleValue, isParallel, middleConsumer, (middle, index) -> Opp.of(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Opp.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, attachValue, isParallel, attachConsumer)).filter(attachKeyValueMap -> !attachKeyValueMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }


    private static <$ATTACH, $MIDDLE_KEY extends Serializable & Comparable<$MIDDLE_KEY>, $MIDDLE_VALUE extends Serializable & Comparable<$MIDDLE_VALUE>> Function<Map<$MIDDLE_VALUE, $ATTACH>, Map<$MIDDLE_KEY, List<$ATTACH>>> convertMiddleToAttachFunction(boolean isParallel, Map<$MIDDLE_KEY, List<$MIDDLE_VALUE>> middleKeyValuesMap) {
        return keyAttachMap -> StreamSupport.stream(middleKeyValuesMap.entrySet().spliterator(), isParallel).collect(Collective.toMap(Map.Entry::getKey, middleKeyValuesEntry -> Opp.of(middleKeyValuesEntry.getValue()).filter(middleValues -> !middleValues.isEmpty()).map(middleValues -> StreamSupport.stream(middleValues.spliterator(), isParallel).map(keyAttachMap::get).filter(Objects::nonNull).collect(Collective.toList())).orElseGet(ArrayList::new)));
    }*/
}
