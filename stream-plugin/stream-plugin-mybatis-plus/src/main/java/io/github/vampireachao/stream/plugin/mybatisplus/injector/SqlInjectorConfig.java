package io.github.vampireachao.stream.plugin.mybatisplus.injector;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import io.github.vampireachao.stream.core.stream.Steam;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
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
                methodList.add(new AbstractMethod(SqlMethodEnum.INSERT_ONE_SQL.getMethod()) {
                    @Override
                    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
                        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
                        SqlMethodEnum sqlMethod = SqlMethodEnum.INSERT_ONE_SQL;
                        String columnScript = SqlScriptUtils.convertTrim(tableInfo.getKeyInsertSqlColumn(false, true) + Steam.of(tableInfo.getFieldList()).map(TableFieldInfo::getColumn)
                                        .join(COMMA),
                                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
                        String valuesScript = SqlScriptUtils.convertTrim(tableInfo.getKeyInsertSqlProperty(false, ENTITY_DOT, true) + Steam.of(tableInfo.getFieldList())
                                        .map(i -> SqlScriptUtils.convertChoose(String.format("%s != null and %s != null",
                                                        ENTITY, ENTITY_DOT + i.getProperty()),
                                                i.getInsertSqlProperty(ENTITY_DOT),
                                                "default" + COMMA)).nonNull().join(NEWLINE),
                                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
                        valuesScript = SqlScriptUtils.convertForeach(valuesScript, "list", null, ENTITY, COMMA);

                        String keyProperty = null;
                        String keyColumn = null;
                        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
                        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
                            if (tableInfo.getIdType() == IdType.AUTO) {
                                /* 自增主键 */
                                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                                keyProperty = tableInfo.getKeyProperty();
                                keyColumn = tableInfo.getKeyColumn();
                            } else if (null != tableInfo.getKeySequence()) {
                                keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
                                keyProperty = tableInfo.getKeyProperty();
                                keyColumn = tableInfo.getKeyColumn();
                            }
                        }
                        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
                        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
                        return this.addInsertMappedStatement(mapperClass, modelClass, sqlSource, keyGenerator, keyProperty, keyColumn);
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
