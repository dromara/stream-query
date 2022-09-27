package io.github.vampireachao.stream.plugin.mybatisplus.engine.methods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import io.github.vampireachao.stream.core.stream.Steam;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.enumration.SqlMethodEnum;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 插入多条数据（mysql语法批量）
 *
 * @author VampireAchao ZVerify
 */
public class InsertOneSql extends AbstractMethod implements PluginConst {

    public InsertOneSql(String methodName) {
        super(methodName);
    }

    /**
     * 注入自定义 MappedStatement
     *
     * @param mapperClass mapper 接口
     * @param modelClass  mapper 泛型
     * @param tableInfo   数据库表反射信息
     * @return MappedStatement
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        SqlMethodEnum sqlMethod = SqlMethodEnum.INSERT_ONE_SQL;
        // column script
        String columnScript = SqlScriptUtils.convertTrim(Steam.of(tableInfo.getFieldList()).map(TableFieldInfo::getColumn)
                        .unshift(tableInfo.getKeyColumn())
                        .join(COMMA),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        // value column script in loop
        String safeProperty = SqlScriptUtils.safeParam(ENTITY_DOT + tableInfo.getKeyProperty()) + COMMA;
        String propertyOrDefault = SqlScriptUtils.convertChoose(
                String.format(NON_NULL_CONDITION, ENTITY, ENTITY_DOT + tableInfo.getKeyProperty()),
                safeProperty,
                DEFAULT + COMMA);
        String valuesScript = SqlScriptUtils.convertTrim(Steam.of(tableInfo.getFieldList())
                        .map(i -> SqlScriptUtils.convertChoose(
                                String.format(NON_NULL_CONDITION, ENTITY, ENTITY_DOT + i.getProperty()),
                                i.getInsertSqlProperty(ENTITY_DOT),
                                DEFAULT + COMMA))
                        .unshift(tableInfo.getIdType() == IdType.AUTO ? propertyOrDefault : safeProperty)
                        .nonNull().join(NEWLINE),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        // value part into foreach
        valuesScript = SqlScriptUtils.convertForeach(valuesScript, COLLECTION_PARAM_NAME, null, ENTITY, COMMA);

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
}
