package issue.org.dromara.streamquery.gitee.issue17BSNV;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.mapper.IGenerateMapper;

import java.time.LocalDateTime;

@Data
@Table(value = "user_info")
public class UserInfo implements IGenerateMapper {
  private static final long serialVersionUID = -7219188882388819210L;
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  private String name;
  private Integer age;
  private String email;
  @TableLogic(value = "'2001-01-01 00:00:00'", delval = "NOW()")
  private LocalDateTime gmtDeleted;
}
