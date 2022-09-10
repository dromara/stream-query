package io.github.vampireachao.stream.plugin.mybatisplus.pojo.po;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * RoleInfo
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/5/23

 */
@Data
@KeySequence(dbType = DbType.H2)
public class RoleInfo {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String roleName;
}
