/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.streamquery.stream.plugin.solon.integration;
/**
 * @ClassName XPluginImpl.java
 *
 * @author 任士博
 * @version 1.0.0 @Description TODO
 * @createTime 2023年08月02日 16:53:00
 */
import com.baomidou.mybatisplus.solon.integration.MybatisAdapterFactoryPlus;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.solon.integration.MybatisAdapterManager;
import org.dromara.streamquery.stream.plugin.solon.scanner.StreamScanConfig;
import org.noear.solon.Solon;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

import javax.sql.DataSource;

/** @author 黄清城 */
public class XPluginImpl implements Plugin {
  @Override
  public void start(AopContext context) throws Throwable {
      MybatisAdapterManager.setAdapterFactory(new StreamAdapterFactory());

  }
}
