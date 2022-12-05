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
public class TreeHelper<T, R extends Comparable<R>> {

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
     * <p>获取树先生的帮助</p>
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
    public static <T, R extends Comparable<R>> TreeHelper<T, R> of(SerFunc<T, R> idGetter,
                                                                   SerFunc<T, R> pidGetter,
                                                                   R pidValue,
                                                                   SerFunc<T, List<T>> childrenGetter,
                                                                   SerBiCons<T, List<T>> childrenSetter) {
        return new TreeHelper<>(idGetter, pidGetter, pidValue, null, childrenGetter, childrenSetter);
    }

    /**
     * <p>ofMatch.</p>
     *
     * @param idGetter        获取节点id操作  {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param pidGetter       获取父节点id操作 {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param parentPredicate 是否是父节点断言操作 {@link io.github.vampireachao.stream.core.lambda.function.SerPred} object
     * @param childrenGetter  获取子节点操作 { {@link io.github.vampireachao.stream.core.lambda.function.SerFunc} object
     * @param childrenSetter  操作子节点  {@link io.github.vampireachao.stream.core.lambda.function.SerBiCons} object
     * @param <T>             树节点类型 T class
     * @param <R>             父id类型 R class
     * @return a {@link io.github.vampireachao.stream.core.business.tree.TreeHelper} object
     */
    public static <T, R extends Comparable<R>> TreeHelper<T, R> ofMatch(SerFunc<T, R> idGetter,
                                                                        SerFunc<T, R> pidGetter,
                                                                        SerPred<T> parentPredicate,
                                                                        SerFunc<T, List<T>> childrenGetter,
                                                                        SerBiCons<T, List<T>> childrenSetter) {
        return new TreeHelper<>(idGetter, pidGetter, null, parentPredicate, childrenGetter, childrenSetter);
    }

    /**
     * <p>拾取果实构建树</p>
     *
     * @param list 果篮 {@link java.util.List} object
     * @return 树 {@link java.util.List} object
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
     * <p>将树上所有的果实摘取到果篮中</p>
     *
     * @param list 要摘取果实的树 {@link java.util.List} object
     * @return 果篮(包装果实的集合) {@link java.util.List} object
     */
    public List<T> flat(List<T> list) {
        AtomicReference<Function<T, Steam<T>>> recursiveRef = new AtomicReference<>();
        Function<T, Steam<T>> recursive = e -> Steam.of(childrenGetter.apply(e)).flat(recursiveRef.get()).unshift(e);
        recursiveRef.set(recursive);
        return Steam.of(list).flat(recursive).peek(e -> childrenSetter.accept(e, null)).toList();
    }

    /**
     * <p>将树中干净的果实摘取</p>
     *
     * @param list      要摘取的树 {@link java.util.List} object
     * @param condition 符合我们想要摘取果实的断言 {@link io.github.vampireachao.stream.core.lambda.function.SerPred} object
     * @return  摘取下来的果实重新组成的树 {@link java.util.List} object
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
     * <p>给你的树上果实包装一层商标叭</p>
     *
     * @param list   果篮 {@link java.util.List} object
     * @param action 加工商 {@link io.github.vampireachao.stream.core.lambda.function.SerCons} object
     * @return 完成包装的树 {@link java.util.List} object
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
