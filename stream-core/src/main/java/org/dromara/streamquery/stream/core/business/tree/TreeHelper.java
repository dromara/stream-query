/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.streamquery.stream.core.business.tree;

import org.dromara.streamquery.stream.core.lambda.function.SerBiCons;
import org.dromara.streamquery.stream.core.lambda.function.SerCons;
import org.dromara.streamquery.stream.core.lambda.function.SerFunc;
import org.dromara.streamquery.stream.core.lambda.function.SerPred;
import org.dromara.streamquery.stream.core.optional.Opp;
import org.dromara.streamquery.stream.core.stream.Steam;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 树先生
 *
 * @author VampireAchao Cizai_
 * @since 2022/11/26
 */
public class TreeHelper<T, R extends Comparable<? super R>> {

  private final SerFunc<T, R> idGetter;
  private final SerFunc<T, R> pidGetter;
  private final SerBiCons<T, Integer> levelSetter;
  private final SerFunc<T, Integer> levelGetter;
  private final R pidValue;
  private final SerPred<T> parentPredicate;
  private final SerFunc<T, List<T>> childrenGetter;
  private final SerBiCons<T, List<T>> childrenSetter;

  private TreeHelper(
      SerFunc<T, R> idGetter,
      SerFunc<T, R> pidGetter,
      SerBiCons<T, Integer> levelSetter,
      SerFunc<T, Integer> levelGetter,
      R pidValue,
      SerPred<T> parentPredicate,
      SerFunc<T, List<T>> childrenGetter,
      SerBiCons<T, List<T>> childrenSetter) {
    this.idGetter = idGetter;
    this.pidGetter = pidGetter;
    this.levelSetter = levelSetter;
    this.levelGetter = levelGetter;
    this.pidValue = pidValue;
    this.parentPredicate = parentPredicate;
    this.childrenGetter = childrenGetter;
    this.childrenSetter = childrenSetter;
  }

  /**
   * 通过提供节点信息构造树先生，此方法用于根节点为Null时
   *
   * @param idGetter 获取节点id操作 {@link SerFunc} object
   * @param pidGetter 获取父节点id操作 {@link SerFunc} object
   * @param levelSetter 保存当前节点的所在层级 {@link SerBiCons} Integer
   * @param pidValue 父节点值
   * @param childrenGetter 获取子节点操作 {@link SerFunc} object
   * @param childrenSetter 操作子节点 {@link SerBiCons} object
   * @param <T> 树节点类型
   * @param <R> 父id类型
   * @return a {@link TreeHelper} object
   */
  public static <T, R extends Comparable<? super R>> TreeHelper<T, R> of(
      SerFunc<T, R> idGetter,
      SerFunc<T, R> pidGetter,
      SerBiCons<T, Integer> levelSetter,
      SerFunc<T, Integer> levelGetter,
      R pidValue,
      SerFunc<T, List<T>> childrenGetter,
      SerBiCons<T, List<T>> childrenSetter) {
    return new TreeHelper<>(
        idGetter,
        pidGetter,
        levelSetter,
        levelGetter,
        pidValue,
        null,
        childrenGetter,
        childrenSetter);
  }

  /**
   * 通过提供节点信息构造树先生,此方法用于自定义(通过第三个参数判断返回True则为祖宗节点)根节点的值
   *
   * @param idGetter 获取节点id操作 {@link SerFunc} object
   * @param pidGetter 获取父节点id操作 {@link SerFunc} object
   * @param levelSetter 保存当前节点的所在层级 {@link SerBiCons} Integer
   * @param levelGetter 拿到当前节点所在的层级 {@link SerPred} object
   * @param parentPredicate 是否是祖宗节点断言操作 {@link SerPred} object
   * @param childrenGetter 获取子节点操作 { {@link SerFunc} object
   * @param childrenSetter 操作子节点 {@link SerBiCons} object
   * @param <T> 树节点类型 T class
   * @param <R> 父id类型 R class
   * @return a {@link TreeHelper} object
   */
  public static <T, R extends Comparable<? super R>> TreeHelper<T, R> ofMatch(
      SerFunc<T, R> idGetter,
      SerFunc<T, R> pidGetter,
      SerBiCons<T, Integer> levelSetter,
      SerFunc<T, Integer> levelGetter,
      SerPred<T> parentPredicate,
      SerFunc<T, List<T>> childrenGetter,
      SerBiCons<T, List<T>> childrenSetter) {
    return new TreeHelper<>(
        idGetter,
        pidGetter,
        levelSetter,
        levelGetter,
        null,
        parentPredicate,
        childrenGetter,
        childrenSetter);
  }

