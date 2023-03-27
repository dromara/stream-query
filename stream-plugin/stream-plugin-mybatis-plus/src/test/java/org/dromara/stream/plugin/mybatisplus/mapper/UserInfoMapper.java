package org.dromara.stream.plugin.mybatisplus.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.stream.plugin.mybatisplus.engine.mapper.IMapper;
import org.dromara.stream.plugin.mybatisplus.pojo.po.UserInfo;

/**
 * UserInfoMapper
 *
 * @author VampireAchao
 * @since 2023/2/9
 */
@Mapper
public interface UserInfoMapper extends IMapper<UserInfo> {
}
