package io.github.vampireachao.stream.core.collection;

import io.github.vampireachao.stream.core.lambda.LambdaHelper;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.optional.Opp;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
     * @param key 想要查找的数据
     * @return int 坐标
     */
    public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        return Collections.binarySearch(ascend(list), key);
    }

    /**
     * 根据传入的字段进行排序，如当前操作的类已经实现Comparable接口的话则优先使用自定义的方法
     * @param list 操作集合
     * @param serFunc 排序字段
     * @param serSupp return True ? ASC : DESC !!! 如实现了自定义排序方法的话请默认生成True,否则False会对排序进行翻转
     * @param <T> 集合元素类型
     * @param <R> 操作字段类型
     * @return 排序后返回 {@link java.util.List}
     */
    public static  <T,R> List<T> customSort(List<T> list, SerFunc<T,R> serFunc,
                                      SerSupp<Boolean> serSupp) {
        if (Opp.ofColl(list).isEmpty()) {
            return list;
        }

        list.sort( (a, b) -> {
            AtomicInteger ret = new AtomicInteger();
            Opp<String> ofTry = Opp.ofTry(() -> {
                String propertyName = LambdaHelper.getPropertyName(serFunc);
                Field field = ReflectHelper.getField(a.getClass(), propertyName);
                field.setAccessible(true);
                Class<?> type = field.getType();
                Class<?> aClass = a.getClass();
                if (ReflectHelper.isImplementsOf(aClass, Comparable.class)) {
                    ret.set((Integer)ReflectHelper.getMethod(aClass, "compareTo",aClass).invoke(a,b));
                } else if (type == Double.class) {
                    ret.set(((Double) field.get(a)).compareTo((Double) field.get(b)));
                } else if (type == Long.class) {
                    ret.set(((Long) field.get(a)).compareTo((Long) field.get(b)));
                } else if (type == Float.class) {
                    ret.set(((Float) field.get(a)).compareTo((Float) field.get(b)));
                } else if (type == Date.class) {
                    ret.set(((Date) field.get(a)).compareTo((Date) field.get(b)));
                } else if (type == Integer.class) {
                    ret.set(((Integer) field.get(a)).compareTo((Integer) field.get(b)));
                } else {
                    ret.set(String.valueOf(field.get(a)).compareTo(String.valueOf(field.get(b))));
                }
                return propertyName;
            }, SecurityException.class, NoSuchFieldException.class, IllegalAccessException.class, IllegalArgumentException.class);

            if (ofTry.isFail()) {
                throw new RuntimeException(ofTry.getException());
            }

            return serSupp.get() ? ret.get() : -ret.get();

        });
        return list;
    }




}
