package io.github.vampireachao.stream.core.collection;

import io.github.vampireachao.stream.core.stream.Steam;

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
     * @return {@link List}<{@link T}>
     */
    @SafeVarargs
    public static <T> List<T> of(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    /**
     * 翻转集合元素
     *
     * @param list 需翻转集合元素
     * @return {@link List}<{@link T}> 翻转之后集合元素
     */
    public static <T> List<T> reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * 升序排序
     *
     * @param list 需排序集合
     * @return {@link List}<{@link T}> 排序后集合
     */
    public static <T> List<T> ascend(List<T> list) {
        list.sort(null);
        return list;
    }

    /**
     * 降序排序
     *
     * @param list 需排序集合
     * @return {@link List}<{@link T}> 排序后集合
     */
    public static <T> List<T> descend(List<T> list) {
        list.sort(Collections.reverseOrder());
        return list;
    }

    /**
     * 查找指定集合中的元素，如不存在返回-1
     * @param list 要查询集合
     * @param objectives 要查找元素
     * @return {@link Integer} 所查找元素的索引，如不存在返回-1
     */
    public static <T> Integer binarySearch(List<T> list,T objectives) {

       return Steam.of(list).findFirstIdx((value) -> value == objectives);
    }


}
