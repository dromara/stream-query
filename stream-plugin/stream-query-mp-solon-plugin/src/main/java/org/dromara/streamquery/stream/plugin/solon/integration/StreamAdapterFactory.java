package org.dromara.streamquery.stream.plugin.solon.integration;

import com.baomidou.mybatisplus.solon.integration.MybatisAdapterFactoryPlus;
import org.apache.ibatis.solon.MybatisAdapter;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Props;





/**
 * @author Cason
 */
public class StreamAdapterFactory extends MybatisAdapterFactoryPlus {
    @Override
    public MybatisAdapter create(BeanWrap dsWrap) {
        return new StreamAdapterMp(dsWrap);
    }

    @Override
    public MybatisAdapter create(BeanWrap dsWrap, Props streamProps) {
        return new StreamAdapterMp(dsWrap, streamProps);
    }
}
