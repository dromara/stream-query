package io.github.vampireachao.stream.plugin.mybatisplus.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Collections;
import java.util.List;

/**
 * 扩展INSERT_BATCH
 *
 * @author handy
 */
public class InsertBatch extends AbstractMethod {

    /**
     * 注入方法
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = new NoKeyGenerator();
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, getBatchInsertSql(tableInfo), Collections.class);
        return this.addMappedStatement(mapperClass, SqlMethodEnum.INSERT_BATCH.getMethod(), sqlSource, SqlCommandType.INSERT, Collections.class, null, int.class, keyGenerator, null, null);
    }

    /**
     * 获取sql
     *
     * @param tableInfo 表信息
     * @return sql
     */
    private String getBatchInsertSql(TableInfo tableInfo) {
        String batchInsertSql = SqlMethodEnum.INSERT_BATCH.getSql();
        StringBuilder insertColumnBuilder = new StringBuilder();
        StringBuilder insertValueBuilder = new StringBuilder();
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        for (int i = 0; i < fieldList.size(); i++) {
            TableFieldInfo tableFieldInfo = fieldList.get(i);
            insertColumnBuilder.append(tableFieldInfo.getColumn());
            if (i < fieldList.size() - 1) {
                insertColumnBuilder.append(",");
            }
            insertValueBuilder.append("#{item.").append(tableFieldInfo.getProperty()).append("}");
            if (i < fieldList.size() - 1) {
                insertValueBuilder.append(",\n");
            }
        }
        String foreachSql = " <foreach collection='items' item='item'  open='' index='index' separator=','>\n" + "(%s)</foreach>";
        foreachSql = String.format(foreachSql, insertValueBuilder);
        return String.format(batchInsertSql, tableInfo.getTableName(), insertColumnBuilder, foreachSql);
    }

}