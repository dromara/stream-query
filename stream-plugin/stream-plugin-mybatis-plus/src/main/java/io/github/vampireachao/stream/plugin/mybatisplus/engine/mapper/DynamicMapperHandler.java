package io.github.vampireachao.stream.plugin.mybatisplus.engine.mapper;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import io.github.vampireachao.stream.plugin.mybatisplus.Database;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * DynamicMapperHandler
 *
 * @author VampireAchao
 * @since 2023/1/8
 */
public class DynamicMapperHandler {

    public DynamicMapperHandler(SqlSessionFactory sqlSessionFactory, List<Class<?>> entityClassList) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        if (configuration instanceof MybatisConfiguration) {
            MybatisConfiguration mybatisConfiguration = (MybatisConfiguration) configuration;
            entityClassList.forEach(entityClass -> Database.buildMapper(mybatisConfiguration, entityClass));
        }
    }
}
