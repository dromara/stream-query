package io.github.vampireachao.stream.plugin.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.vampireachao.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserInfoMapper
 *
 * @author VampireAchao
 * @since 2022/5/21
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
