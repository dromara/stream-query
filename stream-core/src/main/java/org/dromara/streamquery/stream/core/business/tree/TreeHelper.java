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

import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
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
 * @author VampireAchao Cizai_ lmr
 * @since 2022/11/26
 */
public class TreeHelper<T, R extends Comparable<? super R>> {

  private final SerFunc<T, R> idGetter;
  private final SerFunc<T, R> pidGetter;
  private final R pidValue;
  private final SerPred<T> parentPredicate;
  private final SerFunc<T, List<T>> childrenGetter;
  private SerBiCons<T, List<T>> childrenSetter;

  public SerBiCons<T, List<T>> getChildrenSetter() {
    return childrenSetter;
  }

  public void setChildrenSetter(SerBiCons<T, List<T>> childrenSetter) {
    this.childrenSetter = childrenSetter;
  }

  private TreeHelper(
      SerFunc<T, R> idGetter,
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
    this.childrenSetter =
        Opp.of(childrenSetter).orElseGet(() -> LambdaHelper.getSetter(childrenGetter));
  }

  /**
   * 根据给定的节点列表和选中的ID，选择一个级联路径。 使用流处理每个根节点，并尝试找到从根节点到所选ID的路径。 如果找到路径，则返回该路径；如果在所有节点中都未找到路径，则返回空列表。
   *
   * @param nodes 包含树形结构根节点的列表。
   * @param selectedId 要查找的选中节点的ID。
   * @return 选中节点的路径列表，如果未找到则为空列表。
   */
  public List<T> cascadeSelect(List<T> nodes, R selectedId) {
    return Steam.of(nodes)
        .map(rootNode -> findPath(rootNode, selectedId))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst()
        .orElseGet(Collections::emptyList);
  }

  /**
   * 递归方法，用于查找从当前节点到所选ID的路径。 如果当前节点或其任一子节点的ID与所选ID匹配，则构建并返回该路径。 如果在当前节点的子树中未找到所选ID，则返回一个空的Optional。
   *
   * @param currentNode 正在检查的当前节点。
   * @param selectedId 要查找的选中节点的ID。
   * @return 包含从当前节点到所选ID的路径的Optional对象，如果未找到则为空。
   */
  private Optional<List<T>> findPath(T currentNode, R selectedId) {
    if (currentNode == null) {
      return Optional.empty();
    }

    List<T> path = new ArrayList<>();
    path.add(currentNode);

    if (idGetter.apply(currentNode).equals(selectedId)) {
      return Optional.of(path);
    }

    return Steam.of(childrenGetter.apply(currentNode))
        .map(child -> findPath(child, selectedId))
        .filter(Optional::isPresent)
        .findFirst()
        .map(
            childPath -> {
              path.addAll(childPath.get());
              return path;
            });
  }

  /**
   * 通过提供节点信息构造树先生，此方法用于根节点为Null时
   *
   * @param idGetter 获取节点id操作 {@link SerFunc} object
   * @param pidGetter 获取父节点id操作 {@link SerFunc} object
   * @param pidValue 父节点值
   * @param childrenGetter 获取子节点操作 {@link SerFunc} object
   * @param childrenSetter 操作子节点 {@link SerBiCons} object
   * @param <T> 树节点类型
   * @param <R> 父id类型
   * @return a {@link TreeHelper} object
   * @deprecated {@link TreeHelper#of(SerFunc, SerFunc, Comparable, SerFunc)}
   */
  @Deprecated
  public static <T, R extends Comparable<? super R>> TreeHelper<T, R> of(
      SerFunc<T, R> idGetter,
      SerFunc<T, R> pidGetter,
      R pidValue,
      SerFunc<T, List<T>> childrenGetter,
      SerBiCons<T, List<T>> childrenSetter) {
    return new TreeHelper<>(idGetter, pidGetter, pidValue, null, childrenGetter, childrenSetter);
  }

  /**
   * 通过提供节点信息构造树先生，此方法用于根节点为Null时
   *
   * @param idGetter 获取节点id操作 {@link SerFunc} object
   * @param pidGetter 获取父节点id操作 {@link SerFunc} object
   * @param pidValue 父节点值
   * @param childrenGetter 获取子节点操作 {@link SerFunc} object
   * @param <T> 树节点类型
   * @param <R> 父id类型
   * @return a {@link TreeHelper} object
   */
  public static <T, R extends Comparable<? super R>> TreeHelper<T, R> of(
      SerFunc<T, R> idGetter,
      SerFunc<T, R> pidGetter,
      R pidValue,
      SerFunc<T, List<T>> childrenGetter) {
    return new TreeHelper<>(idGetter, pidGetter, pidValue, null, childrenGetter, null);
  }

