package io.github.vampireachao.stream.plugin.mybatisplus.injector;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启sql注入
 *
 * @author VampireAchao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@Import({StreamQuerySqlInjectorAutoConfiguration.class})
public @interface EnableSqlInject {
}
