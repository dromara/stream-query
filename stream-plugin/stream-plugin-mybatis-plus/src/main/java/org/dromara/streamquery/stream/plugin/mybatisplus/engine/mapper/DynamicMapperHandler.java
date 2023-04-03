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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.mapper;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.streamquery.stream.plugin.mybatisplus.Database;

import java.util.Collection;

/**
 * DynamicMapperHandler
 *
 * @author VampireAchao
 * @since 2023/1/8
 */
public class DynamicMapperHandler {

    public DynamicMapperHandler(SqlSessionFactory sqlSessionFactory, Collection<Class<?>> entityClassList) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        if (configuration instanceof MybatisConfiguration) {
            MybatisConfiguration mybatisConfiguration = (MybatisConfiguration) configuration;
            entityClassList.forEach(entityClass -> Database.buildMapper(mybatisConfiguration, entityClass));
        }
    }
}
