package io.github.vampireachao.stream.core.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VampireAchao
 * @since 2022/10/21 16:35
 */
public class Maps {

    private Maps() {
        /* Do not new me! */
    }

    public static <K, V> Map<K, V> of(int initialCapacity) {
        return new HashMap<>(initialCapacity);
    }

    public static <K, V> Map<K, V> of(K k, V v) {
        final Map<K, V> map = of(1);
        map.put(k, v);
        return map;
    }

    public static <K, V> Map<K, V> of(K k, V v, K k1, V v1) {
        final Map<K, V> map = of(1 << 1);
        map.put(k, v);
        map.put(k1, v1);
        return map;
    }

    public static <K, V> Map<K, V> of(K k, V v, K k1, V v1, K k2, V v2) {
        final Map<K, V> map = of(1 << 2);
        map.put(k, v);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> of(K k, V v, K k1, V v1, K k2, V v2, K k3, V v3) {
        final Map<K, V> map = of(1 << 2);
        map.put(k, v);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> Map<K, V> of(K k, V v, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        final Map<K, V> map = of(1 << 3);
        map.put(k, v);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }


}
