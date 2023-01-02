package io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.vampireachao.stream.core.stream.Steam;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;


/**
 * 拓展BaseMapper
 *
 * @author VampireAchao Cizai_

 */
public interface IMapper<T> extends BaseMapper<T> {

    /**
     * 插入多条数据（mysql语法批量）
     *
     * @param list 数据
     * @return 条数
     */
    long saveOneSql(@Param(PluginConst.COLLECTION_PARAM_NAME) Collection<T> list);

    /**
     * 更新多条数据（mysql语法批量）
     *
     * @param list 数据
     * @return 条数
     */
    long updateOneSql(@Param(PluginConst.COLLECTION_PARAM_NAME) Collection<T> list);


    /**
     * 批量插入
     *
     * @param list      集合
     * @param batchSize 分割量
     * @return 是否成功
     */
    default long updateFewSql(Collection<T> list, int batchSize) {
        return Steam.of(list).splitList(batchSize).mapToLong(this::updateOneSql).sum();
    }

    /**
     * 批量插入
     *
     * @param list      集合
     * @param batchSize 分割量
     * @return 是否成功
     */
    default long saveFewSql(Collection<T> list, int batchSize) {
        return Steam.of(list).splitList(batchSize).mapToLong(this::saveOneSql).sum();
    }
}
