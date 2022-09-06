package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.github.vampireachao.stream.core.optional.CollOp;
import io.github.vampireachao.stream.core.optional.Op;
import io.github.vampireachao.stream.core.optional.StrOp;

import java.util.Collection;
import java.util.Objects;

/**
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/8/15 16:39
 */
public class QueryCondition<T> extends LambdaQueryWrapper<T> {


    public QueryCondition(T entity) {
        super(Op.of(entity).orElseThrow(() -> new IllegalArgumentException("entity can not be null")));
    }

    public QueryCondition(Class<T> entityClass) {
        super(Op.of(entityClass).orElseThrow(() -> new IllegalArgumentException("entityClass can not be null")));
    }

    public static <T> QueryCondition<T> query(T entity) {
        return new QueryCondition<>(entity);
    }

    public static <T> QueryCondition<T> query(Class<T> entityClass) {
        return new QueryCondition<>(entityClass);
    }

    public QueryCondition<T> eq(SFunction<T, String> column, String data) {
        super.eq(StringUtils.isNotEmpty(data), column, data);
        return this;
    }

    public <R extends Comparable<R>> QueryCondition<T> eq(SFunction<T, R> column, R data) {
        super.eq(Objects.nonNull(data), column, data);
        return this;
    }

    public QueryCondition<T> like(SFunction<T, String> column, String data) {
        super.like(StringUtils.isNotEmpty(data), column, data);
        return this;
    }

    public <R extends Comparable<R>> QueryCondition<T> in(SFunction<T, R> column, Collection<R> dataList) {
        super.in(CollectionUtils.isNotEmpty(dataList), column, dataList);
        return this;
    }


    public QueryCondition<T> activeEq(SFunction<T, String> column, String data) {
        StrOp.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
        return this;
    }

    public <R extends Comparable<R>> QueryCondition<T> activeEq(SFunction<T, R> column, R data) {
        Op.of(data).map(v -> super.eq(column, v)).orElseRun(() -> Database.notActive(this));
        return this;
    }

    public QueryCondition<T> activeLike(SFunction<T, String> column, String data) {
        StrOp.of(data).map(v -> super.like(column, v)).orElseRun(() -> Database.notActive(this));
        return this;
    }

    public <R extends Comparable<R>> QueryCondition<T> activeIn(SFunction<T, R> column, Collection<R> dataList) {
        CollOp.of(dataList).map(v -> super.in(column, v)).orElseRun(() -> Database.notActive(this));
        return this;
    }

}
