package io.github.vampireachao.stream.plugin.mybatisplus.pojo.po;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * RoleInfo
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/23
 */
@Data
@KeySequence(dbType = DbType.H2)
public class RoleInfo {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String roleName;
}
