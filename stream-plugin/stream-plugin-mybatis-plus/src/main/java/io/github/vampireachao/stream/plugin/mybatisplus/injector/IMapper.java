package io.github.vampireachao.stream.plugin.mybatisplus.injector;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

import static io.github.vampireachao.stream.plugin.mybatisplus.injector.SqlInjectorConfig.COLLECTION_PARAM_NAME;

/**
 * @author VampireAchao
 */
public interface IMapper<T> extends BaseMapper<T> {

    /**
     * 插入多条数据（mysql语法批量）
     *
     * @param list 数据
     * @return 条数
     */
    long insertOneSql(@Param(COLLECTION_PARAM_NAME) Collection<T> list);

}
