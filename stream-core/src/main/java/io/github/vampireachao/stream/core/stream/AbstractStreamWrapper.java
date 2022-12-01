package io.github.vampireachao.stream.core.stream;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * {@link Stream}的包装类，用于基于一个已有的流实例进行扩展
 *
 * @author huangchengxing
 */
abstract class AbstractStreamWrapper<T, I extends Stream<T>> implements Stream<T>, Iterable<T> {

    /**
     * 原始的流实例
     */
    protected Stream<T> stream;

    /**
     * 创建一个流包装器
     *
     * @param stream 包装的流对象
     */
    protected AbstractStreamWrapper(Stream<T> stream) {
        Objects.requireNonNull(stream, "stream must not null");
        this.stream = stream;
    }


    /**
     * <p>
     * 过滤元素，返回与指定断言匹配的元素组成的流
     * 这是一个无状态中间操作
     *
     * @param predicate
     * @return 实现类
     */
    @Override
    public I filter(Predicate<? super T> predicate) {
        return wrap(stream.filter(predicate));
    }

    /**
     * <p>
     * 和{@link Stream#map(Function)}一样，只不过函数的返回值必须为int类型
     * 这是一个无状态中间操作
     *
     * @param mapper
     * @return {@link IntStream}
     */
    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return stream.mapToInt(mapper);
    }

    /**
     * <p>
     * 和{@link Stream#map(Function)}一样，只不过函数的返回值必须为long类型
     * 这是一个无状态中间操作
     *
     * @param mapper
     * @return {@link LongStream}
     */
    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return stream.mapToLong(mapper);
    }

    /**
     * <p>
     * 和{@link Stream#map(Function)}一样，只不过函数的返回值必须为double类型
     * 这是一个无状态中间操作
     *
     * @param mapper
     * @return {@link DoubleStream}
     */
    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return stream.mapToDouble(mapper);
    }

    /**
     * <p>
     * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
     * 这是一个无状态中间操作
     *
     * @param mapper
     * @return {@link IntStream}
     */
    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return stream.flatMapToInt(mapper);
    }

    /**
     * <p>
     * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
     * 这是一个无状态中间操作
     *
     * @param mapper
     * @return {@link LongStream}
     */
    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return stream.flatMapToLong(mapper);
    }

    /**
     * <p>
     * 扩散流操作，可能影响流元素个数，将原有流元素执行mapper操作，返回多个流所有元素组成的流
     * 这是一个无状态中间操作
     *
     * @param mapper
     * @return {@link DoubleStream}
     */
    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return stream.flatMapToDouble(mapper);
    }

    /**
     * <p>
     * 返回一个具有去重特征的流 非并行流(顺序流)下对于重复元素，保留遇到顺序中最先出现的元素，并行流情况下不能保证具体保留哪一个
     * 这是一个有状态中间操作
     *
     * @return 实现类
     */
    @Override
    public I distinct() {
        return wrap(stream.distinct());
    }

    /**
     * <p>
     * 返回一个元素按自然顺序排序的流
     * 如果此流的元素不是{@code Comparable} ，则在执行终端操作时可能会抛出 {@code java.lang.ClassCastException}
     * 对于顺序流，排序是稳定的。 对于无序流，没有稳定性保证。
     * 这是一个有状态中间操作
     *
     * @return 实现类
     */
    @Override
    public I sorted() {
        return wrap(stream.sorted());
    }

    /**
     * <p>
     * 返回一个元素按指定的{@link Comparator}排序的流
     * 如果此流的元素不是{@code Comparable} ，则在执行终端操作时可能会抛出{@code java.lang.ClassCastException}
     * 对于顺序流，排序是稳定的。 对于无序流，没有稳定性保证。
     * 这是一个有状态中间操作
     *
     * @param comparator
     * @return 实现类
     */
    @Override
    public I sorted(Comparator<? super T> comparator) {
        return wrap(stream.sorted(comparator));
    }

    /**
     * <p>
     * 返回与指定函数将元素作为参数执行后组成的流。
     * 这是一个无状态中间操作
     *
     * @param action
     * @return 实现类
     * @apiNote 该方法存在的意义主要是用来调试
     * 当你需要查看经过操作管道某处的元素，可以执行以下操作:
     * <pre>{@code
     *     .of("one", "two", "three", "four")
     *         .filter(e -> e.length() > 3)
     *         .peek(e -> System.out.println("Filtered value: " + e))
     *         .map(String::toUpperCase)
     *         .peek(e -> System.out.println("Mapped value: " + e))
     *         .collect(Collectors.toList());
     * }</pre>
     */
    @Override
    public I peek(Consumer<? super T> action) {
        return wrap(stream.peek(action));
    }

    /**
     * <p>
     * 返回截取后面一些元素的流
     * 这是一个短路状态中间操作
     *
     * @param maxSize
     * @return 实现类
     */
    @Override
    public I limit(long maxSize) {
        return wrap(stream.limit(maxSize));
    }

    /**
     * <p>
     * 返回丢弃前面n个元素后的剩余元素组成的流，如果当前元素个数小于n，则返回一个元素为空的流
     * 这是一个有状态中间操作
     *
     * @param n
     * @return 实现类
     */
    @Override
    public I skip(long n) {
        return wrap(stream.skip(n));
    }

    /**
     * <p>
     * 对流里面的每一个元素执行一个操作
     * 这是一个终端操作
     *
     * @param action
     */
    @Override
    public void forEach(Consumer<? super T> action) {
        stream.forEach(action);
    }

    /**
     * <p>
     * 对流里面的每一个元素按照顺序执行一个操作
     * 这是一个终端操作
     *
     * @param action
     */
    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        stream.forEachOrdered(action);
    }

    /**
     * <p>
     * 返回一个包含此流元素的数组
     * 这是一个终端操作
     *
     * @return {@link Object}{@code []}
     */
    @Override
    public Object[] toArray() {
        return stream.toArray();
    }

    /**
     * <p>
     * 返回一个包含此流元素的指定的数组，例如以下代码编译正常，但运行时会抛出 {@link ArrayStoreException}
     * <pre>{@code String[] strings = Stream.<Integer>builder().add(1).build().toArray(String[]::new); }</pre>
     *
     * @param generator
     * @return 包含此流元素的指定的数组 {@code  A[]}
     */
    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return stream.toArray(generator);
    }

    /**
     * <p>
     * 对元素进行聚合，并返回聚合后的值，相当于在for循环里写sum=sum+ints[i]
     * 这是一个终端操作<br>
     * 求和、最小值、最大值、平均值和转换成一个String字符串均为聚合操作
     * 例如这里对int进行求和可以写成：
     *
     * <pre>{@code
     *     Integer sum = integers.reduce(0, (a, b) -> a+b);
     * }</pre>
     * <p>
     * 或者写成:
     *
     * <pre>{@code
     *     Integer sum = integers.reduce(0, Integer::sum);
     * }</pre>
     *
     * @param identity
     * @param accumulator
     * @return 聚合的结果
     */
    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return stream.reduce(identity, accumulator);
    }

    /**
     * <p>
     * 对元素进行聚合，并返回聚合后用 {@link Optional}包裹的值，相当于在for循环里写sum=sum+ints[i]
     * 该操作相当于：
     * <pre>{@code
     *     boolean foundAny = false;
     *     T result = null;
     *     for (T element : this stream) {
     *         if (!foundAny) {
     *             foundAny = true;
     *             result = element;
     *         }
     *         else
     *             result = accumulator.apply(result, element);
     *     }
     *     return foundAny ? Optional.of(result) : Optional.empty();
     * }</pre>
     * 但它不局限于顺序执行，例如并行流等情况下
     * 这是一个终端操作<br>
     * 例如以下场景抛出 NPE ：
     * <pre>{@code
     *      Optional<Integer> reduce = Stream.<Integer>builder().add(1).add(1).build().reduce((a, b) -> null);
     * }</pre>
     *
     * @param accumulator
     * @return {@link Optional}<{@code  T}>
     * @see #reduce(Object, BinaryOperator)
     * @see #min(Comparator)
     * @see #max(Comparator)
     */
    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return stream.reduce(accumulator);
    }

    /**
     * <p>
     * 对元素进行聚合，并返回聚合后的值，并行流时聚合拿到的初始值不稳定
     * 这是一个终端操作
     *
     * @param identity
     * @param accumulator
     * @param combiner
     * @return 聚合后的值
     * @see #reduce(BinaryOperator)
     * @see #reduce(Object, BinaryOperator)
     */
    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return stream.reduce(identity, accumulator, combiner);
    }

    /**
     * <p>
     * 对元素进行收集，并返回收集后的容器
     * 这是一个终端操作
     *
     * @param supplier
     * @param accumulator
     * @param combiner
     * @return 收集后的容器
     */
    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return stream.collect(supplier, accumulator, combiner);
    }

    /**
     * <p>
     * 对元素进行收集，并返回收集后的元素
     * 这是一个终端操作
     *
     * @param collector
     * @return 集后的元素
     */
    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return stream.collect(collector);
    }

    /**
     * <p>
     * 获取最小值
     *
     * @param comparator
     * @return {@link Optional}<{@code  T}>
     */
    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return stream.min(comparator);
    }

    /**
     * <p>
     * 获取最大值
     *
     * @param comparator
     * @return {@link Optional}<{@code  T}>
     */
    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return stream.max(comparator);
    }

    /**
     * <p>
     * 返回流元素个数
     *
     * @return long
     */
    @Override
    public long count() {
        return stream.count();
    }

    /**
     * <p>
     * 判断是否有任何一个元素满足给定断言
     *
     * @param predicate
     * @return boolean
     */
    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return stream.anyMatch(predicate);
    }

    /**
     * <p>
     * 判断是否所有元素满足给定断言
     *
     * @param predicate
     * @return boolean
     */
    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return stream.allMatch(predicate);
    }

    /**
     * <p>
     * 判断是否没有元素满足给定断言
     *
     * @param predicate
     * @return boolean
     */
    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return stream.noneMatch(predicate);
    }

    /**
     * <p>
     * 获取第一个元素
     *
     * @return {@link Optional}<{@code  T}>
     */
    @Override
    public Optional<T> findFirst() {
        return stream.findFirst();
    }

    /**
     * <p>
     * 考虑性能，随便取一个，这里不是随机取一个，是随便取一个
     *
     * @return {@link Optional}<{@code  T}>
     */
    @Override
    public Optional<T> findAny() {
        return stream.findAny();
    }

    /**
     * <p>
     * 返回流的迭代器
     *
     * @return {@link Iterator}<{@code  T}>
     */
    @Override
    public Iterator<T> iterator() {
        return stream.iterator();
    }

    /**
     * <p>
     * 返回流的拆分器
     *
     * @return {@link Spliterator}<{@code  T}>
     */
    @Override
    public Spliterator<T> spliterator() {
        return stream.spliterator();
    }

    /**
     * <p>
     * 返回流的并行状态
     *
     * @return boolean
     */
    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }

    /**
     * <p>
     * 返回一个串行流，该方法可以将并行流转换为串行流
     *
     * @return 实现类
     */
    @Override
    public I sequential() {
        return wrap(stream.sequential());
    }

    /**
     * <p>
     * 将流转换为并行
     *
     * @return 实现类
     */
    @Override
    public I parallel() {
        return wrap(stream.parallel());
    }

    /**
     * <p>
     * 返回一个无序流(无手动排序)
     * <p>标记一个流是不在意元素顺序的, 在并行流的某些情况下可以提高性能</p>
     *
     * @return 实现类
     */
    @Override
    public I unordered() {
        return wrap(stream.unordered());
    }

    /**
     * <p>
     * 在流关闭时执行操作
     *
     * @param closeHandler
     * @return 实现类
     */
    @Override
    public I onClose(Runnable closeHandler) {
        return wrap(stream.onClose(closeHandler));
    }

    /**
     * <p>
     * 关闭流
     *
     * @see AutoCloseable#close()
     */
    @Override
    public void close() {
        stream.close();
    }

    /**
     * 根据一个原始的流，返回一个新包装类实例
     *
     * @param stream 流
     * @return 实现类
     */
    protected abstract I wrap(Stream<T> stream);

}
