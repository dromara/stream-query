package io.github.vampireachao.stream.plugin.mybatisplus.injector;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * MPSql注入
 *
 * @author VampireAchao
 */
public class SqlInjectorConfig {

    @Bean
    @ConditionalOnMissingBean(DefaultSqlInjector.class)
    public DefaultSqlInjector defaultSqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
                methodList.add(new AbstractMethod(SqlMethodEnum.INSERT_BATCH.getMethod()) {
                    @Override
                    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
                        final String sql = SqlMethodEnum.INSERT_BATCH.getSql();
                        final String fieldSql = prepareFieldSql(tableInfo);
                        final String valueSql = prepareValuesSqlForMysqlBatch(tableInfo);
                        final String sqlResult = String.format(sql, tableInfo.getTableName(), fieldSql, valueSql);
                        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
                        return this.addInsertMappedStatement(mapperClass, modelClass, sqlSource, new NoKeyGenerator(), null, null);
                    }

                    private String prepareFieldSql(TableInfo tableInfo) {
                        StringBuilder fieldSql = new StringBuilder();
                        if (!IdType.AUTO.equals(tableInfo.getIdType())) {
                            fieldSql.append(tableInfo.getKeyColumn()).append(",");
                        }
                        tableInfo.getFieldList().forEach(x -> fieldSql.append(x.getColumn()).append(","));
                        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
                        fieldSql.insert(0, "(");
                        fieldSql.append(")");
                        return fieldSql.toString();
                    }

                    private String prepareValuesSqlForMysqlBatch(TableInfo tableInfo) {
                        final StringBuilder valueSql = new StringBuilder();
                        valueSql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
                        if (!IdType.AUTO.equals(tableInfo.getIdType())) {
                            valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
                        }
                        tableInfo.getFieldList().forEach(x -> valueSql.append("#{item.").append(x.getProperty()).append("},"));
                        valueSql.delete(valueSql.length() - 1, valueSql.length());
                        valueSql.append("</foreach>");
                        return valueSql.toString();
                    }
                });
                return methodList;
            }
        };
    }

}
