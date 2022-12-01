package io.github.vampireachao.stream.plugin.mybatisplus.engine.enumration;

/**
 * sql方法类型
 *
 * @author VampireAchao Cizai_

 */
public enum SqlMethodEnum {
    /**
     * 插入
     */
    SAVE_ONE_SQL("saveOneSql", "插入多条数据（mysql语法批量）", "<script>\nINSERT INTO %s %s VALUES %s\n</script>"),
    UPDATE_ONE_SQL("updateOneSql", "修改多条数据（mysql语法批量）", "<script>\nUPDATE %s SET %s WHERE %s\n</script>"),
    ;
    private final String method;
    private final String desc;
    private final String sql;

    SqlMethodEnum(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    /**
     * <p>Getter for the field <code>method</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getMethod() {
        return method;
    }

    /**
     * <p>Getter for the field <code>desc</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDesc() {
        return desc;
    }

    /**
     * <p>Getter for the field <code>sql</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSql() {
        return sql;
    }
}
