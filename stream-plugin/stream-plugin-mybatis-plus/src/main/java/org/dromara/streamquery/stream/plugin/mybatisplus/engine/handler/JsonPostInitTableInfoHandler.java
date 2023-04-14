package org.dromara.streamquery.stream.plugin.mybatisplus.engine.handler;

import com.baomidou.mybatisplus.core.handlers.PostInitTableInfoHandler;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

/**
 * @author VampireAchao
 * @since 2023/3/20 18:10
 */
public class JsonPostInitTableInfoHandler implements PostInitTableInfoHandler {

    /**
     * 参与 TableInfo 初始化
     *
     * @param tableInfo     TableInfo
     * @param configuration Configuration
     */
    @Override
    public void postTableInfo(TableInfo tableInfo, Configuration configuration) {
        PostInitTableInfoHandler.super.postTableInfo(tableInfo, configuration);
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            if (fieldInfo.getTypeHandler() == null) {
                continue;
            }
            if (tableInfo.getResultMap() == null) {
                return;
            }
            ResultMap resultMap = configuration.getResultMap(tableInfo.getResultMap());
            for (ResultMapping resultMapping : resultMap.getResultMappings()) {
                TypeHandler<?> handler = resultMapping.getTypeHandler();
                if (handler instanceof AbstractJsonFieldHandler) {
                    AbstractJsonFieldHandler<Object> typeHandler = (AbstractJsonFieldHandler<Object>) handler;
                    typeHandler.setTableInfo(tableInfo);
                    typeHandler.setFieldInfo(fieldInfo);
                }
            }
        }
    }
}
