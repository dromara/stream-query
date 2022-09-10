package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.annotation.EnableMybatisPlusPlugin;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * MybatisPlusTestApplication less Create Retrieve Update Delete
 *
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 * @since 2022/5/21

 */
@EnableMybatisPlusPlugin
@SpringBootApplication
@MapperScan({"io.github.vampireachao.stream.plugin.mybatisplus.mapper**"})
public class MybatisPlusTestApplication {
    /**
     * <p>mybatisPlusInterceptor.</p>
     *
     * @return a {@link com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor} object
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
