package io.github.vampireachao.stream.core.stream;

import io.github.vampireachao.stream.core.collector.Collective;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>针对键值对对象{@link java.util.Map.Entry}特化的{@link java.util.stream.Stream}包装类，
 * 本身可视为一个元素类型为{@link java.util.Map.Entry}的流。<br>
 * 用于支持流式处理{@link java.util.Map}集合中的、或具有潜在可能转为{@link java.util.Map}集合的数据。
 *
 * <p>可以通过静态方法基于{@link java.lang.Iterable}、{@link java.util.Collection}或者{@link java.util.Map}获得一个键值对流，
 * 或在原生{@link java.util.stream.Stream#collect(Collector)}通过{@link io.github.vampireachao.stream.core.collector.Collective#toEntrySteam(Function, Function)}获得。
 * TODO 为当前及后续可能存在的扩展类提取公共增强流接口&抽象父类
 *
 * @author huangchengxing
 */
public class EntrySteam<K, V> extends AbstractStreamWrapper<Map.Entry<K, V>, EntrySteam<K, V>> {

    /**
     * 构造
     */
    EntrySteam(Stream<Map.Entry<K, V>> stream) {
        super(stream);
    }

    /**
     * 根据一个{@link java.util.Map}集合中的键值对创建一个串行流，
     * 对流的操作不会影响到入参的{@code map}实例本身
     *
     * @param map 集合
     * @param <A> 键类型
     * @param <B> 值类型
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public static <A, B> EntrySteam<A, B> of(Map<A, B> map) {
        return Objects.isNull(map) ?
                empty() : of(map.entrySet());
    }

    /**
     * 根据一个{@link java.util.Map.Entry}类型的{@link java.lang.Iterable}创建一个串行流，
     * 对流的操作不会影响到入参的{@code entries}实例本身
     *
     * @param entries {@link java.lang.Iterable}实例
     * @param <A>     键类型
     * @param <B>     值类型
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public static <A, B> EntrySteam<A, B> of(Iterable<? extends Map.Entry<A, B>> entries) {
        return Objects.isNull(entries) ?
                empty() : of(entries, Map.Entry::getKey, Map.Entry::getValue);
    }

    /**
     * 根据一个{@link java.util.Collection}集合中创建一个串行流
     *
     * @param source      原始集合
     * @param keyMapper   键的映射方法
     * @param valueMapper 值的映射方法
     * @param <A>         键类型
     * @param <B>         值类型
     * @param <T>         a T class
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public static <T, A, B> EntrySteam<A, B> of(Iterable<T> source, Function<T, A> keyMapper, Function<T, B> valueMapper) {
        Objects.requireNonNull(keyMapper);
        Objects.requireNonNull(valueMapper);
        if (Objects.isNull(source)) {
            return empty();
        }
        final Stream<Map.Entry<A, B>> stream = StreamSupport.stream(source.spliterator(), false)
                .map(t -> new Entry<>(keyMapper.apply(t), valueMapper.apply(t)));
        return of(stream);
    }

    /**
     * 包装一个已有的流，若入参为空则返回一个空的串行流
     *
     * @param stream 流
     * @param <A>    键类型
     * @param <B>    值类型
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public static <A, B> EntrySteam<A, B> of(Stream<? extends Map.Entry<A, B>> stream) {
        return Objects.isNull(stream) ?
                empty() : new EntrySteam<>(stream.map(e -> new Entry<>(e.getKey(), e.getValue())));
    }

    /**
     * 创建一个空的串行流
     *
     * @param <A> 键类型
     * @param <B> 值类型
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public static <A, B> EntrySteam<A, B> empty() {
        return new EntrySteam<>(Stream.empty());
    }

    // ================================ override ================================

    /**
     * key重复时直接抛出异常
     */
    private static <T> BinaryOperator<T> throwingMerger() {
        return (u, v) -> {throw new IllegalStateException(String.format("Duplicate key %s", u));};
    }

    // ================================ 中间操作 ================================

    /**
     * {@inheritDoc}
     * <p>
     * 根据一个原始的流，返回一个新包装类实例
     */
    @Override
    protected EntrySteam<K, V> convertToStreamImpl(Stream<Map.Entry<K, V>> stream) {
        return new EntrySteam<>(stream);
    }

    /**
     * 根据键去重，默认丢弃靠后的
     *
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<K, V> distinctByKey() {
        Set<K> accessed = new HashSet<>(16);
        return new EntrySteam<>(stream.filter(e -> {
            K key = e.getKey();
            if (accessed.contains(key)) {
                return false;
            }
            accessed.add(key);
            return true;
        }));
    }

    /**
     * 根据值去重，默认丢弃靠后的
     *
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<K, V> distinctByValue() {
        Set<V> accessed = new HashSet<>(16);
        return new EntrySteam<>(stream.filter(e -> {
            V val = e.getValue();
            if (accessed.contains(val)) {
                return false;
            }
            accessed.add(val);
            return true;
        }));
    }

    /**
     * 根据键和值过滤键值对
     *
     * @param filter 判断条件
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<K, V> filter(BiPredicate<K, V> filter) {
        Objects.requireNonNull(filter);
        return super.filter(e -> filter.test(e.getKey(), e.getValue()));
    }

    /**
     * 根据键过滤键值对
     *
     * @param filter 判断条件
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<K, V> filterByKey(Predicate<K> filter) {
        Objects.requireNonNull(filter);
        return super.filter(e -> filter.test(e.getKey()));
    }

    /**
     * 根据值过滤键值对
     *
     * @param filter 判断条件
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<K, V> filterByValue(Predicate<? super V> filter) {
        Objects.requireNonNull(filter);
        return super.filter(e -> filter.test(e.getValue()));
    }

    /**
     * 合并具有相同键的值
     *
     * @param valueFolder 值的合并操作
     * @param <N>         合并后值的类型
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public <N> EntrySteam<K, N> foldByKey(BiFunction<K, Collection<V>, N> valueFolder) {
        Map<K, List<Map.Entry<K, V>>> grouped = super.collect(Collectors.groupingBy(Map.Entry::getKey));
        Map<K, N> newMap = new LinkedHashMap<>(grouped.size());
        grouped.forEach((k, es) -> {
            Collection<V> values = es.stream().map(Map.Entry::getValue).collect(Collectors.toList());
            newMap.put(k, valueFolder.apply(k, values));
        });
        return of(newMap);
    }

    /**
     * 合并具有相同键的值
     *
     * @param valueFolder 值的合并操作
     * @param <N>         合并后值的类型
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public <N> EntrySteam<K, N> foldByKey(Function<Collection<V>, N> valueFolder) {
        return foldByKey((k, es) -> valueFolder.apply(es));
    }

    /**
     * 检查键
     *
     * @param consumer 操作
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<K, V> peekKey(Consumer<K> consumer) {
        Objects.requireNonNull(consumer);
        return super.peek(e -> consumer.accept(e.getKey()));
    }

    /**
     * 检查值
     *
     * @param consumer 操作
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<K, V> peekValue(Consumer<V> consumer) {
        Objects.requireNonNull(consumer);
        return super.peek(e -> consumer.accept(e.getValue()));
    }

    /**
     * 根据键排序
     *
     * @param comparator 排序器
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<K, V> sortByKey(Comparator<K> comparator) {
        Objects.requireNonNull(comparator);
        return sorted(Map.Entry.comparingByKey(comparator));
    }

    // ================================ 转换操作 ================================

    /**
     * 根据值排序
     *
     * @param comparator 排序器
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<K, V> sortByValue(Comparator<V> comparator) {
        Objects.requireNonNull(comparator);
        return sorted(Map.Entry.comparingByValue(comparator));
    }

    /**
     * 转为值的流
     *
     * @return 值的流
     */
    public Steam<V> toValueStream() {
        return Steam.of(stream.map(Map.Entry::getValue));
    }

    /**
     * 转为键的流
     *
     * @return 值的流
     */
    public Steam<K> toKeyStream() {
        return Steam.of(stream.map(Map.Entry::getKey));
    }

    /**
     * 将键映射为另一类型
     *
     * @param mapper 映射方法
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     * @param <N> a N class
     */
    public <N> EntrySteam<N, V> mapKeys(Function<K, N> mapper) {
        return new EntrySteam<>(
                stream.map(e -> new Entry<>(mapper.apply(e.getKey()), e.getValue()))
        );
    }

    /**
     * 将值映射为另一类型
     *
     * @param mapper 映射方法
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     * @param <N> a N class
     */
    public <N> EntrySteam<K, N> mapValues(Function<V, N> mapper) {
        Objects.requireNonNull(mapper);
        return new EntrySteam<>(
                stream.map(e -> new Entry<>(e.getKey(), mapper.apply(e.getValue())))
        );
    }

    /**
     * {@inheritDoc}
     *
     * 返回与指定函数将元素作为参数执行的结果组成的流
     * 这是一个无状态中间操作
     */
    @Override
    public <R> Steam<R> map(Function<? super Map.Entry<K, V>, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return Steam.of(stream.map(mapper));
    }

    /**
     * 将实例转为根据键值对生成的单对象{@link java.util.stream.Stream}实例
     *
     * @param mapper 映射方法
     * @return 映射后的单对象组成的流
     * @param <N> a N class
     */
    public <N> Steam<N> map(BiFunction<K, V, N> mapper) {
        Objects.requireNonNull(mapper);
        return Steam.of(stream.map(e -> mapper.apply(e.getKey(), e.getValue())));
    }

    /**
     * {@inheritDoc}
     *
     * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流<br>
     * 这是一个无状态中间操作<br>
     * 例如，将users里所有user的id和parentId组合在一起，形成一个新的流:
     * <pre>{@code
     *     FastStream<Long> ids = FastStream.of(users).flatMap(user -> FastStream.of(user.getId(), user.getParentId()));
     * }</pre>
     */
    @Override
    public <R> Steam<R> flatMap(Function<? super Map.Entry<K, V>, ? extends Stream<? extends R>> mapper) {
        Objects.requireNonNull(mapper);
        return Steam.of(stream.flatMap(mapper));
    }

    /**
     * <p>将原有流的键执行mapper操作映射为流，流中的所有所有元素仍然对应原本的值，
     * 然后再返回由这些流中所有元素组成的流新{@link io.github.vampireachao.stream.core.stream.EntrySteam}串行流。<br>
     * 效果类似：
     * <pre>{@code
     * // stream = [{a = 1}, {b = 2}, {c = 3}]
     * stream.flatMapKey(key -> Stream.of(key + "1", key + "2"));
     * // stream = [{a1 = 1}, {a2 = 1}, {b1 = 2}, {b2 = 2}, {c1 = 3}, {c2 = 3}]
     * }</pre>
     *
     * @param keyMapper 值转映射方法
     * @param <N>       新的键类型
     * @return 返回叠加拆分操作后的流
     */
    public <N> EntrySteam<N, V> flatMapKey(Function<K, Stream<N>> keyMapper) {
        Objects.requireNonNull(keyMapper);
        return new EntrySteam<>(
                stream.flatMap(e -> keyMapper
                        .apply(e.getKey())
                        .map(newKey -> new Entry<>(newKey, e.getValue()))
                )
        );
    }

    // ================================ 结束操作 ================================

    /**
     * <p>将原有流的值执行mapper操作映射为流，流中的所有所有元素仍然对应原本的键，
     * 然后再返回由这些流中所有元素组成的流新{@link io.github.vampireachao.stream.core.stream.EntrySteam}串行流。<br>
     * 效果类似：
     * <pre>{@code
     * // stream = [{a = 1}, {b = 2}, {c = 3}]
     * stream.flatMapValue(num -> Stream.of(num, num+1));
     * // stream = [{a = 1}, {a = 2}, {b = 2}, {b = 3}, {c = 3}, {c = 4}]
     * }</pre>
     *
     * @param valueMapper 值转映射方法
     * @param <N>         新的值类型
     * @return 返回叠加拆分操作后的流
     */
    public <N> EntrySteam<K, N> flatMapValue(Function<V, Stream<N>> valueMapper) {
        Objects.requireNonNull(valueMapper);
        return new EntrySteam<>(
                stream.flatMap(e -> valueMapper
                        .apply(e.getValue())
                        .map(newVal -> new Entry<>(e.getKey(), newVal))
                )
        );
    }

    /**
     * 转为{@link java.util.Map}集合
     *
     * @param mapFactory 获取集合的工厂方法
     * @param operator   当存在重复键时的处理
     * @return 集合
     * @see Collectors#toMap(Function, Function, BinaryOperator, Supplier)
     */
    public Map<K, V> toMap(Supplier<Map<K, V>> mapFactory, BinaryOperator<V> operator) {
        Objects.requireNonNull(mapFactory);
        Objects.requireNonNull(operator);
        return super.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, operator, mapFactory));
    }

    /**
     * 转为{@link java.util.Map}集合
     *
     * @param mapFactory 获取集合的工厂方法
     * @return 集合
     * @see Collectors#toMap(Function, Function, BinaryOperator)
     */
    public Map<K, V> toMap(Supplier<Map<K, V>> mapFactory) {
        Objects.requireNonNull(mapFactory);
        return super.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (t1, t2) -> t2, mapFactory));
    }

    /**
     * 转为{@link java.util.HashMap}集合
     *
     * @return 集合
     * @throws java.lang.IllegalArgumentException 当键重复时抛出
     * @see Collectors#toMap(Function, Function)
     */
    public Map<K, V> toMap() {
        return super.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 将键值对分组后再转为二维{@link java.util.Map}集合
     *
     * @param rowKeyMapper  将键映射为父集合中键的方法
     * @param colMapFactory 创建子集合的工厂方法
     * @param operator      当存在重复键时的处理
     * @return 集合
     * @see Collectors#groupingBy(Function, Supplier, Collector)
     * @param <N> a N class
     */
    public <N> Map<N, Map<K, V>> toTable(
            BiFunction<K, V, N> rowKeyMapper, Supplier<Map<K, V>> colMapFactory, BinaryOperator<V> operator) {
        Objects.requireNonNull(rowKeyMapper);
        Objects.requireNonNull(colMapFactory);
        Objects.requireNonNull(operator);
        return collect(Collectors.groupingBy(
                e -> rowKeyMapper.apply(e.getKey(), e.getValue()),
                HashMap::new,
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, operator, colMapFactory)
        ));
    }

    /**
     * 将键值对分组后再转为二维{@link java.util.HashMap}集合
     *
     * @param rowKeyMapper 创建父集合的工厂方法
     * @return 集合
     * @throws java.lang.IllegalArgumentException 当父集合或子集合中的键重复时抛出
     * @param <N> a N class
     */
    public <N> Map<N, Map<K, V>> toTable(BiFunction<K, V, N> rowKeyMapper) {
        return toTable(rowKeyMapper, HashMap::new, throwingMerger());
    }

    /**
     * 将键值对按值分组后再转为二维{@link java.util.Map}集合
     *
     * @param rowKeyMapper  将键映射为父集合中键的方法
     * @param colMapFactory 创建子集合的工厂方法
     * @param operator      当存在重复键时的处理
     * @return 集合
     * @param <N> a N class
     */
    public <N> Map<N, Map<K, V>> toTableByKey(
            Function<K, N> rowKeyMapper, Supplier<Map<K, V>> colMapFactory, BinaryOperator<V> operator) {
        return toTable((k, v) -> rowKeyMapper.apply(k), colMapFactory, operator);
    }

    /**
     * 将键值对按键分组后再转为二维{@link java.util.HashMap}集合
     *
     * @param rowKeyMapper 创建父集合的工厂方法
     * @return 集合
     * @throws java.lang.IllegalArgumentException 当父集合或子集合中的键重复时抛出
     * @param <N> a N class
     */
    public <N> Map<N, Map<K, V>> toTableByKey(Function<K, N> rowKeyMapper) {
        return toTable((k, v) -> rowKeyMapper.apply(k));
    }

    /**
     * 将键值对按值分组后再转为二维{@link java.util.Map}集合
     *
     * @param rowKeyMapper  将键映射为父集合中键的方法
     * @param colMapFactory 创建子集合的工厂方法
     * @param operator      当存在重复键时的处理
     * @return 集合
     * @param <N> a N class
     */
    public <N> Map<N, Map<K, V>> toTableByValue(
            Function<V, N> rowKeyMapper, Supplier<Map<K, V>> colMapFactory, BinaryOperator<V> operator) {
        return toTable((k, v) -> rowKeyMapper.apply(v), colMapFactory, operator);
    }

    /**
     * 将键值对按键分组后再转为二维{@link java.util.HashMap}集合
     *
     * @param rowKeyMapper 创建父集合的工厂方法
     * @return 集合
     * @throws java.lang.IllegalArgumentException 当父集合或子集合中的键重复时抛出
     * @param <N> a N class
     */
    public <N> Map<N, Map<K, V>> toTableByValue(Function<V, N> rowKeyMapper) {
        return toTable((k, v) -> rowKeyMapper.apply(v));
    }

    /**
     * 将键值对按键分组
     *
     * @return 集合
     */
    public Map<K, List<V>> groupByKey() {
        return groupByKey(Collectors.toList());
    }

    /**
     * 将键值对按键分组
     *
     * @param collector 对具有相同键的值的收集器
     * @param <C>       值集合的类型
     * @return 集合
     */
    public <C extends Collection<V>> Map<K, C> groupByKey(Collector<V, ?, C> collector) {
        return groupByKey((Supplier<Map<K, C>>) HashMap::new, collector);
    }

    /**
     * 将键值对按键分组
     *
     * @param mapFactory 创建map集合的工厂方法
     * @param collector  对具有相同键的值的收集器
     * @param <C>        值集合的类型
     * @param <M>        返回的map集合类型
     * @return 集合
     */
    public <C extends Collection<V>, M extends Map<K, C>> M groupByKey(Supplier<M> mapFactory, Collector<V, ?, C> collector) {
        return super.collect(Collectors.groupingBy(
                Map.Entry::getKey, mapFactory,
                Collective.transform(ArrayList::new, s -> s.stream().map(Map.Entry::getValue).collect(collector))
        ));
    }

    /**
     * 遍历键值对
     *
     * @param consumer 操作
     */
    public void forEach(BiConsumer<K, V> consumer) {
        Objects.requireNonNull(consumer);
        super.forEach(e -> consumer.accept(e.getKey(), e.getValue()));
    }

    /**
     * 遍历值
     *
     * @param consumer 操作
     */
    public void forEachValues(Consumer<V> consumer) {
        super.forEach(e -> consumer.accept(e.getValue()));
    }

    /**
     * 遍历键
     *
     * @param consumer 操作
     */
    public void forEachKeys(Consumer<K> consumer) {
        super.forEach(e -> consumer.accept(e.getKey()));
    }

    /**
     * 将键值对翻转
     *
     * @return {@link io.github.vampireachao.stream.core.stream.EntrySteam}实例
     */
    public EntrySteam<V, K> inverse() {
        return new EntrySteam<>(
                stream.map(e -> new Entry<>(e.getValue(), e.getKey()))
        );
    }

    /**
     * 收集键
     *
     * @param collector 收集器
     * @return 收集容器
     * @param <R> a R class
     */
    public <R> R collectKeys(Collector<K, ?, R> collector) {
        return toKeyStream().collect(collector);
    }

    /**
     * 收集值
     *
     * @param collector 收集器
     * @return 收集容器
     * @param <R> a R class
     */
    public <R> R collectValues(Collector<V, ?, R> collector) {
        return toValueStream().collect(collector);
    }

    /**
     * {@link Map.Entry}的基本实现
     */
    static class Entry<K, V> implements Map.Entry<K, V> {

        /**
         * 键
         */
        private final K key;

        /**
         * 值
         */
        private V val;

        /**
         * 创建一个简单键值对对象
         *
         * @param key 键
         * @param val 值
         */
        public Entry(K key, V val) {
            this.key = key;
            this.val = val;
        }

        /**
         * 获取键
         *
         * @return 键
         */
        @Override
        public K getKey() {
            return key;
        }

        /**
         * 获取值
         *
         * @return 值
         */
        @Override
        public V getValue() {
            return val;
        }

        /**
         * 设置值
         *
         * @param value 值
         * @return 旧值
         */
        @Override
        public V setValue(V value) {
            V old = val;
            val = value;
            return old;
        }
    }

}
