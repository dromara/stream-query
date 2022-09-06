package io.github.vampireachao.stream.plugin.mybatisplus.engine.annotation;

import io.github.vampireachao.stream.plugin.mybatisplus.engine.configuration.StreamPluginAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启sql注入
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@Import({StreamPluginAutoConfiguration.class})
public @interface EnableMybatisPlusPlugin {
}
