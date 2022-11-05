package io.github.vampireachao.stream.core.collection;

import io.github.vampireachao.stream.core.stream.Steam;
import io.github.vampireachao.stream.core.variable.VariableHelper;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

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

    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    @SafeVarargs
    public static <K, A, V> Steam<Map.Entry<K, List<V>>>
    oneToManyToOne(Map<K, List<A>> middleMap, Map<A, V> attachMap, UnaryOperator<Steam<V>>... unaryOperator) {
        return Steam.of(middleMap.entrySet()).map(e -> entry(e.getKey(),
                VariableHelper.<UnaryOperator<Steam<V>>>first(unaryOperator, UnaryOperator::identity)
                        .apply(Steam.of(e.getValue()).map(attachMap::get)).toList()));
    }

    @SafeVarargs
    public static <K, A, V> Steam<Map.Entry<K, V>>
    oneToOneToOne(Map<K, A> middleMap, Map<A, V> attachMap, UnaryOperator<V>... unaryOperator) {
        return Steam.of(middleMap.entrySet()).map(e -> entry(e.getKey(),
                VariableHelper.<UnaryOperator<V>>first(unaryOperator, UnaryOperator::identity)
                        .apply(attachMap.get(e.getValue()))));
    }

    @SafeVarargs
    public static <K, A, V> Steam<Map.Entry<K, List<V>>>
    oneToOneToMany(Map<K, A> middleMap, Map<A, List<V>> attachMap, UnaryOperator<Steam<V>>... unaryOperator) {
        return Steam.of(middleMap.entrySet()).map(e -> entry(e.getKey(),
                VariableHelper.<UnaryOperator<Steam<V>>>first(unaryOperator, UnaryOperator::identity)
                        .apply(Steam.of(e.getValue()).flat(attachMap::get)).toList()));
    }

}
