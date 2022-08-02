package io.github.vampireachao.stream.core.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;

/**
 * 容器相关工具类
 * <p>来自hutool</p>
 *
 * @author looly
 */
public class CollUtil {

    /**
     * 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 获取匹配规则定义中匹配到元素的最后位置<br>
     * 此方法对于某些无序集合的位置信息，以转换为数组后的位置为准。
     *
     * @param <T>     元素类型
     * @param list    List集合
     * @param matcher 匹配器，为空则全部匹配
     * @return 最后一个位置
     * @since 5.6.6
     */
    public static <T> int lastIndexOf(final List<T> list, final Predicate<? super T> matcher) {
        if (null != list) {
            final int size = list.size();
            if (size > 0) {
                for (int i = size - 1; i >= 0; i--) {
                    if (null == matcher || matcher.test(list.get(i))) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 获取集合中指定下标的元素值，下标可以为负数，例如-1表示最后一个元素<br>
     * 如果元素越界，返回null
     *
     * @param <T>        元素类型
     * @param list 集合
     * @param index      下标，支持负数
     * @return 元素值
     * @since 4.0.6
     */
    public static <T> T get(final List<T> list, int index) {
        if (null == list) {
            return null;
        }

        final int size = list.size();
        if (0 == size) {
            return null;
        }

        if (index < 0) {
            index += size;
        }

        // 检查越界
        if (index >= size || index < 0) {
            return null;
        }

        return list.get(index);
    }

    /**
     * 新建一个HashMap
     *
     * @param <K>  Key类型
     * @param <V>  Value类型
     * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
     * @return HashMap对象
     */
    public static <K, V> HashMap<K, V> newHashMap(final int size) {
        return newHashMap(size, false);
    }

    /**
     * 新建一个HashMap
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param size     初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
     * @param isLinked Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
     * @return HashMap对象
     * @since 3.0.4
     */
    public static <K, V> HashMap<K, V> newHashMap(final int size, final boolean isLinked) {
        final int initialCapacity = (int) (size / DEFAULT_LOAD_FACTOR) + 1;
        return isLinked ? new LinkedHashMap<>(initialCapacity) : new HashMap<>(initialCapacity);
    }

}
