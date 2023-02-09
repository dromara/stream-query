package io.github.vampireachao.stream.plugin.mybatisplus.mapper;

import io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper.IMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserInfoMapper
 *
 * @author VampireAchao
 * @since 2023/2/9
 */
@Mapper
public interface UserInfoMapper extends IMapper<UserInfo> {
}
