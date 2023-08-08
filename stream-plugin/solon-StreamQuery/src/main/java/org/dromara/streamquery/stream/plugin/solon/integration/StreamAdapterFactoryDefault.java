package org.dromara.streamquery.stream.plugin.solon.integration;/**
 * @ClassName StreamAdapterFactoryDefault.java
 * @author 任士博
 * @version 1.0.0
 * @Description TODO
 * @createTime 2023年08月04日 16:27:00
 */

import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Props;

/**
 * @author 黄清城
 */
public class StreamAdapterFactoryDefault implements StreamAdapterFactory {
    @Override
    public StreamAdapter create(BeanWrap dsWrap) {
        return new StreamAdapter(dsWrap);
    }

    @Override
    public StreamAdapter create(BeanWrap dsWrap, Props streamProps) {
        return new StreamAdapter(dsWrap, streamProps);
    }
}
