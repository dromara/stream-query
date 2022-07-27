package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.vampireachao.stream.plugin.mybatisplus.injector.EnableSqlInject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * MybatisPlusTestApplication less Create Retrieve Update Delete
 *
 * @author VampireAchao
 * @since 2022/5/21
 */
@EnableSqlInject
@SpringBootApplication
@MapperScan({"io.github.vampireachao.stream.plugin.mybatisplus.mapper**"})
public class MybatisPlusTestApplication {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }
}
