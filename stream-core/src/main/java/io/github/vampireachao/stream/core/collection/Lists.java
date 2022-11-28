package io.github.vampireachao.stream.core.collection;

import java.util.*;

/**
 * @author VampireAchao 请我吃早饭
 * @since 2022/10/21 17:08
 */
public class Lists {

    /**
     *
     */
    private Lists() {
        /* Do not new me! */
    }

    /**
     * @param values 集合元素
     * @return {@link List}<{@code  T}> 集合元素类型
     */
    @SafeVarargs
    public static <T> List<T> of(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    /**
     * @param values 集合元素
     * @return {@link List}<{@code  T}> 集合元素类型
     */
    public static <T> List<T> ofColl(Collection<T> values) {
        if (Objects.isNull(values)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(values);
    }

    /**
     * 升序排序
     *
     * @param list 需排序集合
     * @return {@link List}<{@code  T}>
     */
    public static <T> List<T> ascend(List<T> list) {
        list.sort(Collections.reverseOrder().reversed());
        return list;
    }

    /**
     * 降序排序
     *
     * @param list 需排序集合
     * @return {@link List}<{@code  T}>
     */
    public static <T> List<T> descend(List<T> list) {
        list.sort(Collections.reverseOrder());
        return list;
    }

    /**
     * 翻转集合元素
     *
     * @param list 需翻转集合元素
     * @return {@link List}<{@code  T}>
     */
    public static <T> List<T> reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * @param list 要查找的集合
     * @param key  想要查找的数据
     * @return int 坐标
     */
    public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        return Collections.binarySearch(ascend(list), key);
    }


}
