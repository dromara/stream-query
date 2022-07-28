package io.github.vampireachao.stream.plugin.mybatisplus.mapper;

import io.github.vampireachao.stream.plugin.mybatisplus.injector.IMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

import static io.github.vampireachao.stream.plugin.mybatisplus.injector.SqlInjectorConfig.COLLECTION_PARAM_NAME;

/**
 * UserInfoMapper
 *
 * @author VampireAchao
 * @since 2022/5/21
 */
public interface UserInfoMapper extends IMapper<UserInfo> {

    long updateOneSql(@Param(COLLECTION_PARAM_NAME) Collection<UserInfo> list);
}
