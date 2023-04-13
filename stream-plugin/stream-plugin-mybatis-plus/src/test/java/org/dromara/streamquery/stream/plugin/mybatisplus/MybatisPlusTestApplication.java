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
package org.dromara.streamquery.stream.plugin.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.annotation.EnableMybatisPlusPlugin;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.RoleInfo;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.UserInfo;
import org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po.UserRole;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * MybatisPlusTestApplication less Create Retrieve Update Delete
 *
 * @author VampireAchao Cizai_
 * @since 2022/5/21
 */
@EnableMybatisPlusPlugin(
    value = "org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po",
    basePackages = "org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po",
    basePackageClasses = {RoleInfo.class, UserInfo.class, UserRole.class})
@SpringBootApplication
public class MybatisPlusTestApplication {
  /**
   * mybatisPlusInterceptor.
   *
   * @return a {@link com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor} object
   */
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    return interceptor;
  }

  //  @Bean
  //  public DynamicMapperHandler dynamicMapperHandler(SqlSessionFactory sqlSessionFactory)
  //      throws Exception {
  //    /// 扫描po包下的所有类，作为entity
  //    String entityPackagePath = "org.dromara.streamquery.stream.plugin.mybatisplus.pojo.po";
  //    final List<Class<?>> entityClassList = ClassHelper.scanClasses(entityPackagePath);
  //    return new DynamicMapperHandler(sqlSessionFactory, entityClassList);
  //  }
}
