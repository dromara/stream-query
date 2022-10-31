package io.github.vampireachao.stream.core.optional;

import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerRunn;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * scope-functions
 *
 * @author VampireAchao ZVerify
 * @see <a href="https://www.kotlincn.net/docs/reference/scope-functions.html"/>
 * @since 2022/9/15
 */
public class Sf<T> {

    /**
     * 一个空的{@code Sf}
     */
    private static final Sf<?> EMPTY = new Sf<>(null);

    /**
     * Sf中要操作的{@code value}
     */
    private final T value;

    /**
     * {@code Sf}的构造方法
     *
     * @param value Sf中要操作的数据
     */
    public Sf(T value) {
        this.value = value;
    }

    /**
     * 将参数包裹到Sf中,无论参数是否为{@code null}
     *
     * @param value 包裹的值
     * @param <T>   a T class
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> 一个包裹任意{@code value}的Sf
     */
    public static <T> Sf<T> of(T value) {
        return new Sf<>(value);
    }

    /**
     * 将参数(集合)包裹到Sf中无论是否为{@code null}
     *
     * @param value 包裹的集合
     * @param <E>   a E class
     * @param <T>   a T class
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> 一个包裹任意集合的Sf
     */
    public static <E, T extends Collection<E>> Sf<T> ofColl(T value) {
        return of(value).mayTakeIf(c -> !c.isEmpty());
    }

    /**
     * 将集合包裹到Sf中自动过滤掉元素为{@code null}的
     *
     * @param value 包裹的集合
     * @param <E>   a E class
     * @param <T>   a T class
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> 一个包裹元素不为{@code null}的任意集合的Sf
     */
    public static <E, T extends Collection<E>> Sf<T> mayColl(T value) {
        return ofColl(value).mayTakeIf(c -> Steam.of(c).anyMatch(Objects::nonNull));
    }

    /**
     * 将传入字符串包裹到Sf中无论是否为{@code null}
     *
     * @param value 包裹的字符串
     * @param <T>   a T class
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> 一个包裹任意字符串的Sf
     */
    public static <T extends CharSequence> Sf<T> ofStr(T value) {
        return of(value).mayTakeIf(c -> !c.toString().isEmpty());
    }

    /**
     * 将传入字符串包裹到Sf中如果为空字符串则过滤掉
     *
     * @param value 包裹的字符串
     * @param <T>   a T class
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> 如果空字符串则包裹的元素为{@code null},如果不为{@code null}则返回本身
     */
    public static <T extends CharSequence> Sf<T> mayStr(T value) {
        return ofStr(value).mayTakeIf(c -> Steam.split(c.toString(), "").anyMatch(e -> !" ".equals(e)));
    }

    /**
     * 返回一个空的{@code Sf}
     *
     * @param <R> a R class
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link R}>
     */
    public static <R> Sf<R> empty() {
        @SuppressWarnings("unchecked")
        Sf<R> empty = (Sf<R>) EMPTY;
        return empty;
    }

    /**
     * 判断{@code Sf}中操作的数据是否为{@code null}
     *
     * @return boolean 为{@code null}返回{@code true} 否则{@code false}
     */
    public boolean isEmpty() {
        return Objects.isNull(value);
    }

    /**
     * 判断{@code Sf}中操作的数据是否存在
     *
     * @return boolean 存在返回{@code true} 否则{@code false}
     */
    public boolean isPresent() {
        return Objects.nonNull(value);
    }

    /**
     * 拿到{@code Sf}中的数据
     *
     * @return {@link T} Sf中的数据
     */
    public T get() {
        return value;
    }

    /**
     * 对当前{@code Sf}中所包裹对象进行操作并返回值，返回值会包裹到{@code Sf}中替换之前的元素，
     * 用于操作非{@code null}，否则抛出NPE如为了保证安全操作可以使用mayLet
     *
     * @param function 执行的操作
     * @param <R>      a R class
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link R}> 如果包裹里的值存在，就执行传入的操作({@link java.util.function.Function#apply})并返回一个包裹了该操作返回值的{@code Sf}
     * @throws java.lang.NullPointerException 如果给定的操作为 {@code null}，抛出 {@code NPE}
     */
    public <R> Sf<R> let(SerFunc<T, R> function) {
        return of(function.apply(value));
    }

