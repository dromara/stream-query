package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.vampireachao.stream.core.clazz.ClassHelper;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.annotation.EnableMybatisPlusPlugin;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper.DynamicMapperHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * MybatisPlusTestApplication less Create Retrieve Update Delete
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/21
 */
@EnableMybatisPlusPlugin
@SpringBootApplication
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

    @Bean
    public DynamicMapperHandler dynamicMapperHandler(SqlSessionFactory sqlSessionFactory) throws Exception {
        /// 扫描po包下的所有类，作为entity
        String entityPackagePath = "io.github.vampireachao.stream.plugin.mybatisplus.pojo.po";
        final List<Class<?>> entityClassList = ClassHelper.scanClasses(entityPackagePath);
        return new DynamicMapperHandler(sqlSessionFactory, entityClassList);
    }
}
