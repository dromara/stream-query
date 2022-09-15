package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.optional.CollOp;
import io.github.vampireachao.stream.core.optional.Sf;
import io.github.vampireachao.stream.core.optional.StrOp;

import java.util.Collection;
import java.util.Objects;

/**
 * <p>QueryCondition class.</p>
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/8/15 16:39
 */
public class QueryCondition<T> extends LambdaQueryWrapper<T> {


    /**
     * <p>Constructor for QueryCondition.</p>
     *
     * @param entity a T object
     */
    public QueryCondition(T entity) {
        super(Sf.of(entity).orElseThrow(() -> new IllegalArgumentException("entity can not be null")));
    }

    /**
     * <p>Constructor for QueryCondition.</p>
     *
     * @param entityClass a {@link java.lang.Class} object
     */
    public QueryCondition(Class<T> entityClass) {
        super(Sf.of(entityClass).orElseThrow(() -> new IllegalArgumentException("entityClass can not be null")));
    }

    /**
     * <p>query.</p>
     *
     * @param entity a T object
     * @param <T>    a T class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public static <T> QueryCondition<T> query(T entity) {
        return new QueryCondition<>(entity);
    }

    /**
     * <p>query.</p>
     *
     * @param entityClass a {@link java.lang.Class} object
     * @param <T>         a T class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public static <T> QueryCondition<T> query(Class<T> entityClass) {
        return new QueryCondition<>(entityClass);
    }

    /**
     * <p>eq.</p>
     *
     * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param data   a {@link java.lang.String} object
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public QueryCondition<T> eq(SFunction<T, String> column, String data) {
        super.eq(StringUtils.isNotEmpty(data), column, data);
        return this;
    }

    /**
     * <p>eq.</p>
     *
     * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param data   a R object
     * @param <R>    a R class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public <R extends Comparable<R>> QueryCondition<T> eq(SFunction<T, R> column, R data) {
        super.eq(Objects.nonNull(data), column, data);
        return this;
    }

    /**
     * <p>like.</p>
     *
     * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param data   a {@link java.lang.String} object
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public QueryCondition<T> like(SFunction<T, String> column, String data) {
        super.like(StringUtils.isNotEmpty(data), column, data);
        return this;
    }

    /**
     * <p>in.</p>
     *
     * @param column   a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param dataList a {@link java.util.Collection} object
     * @param <R>      a R class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public <R extends Comparable<R>> QueryCondition<T> in(SFunction<T, R> column, Collection<R> dataList) {
        super.in(CollectionUtils.isNotEmpty(dataList), column, dataList);
        return this;
    }


    /**
     * <p>activeEq.</p>
     *
     * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param data   a {@link java.lang.String} object
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public QueryCondition<T> activeEq(SFunction<T, String> column, String data) {
        StrOp.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
        return this;
    }

    /**
     * <p>activeEq.</p>
     *
     * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param data   a R object
     * @param <R>    a R class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public <R extends Comparable<R>> QueryCondition<T> activeEq(SFunction<T, R> column, R data) {
        Sf.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
        return this;
    }

    /**
     * <p>activeLike.</p>
     *
     * @param column a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param data   a {@link java.lang.String} object
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public QueryCondition<T> activeLike(SFunction<T, String> column, String data) {
        StrOp.of(data).map(v -> super.like(column, v)).orElseRun(() -> Database.notActive(this));
        return this;
    }

    /**
     * <p>activeIn.</p>
     *
     * @param column   a {@link com.baomidou.mybatisplus.core.toolkit.support.SFunction} object
     * @param dataList a {@link java.util.Collection} object
     * @param <R>      a R class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public <R extends Comparable<R>> QueryCondition<T> activeIn(SFunction<T, R> column, Collection<R> dataList) {
        CollOp.of(dataList).map(v -> super.in(column, v)).orElseRun(() -> Database.notActive(this));
        return this;
    }

}
