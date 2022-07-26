package io.github.vampireachao.stream.plugin.mybatisplus.injector;

/**
 * sql方法类型
 *
 * @author VampireAchao
 */
public enum SqlMethodEnum {
    /**
     * 插入
     */
    INSERT_BATCH("insertBatch", "插入多条数据（mysql语法批量）", "<script>\nINSERT INTO %s %s VALUES %s\n</script>"),
    ;
    private final String method;
    private final String desc;
    private final String sql;

    SqlMethodEnum(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
