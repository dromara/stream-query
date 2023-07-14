package org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration;

/**
 * @author VampireAchao
 * @since 2023/7/14 18:10
 */
public class StreamPluginConfig {

    private static boolean safeModeEnabled = false;

    public static void setSafeModeEnabled(boolean safeModeEnabled) {
        StreamPluginConfig.safeModeEnabled = safeModeEnabled;
    }

    public static boolean isSafeModeEnabled() {
        return safeModeEnabled;
    }
}
