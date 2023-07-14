package org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author VampireAchao
 * @since 2023/7/14 18:06
 */
@ConfigurationProperties(prefix = "stream-query.mybatis-plus")
public class StreamPluginProperties {

    private boolean safeMode;

    public boolean isSafeMode() {
        return safeMode;
    }

    public void setSafeMode(boolean safeMode) {
        this.safeMode = safeMode;
    }
}
