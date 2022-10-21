package io.github.vampireachao.stream.core.collection;

import io.github.vampireachao.stream.core.stream.Steam;
import io.github.vampireachao.stream.core.variable.VariableHelper;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;

/**
 * @author VampireAchao
 * @since 2022/10/21 15:48
 */
public class MapHelper {

    public static <K, V> Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    @SafeVarargs
    public static <K, A, V> Steam<Entry<K, List<V>>>
    oneToManyToOne(Map<K, List<A>> middleMap, Map<A, V> attachMap, UnaryOperator<Steam<V>>... unaryOperator) {
        return Steam.of(middleMap.entrySet()).map(e -> entry(e.getKey(),
                VariableHelper.<UnaryOperator<Steam<V>>>first(unaryOperator, UnaryOperator::identity)
                        .apply(Steam.of(e.getValue()).map(attachMap::get)).toList()));
    }

    @SafeVarargs
    public static <K, A, V> Steam<Entry<K, V>>
    oneToOneToOne(Map<K, A> middleMap, Map<A, V> attachMap, UnaryOperator<V>... unaryOperator) {
        return Steam.of(middleMap.entrySet()).map(e -> entry(e.getKey(),
                VariableHelper.<UnaryOperator<V>>first(unaryOperator, UnaryOperator::identity)
                        .apply(attachMap.get(e.getValue()))));
    }

    @SafeVarargs
    public static <K, A, V> Steam<Entry<K, List<V>>>
    oneToOneToMany(Map<K, A> middleMap, Map<A, List<V>> attachMap, UnaryOperator<Steam<V>>... unaryOperator) {
        return Steam.of(middleMap.entrySet()).map(e -> entry(e.getKey(),
                VariableHelper.<UnaryOperator<Steam<V>>>first(unaryOperator, UnaryOperator::identity)
                        .apply(Steam.of(e.getValue()).flat(attachMap::get)).toList()));
    }
}
