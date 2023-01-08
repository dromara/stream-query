package io.github.vampireachao.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.annotation.EnableMybatisPlusPlugin;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper.DynamicMapperHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
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
        String entityPackagePath = "io.github.vampireachao.stream.plugin.mybatisplus.pojo.po";
        Enumeration<URL> resources = ClassLoader.getSystemClassLoader().getResources(entityPackagePath.replace(".", "/"));
        final List<Class<?>> entityClassList = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            File[] files = new File(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name())).listFiles();
            for (File file : files) {
                System.out.println(file);
                String path = file.getAbsolutePath();
                if (path.endsWith(".class")) {
                    String substring = path.substring(path.lastIndexOf("\\") + 1, path.length() - 6);
                    Class<?> entityClass = Class.forName(entityPackagePath + "." + substring);
                    entityClassList.add(entityClass);
                }
            }
        }
        return new DynamicMapperHandler(sqlSessionFactory, entityClassList);
    }
}
