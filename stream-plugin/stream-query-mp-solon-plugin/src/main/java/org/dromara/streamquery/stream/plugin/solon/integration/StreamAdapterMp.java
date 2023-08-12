package org.dromara.streamquery.stream.plugin.solon.integration;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.solon.integration.MybatisAdapterPlus;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.Environment;
import org.dromara.streamquery.stream.core.lambda.LambdaHelper;
import org.dromara.streamquery.stream.core.reflect.ReflectHelper;
import org.dromara.streamquery.stream.plugin.solon.Database;
import org.dromara.streamquery.stream.plugin.solon.engine.methods.SaveOneSql;
import org.dromara.streamquery.stream.plugin.solon.engine.methods.UpdateOneSql;
import org.dromara.streamquery.stream.plugin.solon.handler.JsonPostInitTableInfoHandler;
import org.dromara.streamquery.stream.plugin.solon.scanner.StreamScanConfig;
import org.noear.solon.Utils;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Props;

import java.util.Collection;
import java.util.List;


/**
 * @author Cason
 */
public class StreamAdapterMp extends MybatisAdapterPlus {
    private static final String CURRENT_NAMESPACE =
            LambdaHelper.getPropertyName(TableInfo::getCurrentNamespace);

    StreamScanConfig cfg;
    protected StreamAdapterMp(BeanWrap dsWrap) {
        super(dsWrap);
    }

    protected StreamAdapterMp(BeanWrap dsWrap, Props dsProps) {
        super(dsWrap, dsProps);
    }

    @Override
    protected void initConfiguration(Environment environment) {
        super.initConfiguration(environment);
        initScanConfig();
        enableStreamQuery();
    }


    private void enableStreamQuery() {
        getGlobalConfig().setPostInitTableInfoHandler(new JsonPostInitTableInfoHandler());
        getGlobalConfig().setSqlInjector(
                new DefaultSqlInjector() {
                    @Override
                    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
                        methodList.add(new SaveOneSql());
                        methodList.add(new UpdateOneSql());
                        return methodList;
                    }

                    @Override
                    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
                        super.inspectInject(builderAssistant, mapperClass);
                        Class<?> modelClass =
                                ReflectionKit.getSuperClassGenericType(mapperClass, Mapper.class, 0);
                        if (modelClass == null) {
                            return;
                        }
                        TableInfo tableInfo = TableInfoHelper.initTableInfo(builderAssistant, modelClass);
                        if (Database.isDynamicMapper(tableInfo.getCurrentNamespace())
                                && !mapperClass.getName().equals(tableInfo.getCurrentNamespace())) {
                            // 降低动态mapper优先级
                            ReflectHelper.setFieldValue(tableInfo, CURRENT_NAMESPACE, mapperClass.getName());
                        }
                        if (!Database.isDynamicMapper(mapperClass.getName())) {
                            Database.getEntityMapperClassCache().put(modelClass, mapperClass);
                        }
                    }
                });

        if (getConfiguration() instanceof MybatisConfiguration) {
            MybatisConfiguration mybatisConfiguration = (MybatisConfiguration) getConfiguration();
            cfg.scan();
            Collection<Class<?>> entityClassList = cfg.getEntityClasses();
            entityClassList.forEach(
                    entityClass -> Database.buildMapper(mybatisConfiguration, entityClass));
        }
    }

    private void initScanConfig() {
        cfg = new StreamScanConfig();

        Props scanProps = dsProps.getProp("stream-query");
        if (scanProps.size() > 0) {
            Utils.injectProperties(cfg, scanProps);
        }
    }


}
