package io.github.vampireachao.stream.core.business.tree;

import io.github.vampireachao.stream.core.lambda.function.SerBiCons;
import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerPred;
import io.github.vampireachao.stream.core.optional.Opp;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 树先生
 *
 * @author VampireAchao
 * @since 2022/11/26
 */
public class TreeHelper<T, R extends Comparable<? super R>> {

    private final SerFunc<T, R> idGetter;
    private final SerFunc<T, R> pidGetter;
    private final R pidValue;
    private final SerPred<T> parentPredicate;
    private final SerFunc<T, List<T>> childrenGetter;
    private final SerBiCons<T, List<T>> childrenSetter;

    private TreeHelper(SerFunc<T, R> idGetter,
                       SerFunc<T, R> pidGetter,
                       R pidValue,
                       SerPred<T> parentPredicate,
                       SerFunc<T, List<T>> childrenGetter,
                       SerBiCons<T, List<T>> childrenSetter) {
        this.idGetter = idGetter;
        this.pidGetter = pidGetter;
        this.pidValue = pidValue;
        this.parentPredicate = parentPredicate;
        this.childrenGetter = childrenGetter;
        this.childrenSetter = childrenSetter;
    }

    /**
     * <p>通过提供节点信息构造树先生，此方法用于根节点为Null时</p>
     *
     * @param idGetter       获取节点id操作 {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param pidGetter      获取父节点id操作 {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param pidValue       父节点值
     * @param childrenGetter 获取子节点操作 {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param childrenSetter 操作子节点 {@link io.github.vampireachao.stream.core.lambda.function.SerBiCons} object
     * @param <T>            树节点类型
     * @param <R>            父id类型
     * @return a {@link io.github.vampireachao.stream.core.business.tree.TreeHelper} object
     */
    public static <T, R extends Comparable<? super R>> TreeHelper<T, R> of(SerFunc<T, R> idGetter,
                                                                           SerFunc<T, R> pidGetter,
                                                                           R pidValue,
                                                                           SerFunc<T, List<T>> childrenGetter,
                                                                           SerBiCons<T, List<T>> childrenSetter) {
        return new TreeHelper<>(idGetter, pidGetter, pidValue, null, childrenGetter, childrenSetter);
    }

    /**
     * <p>通过提供节点信息构造树先生,此方法用于自定义(通过第三个参数判断返回True则为祖宗节点)根节点的值</p>
     *
     * @param idGetter        获取节点id操作  {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param pidGetter       获取父节点id操作 {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param parentPredicate 是否是祖宗节点断言操作 {@link io.github.vampireachao.stream.core.lambda.function.SerPred} object
     * @param childrenGetter  获取子节点操作 { {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param childrenSetter  操作子节点  {@link io.github.vampireachao.stream.core.lambda.function.SerBiCons} object
     * @param <T>             树节点类型 T class
     * @param <R>             父id类型 R class
     * @return a {@link io.github.vampireachao.stream.core.business.tree.TreeHelper} object
     */
    public static <T, R extends Comparable<? super R>> TreeHelper<T, R> ofMatch(SerFunc<T, R> idGetter,
                                                                                SerFunc<T, R> pidGetter,
                                                                                SerPred<T> parentPredicate,
                                                                                SerFunc<T, List<T>> childrenGetter,
                                                                                SerBiCons<T, List<T>> childrenSetter) {
        return new TreeHelper<>(idGetter, pidGetter, null, parentPredicate, childrenGetter, childrenSetter);
    }

    /**
     * <p>传入List集合通过创建树先生时所传入信息去构造树结构</p>
     *
     * @param list 需要构建树结构的集合 {@link java.util.List} object
     * @return 符合树结构的集合 {@link java.util.List} object
     */
    public List<T> toTree(List<T> list) {
        if (Objects.isNull(parentPredicate)) {
            final Map<R, List<T>> pIdValuesMap = Steam.of(list).filter(e -> Objects.nonNull(idGetter.apply(e))).group(pidGetter);
            final List<T> parents = pIdValuesMap.getOrDefault(pidValue, new ArrayList<>());
            getChildrenFromMapByPidAndSet(pIdValuesMap);
            return parents;
        }
        final List<T> parents = new ArrayList<>(list.size());
        final Map<R, List<T>> pIdValuesMap = Steam.of(list).filter(e -> {
            if (parentPredicate.test(e)) {
                parents.add(e);
            }
            return Objects.nonNull(idGetter.apply(e));
        }).group(pidGetter);
        getChildrenFromMapByPidAndSet(pIdValuesMap);
        return parents;
    }

    /**
     * <p>将树结构进行扁平化</p>
     *
     * @param list 要操作的树结构 {@link java.util.List} object
     * @return 扁平化之后的集合 {@link java.util.List} object
     */
    public List<T> flat(List<T> list) {
        AtomicReference<Function<T, Steam<T>>> recursiveRef = new AtomicReference<>();
        Function<T, Steam<T>> recursive = e -> Steam.of(childrenGetter.apply(e)).flat(recursiveRef.get()).unshift(e);
        recursiveRef.set(recursive);
        return Steam.of(list).flat(recursive).peek(e -> childrenSetter.accept(e, null)).toList();
    }

    /**
     * <p>根据给定的条件过滤列表中的元素，并且递归过滤子元素列表</p>
     *
     * @param list      要过滤的列表 {@link java.util.List} object
     * @param condition 过滤条件 {@link io.github.vampireachao.stream.core.lambda.function.SerPred} object
     * @return  过滤后的列表 {@link java.util.List} object
     */
    public List<T> filter(List<T> list, SerPred<T> condition) {
        AtomicReference<Predicate<T>> recursiveRef = new AtomicReference<>();
        Predicate<T> recursive = SerPred.multiOr(condition::test,
                e -> Opp.ofColl(childrenGetter.apply(e))
                        .map(children -> Steam.of(children).filter(recursiveRef.get()).toList())
                        .peek(children -> childrenSetter.accept(e, children))
                        .is(s -> !s.isEmpty()));
        recursiveRef.set(recursive);
        return Steam.of(list).filter(recursive).toList();
    }

    /**
     * <p>对列表中的元素以及它们的子元素列表进行递归遍历，并在每个元素上执行给定的操作</p>
     *
     * @param list   要操作的树 {@link java.util.List} object
     * @param action 要执行的操作 {@link io.github.vampireachao.stream.core.lambda.function.SerCons} object
     * @return 操作结果 {@link java.util.List} object
     */
    public List<T> forEach(List<T> list, SerCons<T> action) {
        AtomicReference<Consumer<T>> recursiveRef = new AtomicReference<>();
        Consumer<T> recursive = SerCons.multi(action::accept,
                e -> Opp.ofColl(childrenGetter.apply(e))
                        .peek(children -> Steam.of(children).forEach(recursiveRef.get())));
        recursiveRef.set(recursive);
        Steam.of(list).forEach(recursive);
        return list;
    }

    private void getChildrenFromMapByPidAndSet(Map<R, List<T>> pIdValuesMap) {
        Steam.of(pIdValuesMap.values()).flat(Function.identity())
                .forEach(value -> {
                    final List<T> children = pIdValuesMap.get(idGetter.apply(value));
                    if (children != null) {
                        childrenSetter.accept(value, children);
                    }
                });
    }

}