    /**
     * 对当前{@code Sf}中所包裹对象进行操作并返回值，返回值会包裹到{@code Sf}中替换之前的元素
     * 如果所操作对象为null则不进行操作
     *
     * @param function 值存在时执行的操作
     * @param <R>      a R class
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link R}> 如果包裹里的值存在，就执行传入的操作({@link java.util.function.Function#apply})并返回一个包裹了该操作返回值的{@code Sf},
     * 如果不存在返回一个空的{@code Sf}
     */
    public <R> Sf<R> mayLet(SerFunc<T, R> function) {
        if (isEmpty()) {
            return empty();
        }
        return let(function);
    }

    /**
     * 对当前{@code Sf}中所包裹对象进行消费操作无返回值，用于操作非{@code null}，否则抛出NPE如为了保证安全操作可以使用mayAlso
     *
     * @param consumer 执行的操作
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> {@code Sf}本身
     * @throws java.lang.NullPointerException 如果给定的操作为 {@code null}，抛出 {@code NPE}
     */
    public Sf<T> also(SerCons<T> consumer) {
        consumer.accept(value);
        return this;
    }

    /**
     * 对当前{@code Sf}中所包裹对象进行消费操作无返回值，如果所操作对象为{@code null}则不进行操作
     *
     * @param consumer 值存在时执行的操作
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> {@code Sf}本身
     */
    public Sf<T> mayAlso(SerCons<T> consumer) {
        if (isEmpty()) {
            return this;
        }
        return also(consumer);
    }

    /**
     * 对当前{@code Sf}中所包裹对象进行操作并返回一个布尔值，
     * 如果当前返回值为false则将{@code Sf}中包裹的数据置为{@code null}返回true则不变，
     * 用于操作非{@code null}，否则抛出NPE如为了保证安全操作可以使用mayTakeIf
     *
     * @param function 执行的操作
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> {@code Sf}本身
     * @throws java.lang.NullPointerException 如果给定的操作为 {@code null}，抛出 {@code NPE}
     */
    public Sf<T> takeIf(SerFunc<T, Boolean> function) {
        if (!Boolean.TRUE.equals(function.apply(value))) {
            return empty();
        }
        return this;
    }

    /**
     * 对当前{@code Sf}中所包裹对象进行操作并返回一个布尔值，
     * 如果当前返回值为false则将Sf中包裹的数据置为{@code null}返回true则不变，如果所操作对象为{@code null}则不进行操作
     *
     * @param function 值存在时执行的操作
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> {@code Sf}本身
     */
    public Sf<T> mayTakeIf(SerFunc<T, Boolean> function) {
        if (isEmpty()) {
            return this;
        }
        return takeIf(function);
    }


    /**
     * 与takeIf相反
     * 对当前{@code Sf}中所包裹对象进行操作并返回一个布尔值，
     * 如果当前返回值为true则将{@code Sf}中包裹的数据置为{@code null}返回false则不变，
     * 用于操作非{@code null}，否则抛出NPE如为了保证安全操作可以使用mayTakeUnless
     *
     * @param function 执行的操作
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> {@code Sf}本身
     */
    public Sf<T> takeUnless(SerFunc<T, Boolean> function) {
        return takeIf(v -> Boolean.FALSE.equals(function.apply(v)));
    }

    /**
     * 与mayTakeIf相反
     * 对当前{@code Sf}中所包裹对象进行操作并返回一个布尔值，
     * 如果当前返回值为true则将Sf中包裹的数据置为{@code null}返回false则不变，如果所操作对象为{@code null}则不进行操作
     *
     * @param function 值存在时执行的操作
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> {@code Sf}本身
     */
    public Sf<T> mayTakeUnless(SerFunc<T, Boolean> function) {
        if (isEmpty()) {
            return this;
        }
        return takeUnless(function);
    }

