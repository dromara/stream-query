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
package org.dromara.streamquery.stream.plugin.solon.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import java.io.IOException;
import java.sql.SQLException;

@Configuration
public class Config {
  @Bean(value = "db1", typed = true)
  public DataSource db1(@Inject("${test.db1}") HikariDataSource ds) {
    return ds;
  }

  @Bean
  public void sqlInit(@Inject("db1") DataSource dataSource) throws IOException, SQLException {
    // 初始化表
    exeSqlFile(dataSource, "schema.sql");
    // 初始化数据
    exeSqlFile(dataSource, "data.sql");
  }

  private void exeSqlFile(DataSource dataSource, String filePath) throws IOException, SQLException {
    ScriptRunner scriptRunner = new ScriptRunner(dataSource.getConnection());

    scriptRunner.runScript(Resources.getResourceAsReader(filePath));
  }
}
