package org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.inner;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.dromara.streamquery.stream.plugin.mybatisplus.annotation.Entity;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.annotation.GenerateMapper;

/**
 * @author <a href = "kamtohung@gmail.com">KamTo Hung</a>
 */
@Data
@GenerateMapper
public class AddressInfo {

  @TableId(type = IdType.ASSIGN_ID)
  private String id;

  private String address;

}
