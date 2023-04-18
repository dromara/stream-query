package org.dromara.streamquery.stream.plugin.mybatisplus.annotation;

import org.dromara.streamquery.stream.plugin.mybatisplus.engine.annotation.EnableMybatisPlusPlugin;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration.StreamScannerConfigurer;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.RoleInfo;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.UserRole;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.inner.AddressInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EnableMybatisPlusPluginByClassesTest
 * @author <a href = "kamtohung@gmail.com">KamTo Hung</a>
 */
@EnableAutoConfiguration
@EnableMybatisPlusPlugin(classes = {UserInfo.class})
public class EnableMybatisPlusPluginByClassesTest extends AbstractMybatisPlusTestApplication {

    @Test
    void testScanByValue() {
        StreamScannerConfigurer bean = context.getBean(StreamScannerConfigurer.class);
        assertNotNull(bean);
        assertNotNull(bean.getEntityClasses());
        assertFalse(bean.getEntityClasses().contains(RoleInfo.class));
        assertTrue(bean.getEntityClasses().contains(UserInfo.class));
        assertFalse(bean.getEntityClasses().contains(UserRole.class));
        assertFalse(bean.getEntityClasses().contains(AddressInfo.class));
        assertFalse(bean.getEntityClasses().contains(AddressInfo.InnerAddressInfo.class));
    }

}
