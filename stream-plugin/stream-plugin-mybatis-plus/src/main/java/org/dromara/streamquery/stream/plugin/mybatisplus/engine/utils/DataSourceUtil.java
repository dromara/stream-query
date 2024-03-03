package org.dromara.streamquery.stream.plugin.mybatisplus.engine.utils;

import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicDataSource.DynamicRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Cason
 * @since 2024-02-25
 */
@Component
public class DataSourceUtil {
    private static DynamicRoutingDataSource dynamicRoutingDataSource;

    @Autowired
    public void setDynamicRoutingDataSource(DynamicRoutingDataSource dataSource) {
        DataSourceUtil.dynamicRoutingDataSource = dataSource;
    }

    public static DynamicRoutingDataSource getDynamicRoutingDataSource() {
        return dynamicRoutingDataSource;
    }
}
