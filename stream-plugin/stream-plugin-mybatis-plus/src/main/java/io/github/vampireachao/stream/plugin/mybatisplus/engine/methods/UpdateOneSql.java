package io.github.vampireachao.stream.plugin.mybatisplus.engine.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import io.github.vampireachao.stream.core.stream.Steam;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.enumration.SqlMethodEnum;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 修改多条数据（mysql语法批量）
 *
 * @author sikadai VampireAchao
 */
public class UpdateOneSql extends AbstractMethod implements PluginConst {

    public UpdateOneSql(String methodName) {
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
        SqlMethodEnum sqlMethod = SqlMethodEnum.UPDATE_ONE_SQL;
        String caseWhenScript = buildCaseWhen(tableInfo);
        StringBuilder whereScript = buildWhereSql(tableInfo);
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), caseWhenScript, whereScript);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, sqlSource);
    }

    /**
     * <p>
     * 构建caseWhen的跟新语句
     * </p>
     *
     * @param tableInfo 表信息
     * @return java.lang.StringBuilder
     * @author sikadai
     * @since 2022/8/24 18:42
     */
    private String buildCaseWhen(TableInfo tableInfo) {
        String safeKeyProperty = SqlScriptUtils.safeParam(ENTITY_DOT + tableInfo.getKeyProperty());
        return Steam.of(tableInfo.getFieldList())
                .filter(i -> !i.isLogicDelete())
                .map(i -> i.getColumn() + EQUALS + CASE + SPACE + tableInfo.getKeyColumn() + NEWLINE +
                        SqlScriptUtils.convertForeach(SqlScriptUtils.convertChoose(
                                String.format(NON_NULL_CONDITION, ENTITY, ENTITY_DOT + i.getProperty())
                                , String.format(WHEN_THEN, safeKeyProperty, SqlScriptUtils.safeParam(ENTITY_DOT + i.getProperty())),
                                String.format(WHEN_THEN, safeKeyProperty, i.getColumn())), COLLECTION_PARAM_NAME, null, ENTITY, null)
                        + END
                )
                .join(COMMA + NEWLINE);
    }

    /**
     * <p>
     * 构建where的批量更新
     * </p>
     *
     * @param tableInfo 表信息
     * @return java.lang.StringBuilder
     * @author sikadai
     * @since 2022/8/24 18:43
     */
    private StringBuilder buildWhereSql(TableInfo tableInfo) {
        StringBuilder whereSqlBuilder = new StringBuilder();
        whereSqlBuilder.append(tableInfo.getKeyColumn()).append(" IN\n");
        whereSqlBuilder.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\">\n");
        whereSqlBuilder.append("#{item.").append(tableInfo.getKeyProperty()).append("}").append("\n");
        whereSqlBuilder.append("</foreach>").append("\n");
        whereSqlBuilder.append(tableInfo.getLogicDeleteSql(true, true));
        return whereSqlBuilder;
    }
}
