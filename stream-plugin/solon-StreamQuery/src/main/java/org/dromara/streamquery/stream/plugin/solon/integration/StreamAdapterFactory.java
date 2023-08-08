package org.dromara.streamquery.stream.plugin.solon.integration;

import org.apache.ibatis.solon.MybatisAdapter;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Props;

/**

/**
 * @author Cason
 */
public interface StreamAdapterFactory {
    StreamAdapter create(BeanWrap dsWrap);
    StreamAdapter create(BeanWrap dsWrap, Props dsProps);
}
