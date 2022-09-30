package io.github.vampireachao.stream.plugin.mybatisplus.engine.constant;

/**
 * @author VampireAchao ZVerify
 */
public interface PluginConst {

    /**
     * default batch commit count
     */
    int DEFAULT_BATCH_SIZE = 500;
    /**
     * db keyword default
     */
    String DEFAULT = "default";
    /**
     * db keyword case
     */
    String CASE = "case";
    /**
     * db keyword end
     */
    String END = "end";
    /**
     * mapper non null condition
     */
    String NON_NULL_CONDITION = "%s != null and %s != null";
    /**
     * db keyword when then template
     */
    String WHEN_THEN = "when %s then %s";
    /**
     * collection parameter name
     */
    String COLLECTION_PARAM_NAME = "list";
    /**
     * wrapper not active
     */
    String WRAPPER_NOT_ACTIVE = "WRAPPER_NOT_ACTIVE";


}
