package io.github.vampireachao.stream.plugin.mybatisplus.mapper;

import io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper.IMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.methods.InsertOneSql;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;


/**
 * UserInfoMapper
 *
 * @author VampireAchao
 * @since 2022/5/21
 */
public interface UserInfoMapper extends IMapper<UserInfo> {

    long updateOneSql(@Param(InsertOneSql.COLLECTION_PARAM_NAME) Collection<UserInfo> list);
}
