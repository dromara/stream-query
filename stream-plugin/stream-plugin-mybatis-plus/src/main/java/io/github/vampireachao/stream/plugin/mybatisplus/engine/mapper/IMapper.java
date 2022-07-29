package io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.vampireachao.stream.core.stream.Steam;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;


/**
 * 拓展BaseMapper
 *
 * @author VampireAchao
 */
public interface IMapper<T> extends BaseMapper<T> {

    /**
     * 插入多条数据（mysql语法批量）
     *
     * @param list 数据
     * @return 条数
     */
    long insertOneSql(@Param(PluginConst.COLLECTION_PARAM_NAME) Collection<T> list);

    /**
     * 批量插入, 默认一千分批
     *
     * @param list 集合
     * @return 是否成功
     */
    default boolean insertFewSql(Collection<T> list) {
        return this.insertFewSql(list, PluginConst.DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量插入, 默认一千分批
     *
     * @param list      集合
     * @param batchSize 分割量
     * @return 是否成功
     */
    default boolean insertFewSql(Collection<T> list, int batchSize) {
        return Steam.of(list).subList(batchSize).allMatch(l -> this.insertOneSql(l) == l.size());
    }
}
