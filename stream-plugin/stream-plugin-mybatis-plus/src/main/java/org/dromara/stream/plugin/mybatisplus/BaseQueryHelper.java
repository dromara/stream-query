package org.dromara.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.dromara.stream.core.lambda.function.SerCons;
import org.dromara.stream.core.lambda.function.SerFunc;
import org.dromara.stream.core.optional.Sf;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.UnaryOperator;


/**
 * 公共方法抽象
 *
 * @param <S> value改变后的返回值
 * @param <T> 实体类型
 * @param <K> key值类型
 * @param <V> value或后续操作返回值类型
 * @author VampireAchao
 * @since 2022/9/16 16:30
 */
@SuppressWarnings("unchecked")
public abstract class BaseQueryHelper<
        S extends BaseQueryHelper<S, T, K, V>,
        T,
        K extends Serializable & Comparable<? super K>,
        V
        > extends BaseQuery<T, K, V> {

    /**
     * <p>Constructor for BaseQueryHelper.</p>
     *
     * @param keyFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     */
    protected BaseQueryHelper(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    /**
     * <p>eq.</p>
     *
     * @param data a K object
     * @return a S object
     */
    public S eq(K data) {
        wrapper = Sf.of(data).mayLet(value -> wrapper.eq(keyFunction, value)).orGet(() -> Database.notActive(wrapper));
        return (S) this;
    }

    /**
     * <p>in.</p>
     *
     * @param dataList a {@link java.util.Collection} object
     * @return a S object
     */
    public S in(Collection<K> dataList) {
        wrapper = Sf.mayColl(dataList).mayLet(HashSet::new).mayLet(values -> wrapper.in(keyFunction, values)).orGet(() -> Database.notActive(wrapper));
        return (S) this;
    }

    /**
     * <p>like.</p>
     *
     * @param data a {@link java.lang.String} object
     * @return a S object
     */
    public S like(String data) {
        wrapper = Sf.ofStr(data).mayLet(value -> wrapper.like(keyFunction, value)).orGet(() -> Database.notActive(wrapper));
        return (S) this;
    }

    /**
     * <p>condition.</p>
     *
     * @param queryOperator a {@link java.util.function.UnaryOperator} object
     * @return a S object
     */
    public S condition(UnaryOperator<LambdaQueryWrapper<T>> queryOperator) {
        wrapper = Sf.of(queryOperator.apply(wrapper)).orGet(() -> Database.notActive(wrapper));
        return (S) this;
    }

    /**
     * <p>parallel.</p>
     *
     * @param isParallel a boolean
     * @return a S object
     */
    public S parallel(boolean isParallel) {
        this.isParallel = isParallel;
        return (S) this;
    }

    /**
     * <p>parallel.</p>
     *
     * @return a S object
     */
    public S parallel() {
        return parallel(true);
    }

    /**
     * <p>sequential.</p>
     *
     * @return a S object
     */
    public S sequential() {
        return parallel(false);
    }

    /**
     * <p>peek.</p>
     *
     * @param peek a {@link SerCons} object
     * @return a S object
     */
    public S peek(SerCons<T> peek) {
        this.peekConsumer = this.peekConsumer.andThen(peek);
        return (S) this;
    }
    /**
     * <p>attachSingle.</p>
     *
     * @param valueFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <R>           a R class
     */
    protected <R> void attachSingle(SFunction<T, R> valueFunction) {
        this.valueFunction = (SFunction<T, V>) valueFunction;
        Database.select(wrapper, (w, col) -> w.select(col[1]), keyFunction, valueFunction);
    }

    /**
     * <p>attachDouble.</p>
     *
     * @param valueFunction a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param <R>           a R class
     */
    protected <R> void attachDouble(SFunction<T, R> valueFunction) {
        this.valueFunction = (SFunction<T, V>) valueFunction;
        Database.select(wrapper, keyFunction, valueFunction);
    }

    /**
     * <p>valueOrIdentity.</p>
     *
     * @return a {@link SerFunc} object
     */
    protected SerFunc<T, V> valueOrIdentity() {
        return t -> Sf.of(valueFunction).orGet(() -> SerFunc.<T, V>cast()::apply).apply(t);
    }

}
