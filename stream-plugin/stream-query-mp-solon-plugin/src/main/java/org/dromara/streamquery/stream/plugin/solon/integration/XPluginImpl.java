package org.dromara.streamquery.stream.plugin.solon.integration;


import org.apache.ibatis.solon.integration.MybatisAdapterManager;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

/**
 * @author Cason
 */
public class XPluginImpl implements Plugin {
  @Override
  public void start(AopContext context) throws Throwable {
      MybatisAdapterManager.setAdapterFactory(new StreamAdapterFactory());

  }
}
