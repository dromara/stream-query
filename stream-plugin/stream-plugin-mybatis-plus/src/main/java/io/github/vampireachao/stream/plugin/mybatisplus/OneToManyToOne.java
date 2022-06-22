package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.collector.Collectors;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.StreamSupport;

/**
 * 一对多对一
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt;
 * @since 2022/5/24 14:15
 */
public class OneToManyToOne {

    private OneToManyToOne() {
        /* Do not new me! */
    }

    // mainDataList middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middle -> {}, attach -> {});
    }

    // mainDataList middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middle -> {}, attach -> {});
    }

    // mainDataList middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middle -> {}, attach -> {});
    }

    // mainDataList middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middle -> {}, attach -> {});
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middle -> {}, attach -> {});
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middle -> {}, attach -> {});
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middle -> {}, attach -> {});
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middle -> {}, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, false, middle -> {}, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, false, middle -> {}, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, isParallel, middle -> {}, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middle -> {}, attach -> {});
    }

    // mainDataList middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attach -> {});
    }

    // mainDataList middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attach -> {});
    }

    // mainDataList middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, attach -> {});
    }

    // mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attach -> {});
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attach -> {});
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attach -> {});
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, attach -> {});
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attach -> {});
    }

    // mainDataList middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // mainDataList middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // mainDataList middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainDataList, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainDataList, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        Set<$MIDDLE_VALUE> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Optional.of(OneToMany.query(middleQueryOperator, mainDataList, middleKey, middleValue, isParallel, middleConsumer, middle -> Optional.ofNullable(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Optional.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, isParallel, attachConsumer)).filter(keyAttachMap -> !keyAttachMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }

    // middleQueryOperator attachQueryOperator mainDataList middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, Collection<$MIDDLE_KEY> mainDataList, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        Set<$MIDDLE_VALUE> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Optional.of(OneToMany.query(middleQueryOperator, mainDataList, middleKey, middleValue, isParallel, middleConsumer, middle -> Optional.ofNullable(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Optional.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, attachValue, isParallel, attachConsumer)).filter(attachKeyValueMap -> !attachKeyValueMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }

    // mainData middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middle -> {}, attach -> {});
    }

    // mainData middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middle -> {}, attach -> {});
    }

    // mainData middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middle -> {}, attach -> {});
    }

    // mainData middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middle -> {}, attach -> {});
    }

    // middleQueryOperator mainData middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middle -> {}, attach -> {});
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middle -> {}, attach -> {});
    }

    // middleQueryOperator mainData middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middle -> {}, attach -> {});
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middle -> {}, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, false, middle -> {}, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, false, middle -> {}, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, isParallel, middle -> {}, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue parallel

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middle -> {}, attach -> {});
    }

    // mainData middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, attach -> {});
    }

    // mainData middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attach -> {});
    }

    // mainData middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, attach -> {});
    }

    // mainData middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attach -> {});
    }

    // middleQueryOperator mainData middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, attach -> {});
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attach -> {});
    }

    // middleQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, attach -> {});
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, false, middleConsumer, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, attach -> {});
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attach -> {});
    }

    // mainData middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // mainData middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // mainData middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // mainData middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query($MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(UnaryOperator.identity(), UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, UnaryOperator.identity(), mainData, middleKey, middleValue, attachKey, attachValue, isParallel, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        return query(middleQueryOperator, attachQueryOperator, mainData, middleKey, middleValue, attachKey, attachValue, false, middleConsumer, attachConsumer);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        Set<$MIDDLE_VALUE> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Optional.of(OneToMany.query(middleQueryOperator, mainData, middleKey, middleValue, isParallel, middleConsumer, middle -> Optional.ofNullable(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Optional.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, isParallel, attachConsumer)).filter(keyAttachMap -> !keyAttachMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }

    // middleQueryOperator attachQueryOperator mainData middleKey middleValue attachKey attachValue parallel middleConsumer attachConsumer

    public static <$MIDDLE, $ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable, $ATTACH_VALUE extends Serializable> Map<$MIDDLE_KEY, List<$ATTACH_VALUE>> query(UnaryOperator<LambdaQueryWrapper<$MIDDLE>> middleQueryOperator, UnaryOperator<LambdaQueryWrapper<$ATTACH>> attachQueryOperator, $MIDDLE_KEY mainData, SFunction<$MIDDLE, $MIDDLE_KEY> middleKey, SFunction<$MIDDLE, $MIDDLE_VALUE> middleValue, SFunction<$ATTACH, $MIDDLE_VALUE> attachKey, SFunction<$ATTACH, $ATTACH_VALUE> attachValue, boolean isParallel, Consumer<$MIDDLE> middleConsumer, Consumer<$ATTACH> attachConsumer) {
        Set<$MIDDLE_VALUE> middleFlatValues = isParallel ? new LinkedHashSet<>() : new HashSet<>();
        return Optional.of(OneToMany.query(middleQueryOperator, mainData, middleKey, middleValue, isParallel, middleConsumer, middle -> Optional.ofNullable(middle).map(middleValue).ifPresent(middleFlatValues::add))).filter(middleKeyValuesMap -> !middleKeyValuesMap.isEmpty()).flatMap(middleKeyValuesMap -> Optional.of(OneToOne.query(attachQueryOperator, middleFlatValues, attachKey, attachValue, isParallel, attachConsumer)).filter(attachKeyValueMap -> !attachKeyValueMap.isEmpty()).map(convertMiddleToAttachFunction(isParallel, middleKeyValuesMap))).orElseGet(HashMap::new);
    }


    private static <$ATTACH, $MIDDLE_KEY extends Serializable, $MIDDLE_VALUE extends Serializable> Function<Map<$MIDDLE_VALUE, $ATTACH>, Map<$MIDDLE_KEY, List<$ATTACH>>> convertMiddleToAttachFunction(boolean isParallel, Map<$MIDDLE_KEY, List<$MIDDLE_VALUE>> middleKeyValuesMap) {
        return keyAttachMap -> StreamSupport.stream(middleKeyValuesMap.entrySet().spliterator(), isParallel).collect(Collectors.toMap(Map.Entry::getKey, middleKeyValuesEntry -> Optional.ofNullable(middleKeyValuesEntry.getValue()).filter(middleValues -> !middleValues.isEmpty()).map(middleValues -> StreamSupport.stream(middleValues.spliterator(), isParallel).map(keyAttachMap::get).collect(Collectors.toList())).orElseGet(ArrayList::new)));
    }
}