    /**
     * 对当前{@code Sf}中所包裹对象进行操作返回一个布尔值如果所操作对象不等于{@code null}并且则判断所传入操作是否返回为true，
     *
     * @param function 执行的操作
     * @return boolean 如果等于{@code null}则返回false，如果操作不为{@code null}，那么操作返回true则为true，返回false则为false
     */
    public boolean is(SerFunc<T, Boolean> function) {
        return isPresent() && Boolean.TRUE.equals(function.apply(value));
    }

    /**
     * 获取当前{@code Sf}对象如果当前{@code Sf}中所包裹元素为{@code null}则抛出指定异常，
     * 不为{@code null}则返回{@code Sf}对象（中间操作）
     *
     * @param supplier 操作
     * @param <X>      a X class
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> {@code Sf}本身
     * @throws X 如果给定的操作为 {@code null}，抛出指定异常
     */
    public <X extends Throwable> Sf<T> require(SerSupp<X> supplier) throws X {
        if (isPresent()) {
            return this;
        }
        throw supplier.get();
    }

    /**
     * 默认情况下获取当前{@code Sf}对象如果当前{@code Sf}中所包裹元素为{@code null}则抛出NoSuchElementException异常，
     *
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> {@code Sf}本身
     */
    public Sf<T> require() {
        return require(NoSuchElementException::new);
    }

    /**
     * 中间生产者操作，
     * 如果当前{@code Sf}中所包裹元素为{@code null}则拿到生产者所生产对象包裹到{@code Sf}中替换原有值，不为{@code null}则返回{@code Sf}本身
     *
     * @param supplier 操作
     * @return {@link io.github.vampireachao.stream.core.optional.Sf}<{@link T}> {@code Sf}本身
     */
    public Sf<T> or(SerSupp<Sf<T>> supplier) {
        if (isPresent()) {
            return this;
        }
        return supplier.get();
    }

    /**
     * 生产者操作，
     * 获取当前{@code Sf}中包裹对象如果当前{@code Sf}中所包裹元素为{@code null}则拿到{@code other}包裹到Sf中替换原有值，不为{@code Sf}则返回Sf所包裹对象
     *
     * @param other 产生的值
     * @return {@link T} {@code Sf}操作的数据
     */
    public T orElse(T other) {
        if (isPresent()) {
            return value;
        }
        return other;
    }

    /**
     * 生产者操作，与orElse(T other)区别为自定义产生数据的逻辑操作，返回值定制性更高
     * 获取当前{@code Sf}中包裹对象如果当前{@code Sf}中所包裹元素为{@code null}则拿到生产者所生产对象包裹到{@code Sf}中替换原有值，不为{@code Sf}则返回Sf所包裹对象
     *
     * @param supplier 生产者操作
     * @return {@link T} 生产者操作后{@code Sf}中操作的数据
     */
    public T orGet(SerSupp<T> supplier) {
        if (isPresent()) {
            return value;
        }
        return supplier.get();
    }

    /**
     * <p>orRun.</p>
     *
     * @param mapper a {@link io.github.vampireachao.stream.core.lambda.function.SerRunn} object
     * @return a T object
     */
    public T orRun(SerRunn mapper) {
        if (isEmpty()) {
            mapper.run();
        }
        return value;
    }

    /**
     * 与require(SerSupplier<X> supplier)区别为一个是终止操作一个是中间操作
     * 获取当前{@code Sf}对象如果当前{@code Sf}中所包裹元素为{@code null}则抛出指定异常，
     * 不为{@code null}则返回{@code Sf}对象所操作数据
     *
     * @param supplier 操作
     * @param <X>      a X class
     * @return {@link T} {@code Sf}对象所操作数据
     * @throws X 如果给定的操作为 {@code null}，抛出指定异常
     */
    public <X extends Throwable> T orThrow(SerSupp<X> supplier) throws X {
        if (isPresent()) {
            return value;
        }
        throw supplier.get();
    }


    /**
     * 与require()区别为一个是终止操作一个是中间操作
     * 默认情况下获取当前{@code Sf}对象中所包裹的数据
     * 如果当前{@code Sf}中所包裹元素为{@code null}则抛出NoSuchElementException异常
     *
     * @return {@link T} {@code Sf}对象中所包裹的数据
     */
    public T orThrow() {
        return orThrow(NoSuchElementException::new);
    }

}
