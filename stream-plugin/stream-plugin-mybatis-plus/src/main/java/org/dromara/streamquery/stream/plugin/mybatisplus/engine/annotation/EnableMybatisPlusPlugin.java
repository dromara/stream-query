package org.dromara.streamquery.stream.plugin.mybatisplus.engine.annotation;

import org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration.StreamPluginAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启sql注入
 *
 * @author VampireAchao Cizai_

 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@Import({StreamPluginAutoConfiguration.class})
public @interface EnableMybatisPlusPlugin {
}
