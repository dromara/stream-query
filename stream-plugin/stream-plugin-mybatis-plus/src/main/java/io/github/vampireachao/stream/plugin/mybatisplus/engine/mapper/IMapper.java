package io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;


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
     * 批量新增（mysql语法批量）
     *
     * @param batchList 批量数据
     * @return 结果
     */
    int insertBatch(@Param("items") List<T> batchList);

}
