package io.github.vampireachao.stream.plugin.mybatisplus.engine.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 修改多条数据（mysql语法批量）
 *
 * @author VampireAchao
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
        StringBuilder sqlResult = new StringBuilder();
        sqlResult.append("<script>\n");
        String updateTableBuilder = "UPDATE " + tableInfo.getTableName() + " SET ";
        // 构建caseWhen的语句
        StringBuilder caseWhenSqlBuild = buildCaseWhen(tableInfo);
        // 构建where的语句
        StringBuilder whereSqlBuilder = buildWhereSql(tableInfo);
        sqlResult.append(updateTableBuilder).append(caseWhenSqlBuild).append(whereSqlBuilder);
        sqlResult.append("</script>");
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult.toString(), modelClass);
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
    private StringBuilder buildCaseWhen(TableInfo tableInfo) {
        StringBuilder caseWhenSqlBuild = new StringBuilder();
        int count = 0;
        int fieldSize = tableInfo.getFieldList().size();
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            count++;
            caseWhenSqlBuild.append(fieldInfo.getColumn()).append(" = CASE ").append(tableInfo.getKeyColumn()).append("\n");
            caseWhenSqlBuild.append("<foreach collection=\"list\" item=\"item\" index=\"index\">\n");
            caseWhenSqlBuild.append("<choose>\n");
            caseWhenSqlBuild.append("<when test=\"item.").append(fieldInfo.getProperty()).append(" != null\">\n");
            caseWhenSqlBuild.append("WHEN #{item.").append(tableInfo.getKeyProperty()).append("} THEN #{item.").append(fieldInfo.getProperty()).append("}");
            caseWhenSqlBuild.append("</when>\n");
            caseWhenSqlBuild.append("<otherwise>\n");
            caseWhenSqlBuild.append("WHEN #{item.").append(tableInfo.getKeyProperty()).append("} THEN ").append(fieldInfo.getColumn());
            caseWhenSqlBuild.append("</otherwise>\n");
            caseWhenSqlBuild.append("</choose>\n");
            caseWhenSqlBuild.append("</foreach>\n");
            caseWhenSqlBuild.append("END");
            if (count < fieldSize) {
                caseWhenSqlBuild.append(",\n");
            } else {
                caseWhenSqlBuild.append("\n");
            }
        }
        return caseWhenSqlBuild;
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
        whereSqlBuilder.append("WHERE ").append(tableInfo.getKeyColumn()).append(" IN\n");
        whereSqlBuilder.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\">\n");
        whereSqlBuilder.append("#{item.").append(tableInfo.getKeyProperty()).append("}").append("\n");
        whereSqlBuilder.append("</foreach>");
        return whereSqlBuilder;
    }
}
