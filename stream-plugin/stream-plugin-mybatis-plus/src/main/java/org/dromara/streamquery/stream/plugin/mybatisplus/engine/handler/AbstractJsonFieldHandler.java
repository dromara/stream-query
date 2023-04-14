package org.dromara.streamquery.stream.plugin.mybatisplus.engine.handler;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;

/**
 * @author VampireAchao
 * @since 2023/4/14 13:45
 */
public abstract class AbstractJsonFieldHandler<T> extends AbstractJsonTypeHandler<T> {

    private TableInfo tableInfo;

    private TableFieldInfo fieldInfo;

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public TableFieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(TableFieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    @Override
    protected T parse(String json) {
        return parse(json, tableInfo, fieldInfo);
    }

    @Override
    protected String toJson(T obj) {
        return toJson(obj, tableInfo, fieldInfo);
    }

    protected abstract T parse(String json, TableInfo tableInfo, TableFieldInfo fieldInfo);

    protected abstract String toJson(T obj, TableInfo tableInfo, TableFieldInfo fieldInfo);
}
