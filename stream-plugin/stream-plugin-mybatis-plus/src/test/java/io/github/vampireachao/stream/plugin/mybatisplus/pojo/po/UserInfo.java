package io.github.vampireachao.stream.plugin.mybatisplus.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@Data
public class UserInfo {

    private static final long serialVersionUID = -7219188882388819210L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
