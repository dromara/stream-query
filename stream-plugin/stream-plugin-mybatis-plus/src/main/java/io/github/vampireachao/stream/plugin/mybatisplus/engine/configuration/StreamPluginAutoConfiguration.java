package io.github.vampireachao.stream.plugin.mybatisplus.engine.configuration;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.enumration.SqlMethodEnum;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.methods.InsertOneSql;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.methods.UpdateOneSql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * MPSql注入
 *
 * @author VampireAchao ZVerify
 */
public class StreamPluginAutoConfiguration {

    @Bean
    @Order
    @ConditionalOnMissingBean(DefaultSqlInjector.class)
    public DefaultSqlInjector defaultSqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
                methodList.add(new InsertOneSql(SqlMethodEnum.INSERT_ONE_SQL.getMethod()));
                methodList.add(new UpdateOneSql(SqlMethodEnum.UPDATE_ONE_SQL.getMethod()));
                return methodList;
            }
        };
    }

}
