package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.optional.Opp;

import java.util.Collection;
import java.util.Objects;

/**
 * <p>QueryCondition class.</p>
 *
 * @author VampireAchao ZVerify
 * @since 2022/8/15 16:39
 */
public class QueryCondition<T> extends LambdaQueryWrapper<T> {

    /**
     * <p>query.</p>
     *
     * @param entity a T object
     * @param <T>    a T class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public static <T> QueryCondition<T> query(T entity) {
        QueryCondition<T> condition = new QueryCondition<>();
        condition.setEntity(entity);
        return condition;
    }

    /**
     * <p>query.</p>
     *
     * @param entityClass a {@link java.lang.Class} object
     * @param <T>         a T class
     * @return a {@link io.github.vampireachao.stream.plugin.mybatisplus.QueryCondition} object
     */
    public static <T> QueryCondition<T> query(Class<T> entityClass) {
        QueryCondition<T> condition = new QueryCondition<>();
        condition.setEntityClass(entityClass);
        return condition;
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
        Opp.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
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
        Opp.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
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
        Opp.of(data).map(v -> super.like(column, v)).orElseRun(() -> Database.notActive(this));
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
        Opp.ofColl(dataList).map(v -> super.in(column, v)).orElseRun(() -> Database.notActive(this));
        return this;
    }

}
