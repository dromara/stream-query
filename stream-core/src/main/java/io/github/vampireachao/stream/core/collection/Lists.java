package io.github.vampireachao.stream.core.collection;

import java.util.*;

/**
 * <p>Lists class.</p>
 *
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
     * <p>of.</p>
     *
     * @param values 集合元素
     * @param <T>    a T class
     * @return {@link java.util.List}<{@code  T}> 集合元素类型
     */
    @SafeVarargs
    public static <T> List<T> of(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    /**
     * <p>ofColl.</p>
     *
     * @param values 集合元素
     * @param <T>    a T class
     * @return {@link java.util.List}<{@code  T}> 集合元素类型
     */
    public static <T> List<T> ofColl(Collection<T> values) {
        if (Objects.isNull(values)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(values);
    }

    /**
     * <p>first.</p>
     *
     * @param values a {@link java.util.List} object
     * @param <T>    a T class
     * @return a T object
     */
    public static <T> T first(List<T> values) {
        if (isEmpty(values)) {
            return null;
        }
        return values.get(0);
    }

    /**
     * <p>last.</p>
     *
     * @param values a {@link java.util.List} object
     * @param <T>    a T class
     * @return a T object
     */
    public static <T> T last(List<T> values) {
        if (isEmpty(values)) {
            return null;
        }
        return values.get(values.size() - 1);
    }

    /**
     * 升序排序
     *
     * @param list 需排序集合
     * @param <T>  a T class
     * @return {@link java.util.List}<{@code  T}>
     */
    public static <T> List<T> ascend(List<T> list) {
        list.sort(Collections.reverseOrder().reversed());
        return list;
    }

    /**
     * 降序排序
     *
     * @param list 需排序集合
     * @param <T>  a T class
     * @return {@link java.util.List}<{@code  T}>
     */
    public static <T> List<T> descend(List<T> list) {
        list.sort(Collections.reverseOrder());
        return list;
    }

    /**
     * 翻转集合元素
     *
     * @param list 需翻转集合元素
     * @param <T>  a T class
     * @return {@link java.util.List}<{@code  T}>
     */
    public static <T> List<T> reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * <p>binarySearch.</p>
     *
     * @param list 要查找的集合
     * @param key  想要查找的数据
     * @param <T>  a T class
     * @return int 坐标
     */
    public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        return Collections.binarySearch(ascend(list), key);
    }

    /**
     * <p>isEmpty.</p>
     *
     * @param list a {@link java.util.List} object
     * @return a boolean
     */
    public static boolean isEmpty(List<?> list) {
        return Objects.isNull(list) || list.isEmpty();
    }

    /**
     * <p>isNotEmpty.</p>
     *
     * @param list a {@link java.util.List} object
     * @return a boolean
     */
    public static boolean isNotEmpty(List<?> list) {
        return Objects.nonNull(list) && !list.isEmpty();
    }

    /**
     * <p>empty.</p>
     *
     * @param <T> a T class
     * @return a {@link java.util.List} object
     */
    public static <T> List<T> empty() {
        return new ArrayList<>();
    }
}
