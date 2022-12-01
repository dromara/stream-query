package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.lambda.function.SerCons;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.Sf;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.UnaryOperator;


/**
 * 公共方法抽象
 *
 * @param <TR> 原返回值
 * @param <S>  value改变后的返回值
 * @param <T>  实体类型
 * @param <K>  key值类型
 * @param <V>  value或后续操作返回值类型
 * @author VampireAchao
 * @since 2022/9/16 16:30
 */
@SuppressWarnings("unchecked")
public abstract class BaseQueryHelper<
        S extends BaseQueryHelper<S, T, K, V>,
        T,
        K extends Serializable & Comparable<K>,
        V
        > extends BaseQuery<T, K, V> {

    protected BaseQueryHelper(SFunction<T, K> keyFunction) {
        super(keyFunction);
    }

    public S eq(K data) {
        wrapper = Sf.of(data).mayLet(value -> wrapper.eq(keyFunction, value)).orGet(() -> Database.notActive(wrapper));
        return (S) this;
    }

    public S in(Collection<K> dataList) {
        wrapper = Sf.mayColl(dataList).mayLet(HashSet::new).mayLet(values -> wrapper.in(keyFunction, values)).orGet(() -> Database.notActive(wrapper));
        return (S) this;
    }

    public S like(String data) {
        wrapper = Sf.ofStr(data).mayLet(value -> wrapper.like(keyFunction, value)).orGet(() -> Database.notActive(wrapper));
        return (S) this;
    }

    public S condition(UnaryOperator<LambdaQueryWrapper<T>> queryOperator) {
        wrapper = Sf.of(queryOperator.apply(wrapper)).orGet(() -> Database.notActive(wrapper));
        return (S) this;
    }

    public S parallel(boolean isParallel) {
        this.isParallel = isParallel;
        return (S) this;
    }

    public S parallel() {
        return parallel(true);
    }

    public S sequential() {
        return parallel(false);
    }

    public S peek(SerCons<T> peek) {
        this.peekConsumer = this.peekConsumer.andThen(peek);
        return (S) this;
    }

    protected <R> void attachSingle(SFunction<T, R> valueFunction) {
        this.valueFunction = (SFunction<T, V>) valueFunction;
        Database.select(wrapper, (w, col) -> w.select(col[1]), keyFunction, valueFunction);
    }

    protected <R> void attachDouble(SFunction<T, R> valueFunction) {
        this.valueFunction = (SFunction<T, V>) valueFunction;
        Database.select(wrapper, keyFunction, valueFunction);
    }

    protected SerFunc<T, V> valueOrIdentity() {
        return t -> Sf.of(valueFunction).orGet(() -> SerFunc.<T, V>castingIdentity()::apply).apply(t);
    }

}
