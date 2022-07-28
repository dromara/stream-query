package io.github.vampireachao.stream.plugin.mybatisplus.injector;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.injector.constant.PluginConst;
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

}
