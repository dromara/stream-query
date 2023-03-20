package io.github.vampireachao.stream.plugin.mybatisplus.engine.configuration;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import io.github.vampireachao.stream.core.lambda.LambdaHelper;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import io.github.vampireachao.stream.plugin.mybatisplus.Database;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.enumration.SqlMethodEnum;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.methods.SaveOneSql;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.methods.UpdateOneSql;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * MPSql注入
 *
 * @author VampireAchao Cizai_
 */
public class StreamPluginAutoConfiguration {

    private static final String CURRENT_NAMESPACE = LambdaHelper.getPropertyName(TableInfo::getCurrentNamespace);

    /**
     * <p>defaultSqlInjector.</p>
     *
     * @return a {@link com.baomidou.mybatisplus.core.injector.DefaultSqlInjector} object
     */
    @Bean
    @Order
    @ConditionalOnMissingBean(DefaultSqlInjector.class)
    public DefaultSqlInjector defaultSqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
                methodList.add(new SaveOneSql(SqlMethodEnum.SAVE_ONE_SQL.getMethod()));
                methodList.add(new UpdateOneSql(SqlMethodEnum.UPDATE_ONE_SQL.getMethod()));
                return methodList;
            }

            @Override
            public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
                super.inspectInject(builderAssistant, mapperClass);
                Class<?> modelClass = ReflectionKit.getSuperClassGenericType(mapperClass, Mapper.class, 0);
                if (modelClass == null) {
                    return;
                }
                TableInfo tableInfo = TableInfoHelper.initTableInfo(builderAssistant, modelClass);
                if (Database.isDynamicMapper(tableInfo.getCurrentNamespace()) &&
                        !mapperClass.getName().equals(tableInfo.getCurrentNamespace())) {
                    // 降低动态mapper优先级
                    ReflectHelper.setFieldValue(tableInfo, CURRENT_NAMESPACE, mapperClass.getName());
                }
                if (!Database.isDynamicMapper(mapperClass.getName())) {
                    Database.getEntityMapperClassCache().put(modelClass, mapperClass);
                }
            }
        };
    }

}