  /**
   * 通过提供节点信息构造树先生,此方法用于自定义(通过第三个参数判断返回True则为祖宗节点)根节点的值
   *
   * @param idGetter 获取节点id操作 {@link SerFunc} object
   * @param pidGetter 获取父节点id操作 {@link SerFunc} object
   * @param parentPredicate 是否是祖宗节点断言操作 {@link SerPred} object
   * @param childrenGetter 获取子节点操作 { {@link SerFunc} object
   * @param childrenSetter 操作子节点 {@link SerBiCons} object
   * @param <T> 树节点类型 T class
   * @param <R> 父id类型 R class
   * @return a {@link TreeHelper} object
   * @deprecated {@link TreeHelper#ofMatch(SerFunc, SerFunc, SerPred, SerFunc)}
   */
  @Deprecated
  public static <T, R extends Comparable<? super R>> TreeHelper<T, R> ofMatch(
      SerFunc<T, R> idGetter,
      SerFunc<T, R> pidGetter,
      SerPred<T> parentPredicate,
      SerFunc<T, List<T>> childrenGetter,
      SerBiCons<T, List<T>> childrenSetter) {
    return new TreeHelper<>(
        idGetter, pidGetter, null, parentPredicate, childrenGetter, childrenSetter);
  }

  /**
   * 通过提供节点信息构造树先生,此方法用于自定义(通过第三个参数判断返回True则为祖宗节点)根节点的值
   *
   * @param idGetter 获取节点id操作 {@link SerFunc} object
   * @param pidGetter 获取父节点id操作 {@link SerFunc} object
   * @param parentPredicate 是否是祖宗节点断言操作 {@link SerPred} object
   * @param childrenGetter 获取子节点操作 { {@link SerFunc} object
   * @param <T> 树节点类型 T class
   * @param <R> 父id类型 R class
   * @return a {@link TreeHelper} object
   */
  public static <T, R extends Comparable<? super R>> TreeHelper<T, R> ofMatch(
      SerFunc<T, R> idGetter,
      SerFunc<T, R> pidGetter,
      SerPred<T> parentPredicate,
      SerFunc<T, List<T>> childrenGetter) {
    return new TreeHelper<>(idGetter, pidGetter, null, parentPredicate, childrenGetter, null);
  }

  /**
   * 传入List集合通过创建树先生时所传入信息去构造树结构
   *
   * @param list list 需要构建树结构的集合 {@link java.util.List} object
   * @return 符合树结构的集合 {@link java.util.List} object
   */
  public List<T> toTree(List<T> list) {
    return toTree(list, null, null);
  }

  public List<T> toTree(List<T> list, SerBiCons<T, Integer> levelSetter) {
    return toTree(list, null, levelSetter);
  }

  /**
   * 传入List集合通过创建树先生时所传入信息去构造树结构
   *
   * @param list list 需要构建树结构的集合 {@link java.util.List} object
   * @param level {@link Integer} object 要生成节点的层数 null则为生成到最后一层
   * @return 符合树结构的集合 {@link java.util.List} object
   */
  public List<T> toTree(List<T> list, Integer level, SerBiCons<T, Integer> levelSetter) {
    if (level != null && level < 0) {
      return new ArrayList<>();
    }
    if (Objects.isNull(parentPredicate)) {
      final Map<R, List<T>> pIdValuesMap = Steam.of(list).nonNull(idGetter).group(pidGetter);
      final List<T> parents = pIdValuesMap.getOrDefault(pidValue, new ArrayList<>());
      return getTreeSet(level, pIdValuesMap, parents, levelSetter);
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
    return getTreeSet(level, pIdValuesMap, parents, levelSetter);
  }

  private List<T> getTreeSet(
      Integer level,
      Map<R, List<T>> pIdValuesMap,
      List<T> parents,
      SerBiCons<T, Integer> levelSetter) {
    for (T parent : parents) {
      if (null != levelSetter) {
        levelSetter.accept(parent, 0);
      }
      getChildrenFromMapByPidAndSet(
          pIdValuesMap, parent, level == null ? Integer.MAX_VALUE : level, 0, levelSetter);
    }
    return parents;
  }

  private void getChildrenFromMapByPidAndSet(
      Map<R, List<T>> pIdValuesMap,
      T parent,
      Integer level,
      Integer currentLevel,
      SerBiCons<T, Integer> levelSetter) {
    if (currentLevel >= level) {
      return;
    }

    List<T> children = pIdValuesMap.get(idGetter.apply(parent));
    if (Opp.ofColl(children).isEmpty()) {
      return;
    }

    childrenSetter.accept(parent, children);
    for (T child : children) {
      if (null != levelSetter) {
        levelSetter.accept(child, currentLevel + 1);
      }
      getChildrenFromMapByPidAndSet(pIdValuesMap, child, level, currentLevel + 1, levelSetter);
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

  /**
   * 获取以给定节点为根的树的深度
   *
   * @param node 要获取深度的节点
   * @return 以该节点为根的树的深度
   */
  public int getDepth(T node) {
    Queue<T> queue = new LinkedList<>();
    queue.offer(node);
    int maxDepth = 0;
    while (Opp.ofColl(queue).isPresent()) {
      // 每层节点的数量
      for (int i = 0; i < queue.size(); i++) {
        Steam.of(childrenGetter.apply(queue.poll())).forEach(queue::offer);
      }
      // 当前层的节点已经全部出队，深度加一
      maxDepth++;
    }
    return maxDepth;
  }
}