  /**
   * 传入List集合通过创建树先生时所传入信息去构造树结构
   *
   * @param list list 需要构建树结构的集合 {@link java.util.List} object
   * @param level {@link Integer} object 要生成节点的层数 null则为生成到最后一层
   * @return 符合树结构的集合 {@link java.util.List} object
   */
  public List<T> toTree(List<T> list, Integer level) {
    if (Objects.isNull(parentPredicate)) {
      final Map<R, List<T>> pIdValuesMap =
          Steam.of(list).filter(e -> Objects.nonNull(idGetter.apply(e))).group(pidGetter);
      final List<T> parents = pIdValuesMap.getOrDefault(pidValue, new ArrayList<>());
      return getTreeSet(level, pIdValuesMap, parents);
    }
    final List<T> parents = new ArrayList<>(list.size());
    final Map<R, List<T>> pIdValuesMap =
        Steam.of(list)
            .filter(
                e -> {
                  if (parentPredicate.test(e)) {
                    parents.add(e);
                  }
                  return Objects.nonNull(idGetter.apply(e));
                })
            .group(pidGetter);
    return getTreeSet(level, pIdValuesMap, parents);
  }

  private List<T> getTreeSet(Integer level, Map<R, List<T>> pIdValuesMap, List<T> parents) {
    for (T parent : parents) {
      levelSetter.accept(parent, 0);
      if (level == null || level > 0) {
        getChildrenFromMapByPidAndSet(pIdValuesMap, parent, level == null ? null : 0);
      } else {
        childrenSetter.accept(parent, Collections.emptyList());
      }
    }
    return parents;
  }

  private void getChildrenFromMapByPidAndSet(
      Map<R, List<T>> pIdValuesMap, T parent, Integer currentLevel) {
    if (currentLevel != null && currentLevel < 0) {
      childrenSetter.accept(parent, Collections.emptyList());
      return;
    }

    List<T> children = pIdValuesMap.get(idGetter.apply(parent));
    if (Opp.ofColl(children).isEmpty()) {
      if (currentLevel == null) {
        Integer parentLevel = levelGetter.apply(parent);
        if (parentLevel != null) {
          currentLevel = parentLevel + 1;
        } else {
          currentLevel = 1;
        }
      }
      levelSetter.accept(parent, currentLevel);
      return;
    }

    for (T child : children) {
      childrenSetter.accept(parent, children);
      levelSetter.accept(child, currentLevel == null ? 1 : currentLevel + 1);
      getChildrenFromMapByPidAndSet(
          pIdValuesMap, child, currentLevel == null ? null : currentLevel + 1);
    }
  }

  /**
   * 将树结构进行扁平化
   *
   * @param list 要操作的树结构 {@link java.util.List} object
   * @return 扁平化之后的集合 {@link java.util.List} object
   */
  public List<T> flat(List<T> list) {
    AtomicReference<Function<T, Steam<T>>> recursiveRef = new AtomicReference<>();
    Function<T, Steam<T>> recursive =
        e -> Steam.of(childrenGetter.apply(e)).flat(recursiveRef.get()).unshift(e);
    recursiveRef.set(recursive);
    return Steam.of(list).flat(recursive).peek(e -> childrenSetter.accept(e, null)).toList();
  }

  /**
   * 根据给定的条件过滤列表中的元素，并且递归过滤子元素列表
   *
   * @param list 要过滤的列表 {@link java.util.List} object
   * @param condition 过滤条件 {@link SerPred} object
   * @return 过滤后的列表 {@link java.util.List} object
   */
  public List<T> filter(List<T> list, SerPred<T> condition) {
    AtomicReference<Predicate<T>> recursiveRef = new AtomicReference<>();
    Predicate<T> recursive =
        SerPred.multiOr(
            condition::test,
            e ->
                Opp.ofColl(childrenGetter.apply(e))
                    .map(children -> Steam.of(children).filter(recursiveRef.get()).toList())
                    .peek(children -> childrenSetter.accept(e, children))
                    .is(s -> !s.isEmpty()));
    recursiveRef.set(recursive);
    return Steam.of(list).filter(recursive).toList();
  }

  /**
   * 对列表中的元素以及它们的子元素列表进行递归遍历，并在每个元素上执行给定的操作
   *
   * @param list 要操作的树 {@link java.util.List} object
   * @param action 要执行的操作 {@link SerCons} object
   * @return 操作结果 {@link java.util.List} object
   */
  public List<T> forEach(List<T> list, SerCons<T> action) {
    AtomicReference<Consumer<T>> recursiveRef = new AtomicReference<>();
    Consumer<T> recursive =
        SerCons.multi(
            action::accept,
            e ->
                Opp.ofColl(childrenGetter.apply(e))
                    .peek(children -> Steam.of(children).forEach(recursiveRef.get())));
    recursiveRef.set(recursive);
    Steam.of(list).forEach(recursive);
    return list;
  }

  // 获取节点的最大深度
  private int getDepth(T node) {
    List<T> children = childrenGetter.apply(node);
    if (children == null || children.isEmpty()) {
      return 1;
    }
    int maxDepth = 0;
    for (T child : children) {
      int depth = getDepth(child);
      if (depth > maxDepth) {
        maxDepth = depth;
      }
    }
    return maxDepth + 1;
  }
}
