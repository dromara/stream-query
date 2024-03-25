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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.tx;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.DynamicRoutingDataSource;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

/**
 * Atomikos事务适配-多数据源切换
 *
 * @author <a href="mailto:312290710@qq.com">jiazhifeng</a>
 */
public class AtomikosTransactionFactory extends SpringManagedTransactionFactory {

  @Override
  public Transaction newTransaction(
      DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
    DataSource determineDataSource = dataSource;

    if (dataSource instanceof DynamicRoutingDataSource) {
      determineDataSource = ((DynamicRoutingDataSource) dataSource).determineDataSource();
    }
    return new SpringManagedTransaction(determineDataSource);
  }
}