package io.github.vampireachao.stream.plugin.mybatisplus.engine.configuration;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.enumration.SqlMethodEnum;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.methods.InsertOneSql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * MPSql注入
 *
 * @author VampireAchao
 */
public class StreamPluginAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(DefaultSqlInjector.class)
    public DefaultSqlInjector defaultSqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
                methodList.add(new InsertOneSql(SqlMethodEnum.INSERT_ONE_SQL.getMethod()));
                return methodList;
            }
        };
    }

}
