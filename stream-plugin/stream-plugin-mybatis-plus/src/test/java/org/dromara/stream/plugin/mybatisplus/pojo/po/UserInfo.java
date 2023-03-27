package org.dromara.stream.plugin.mybatisplus.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * UserInfo
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/21
 */
@Data
public class UserInfo {

    private static final long serialVersionUID = -7219188882388819210L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String email;
    @Version
    private Integer version;
    @TableLogic(value = "'2001-01-01 00:00:00'", delval = "NOW()")
    private LocalDateTime gmtDeleted;
}