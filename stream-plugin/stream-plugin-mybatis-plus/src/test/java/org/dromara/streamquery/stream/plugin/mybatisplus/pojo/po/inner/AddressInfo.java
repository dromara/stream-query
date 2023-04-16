package org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.inner;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.annotation.GenerateMapper;

/**
 * AddressInfo
 *
 * @author KamToHung
 */
@Data
@GenerateMapper
public class AddressInfo {

  @TableId(type = IdType.ASSIGN_ID)
  private String id;

  private String address;
}
