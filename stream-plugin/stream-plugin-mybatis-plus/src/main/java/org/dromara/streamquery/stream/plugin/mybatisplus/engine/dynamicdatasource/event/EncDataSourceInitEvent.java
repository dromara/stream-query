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
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.event;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.configuration.DataSourceProperty;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.toolkit.CryptoUtils;
import org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicdatasource.datasource.toolkit.DsStrUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 多数据源默认解密事件
 *
 * @author TaoYu
 */
@Slf4j
public class EncDataSourceInitEvent implements DataSourceInitEvent {

  /** 加密正则 */
  private static final Pattern ENC_PATTERN = Pattern.compile("^ENC\\((.*)\\)$");

  @Override
  public void beforeCreate(DataSourceProperty dataSourceProperty) {
    String publicKey = dataSourceProperty.getPublicKey();
    if (DsStrUtils.hasText(publicKey)) {
      dataSourceProperty.setUrl(decrypt(publicKey, dataSourceProperty.getUrl()));
      dataSourceProperty.setUsername(decrypt(publicKey, dataSourceProperty.getUsername()));
      dataSourceProperty.setPassword(decrypt(publicKey, dataSourceProperty.getPassword()));
    }
  }

  @Override
  public void afterCreate(DataSource dataSource) {}

  /** 字符串解密 */
  private String decrypt(String publicKey, String cipherText) {
    if (DsStrUtils.hasText(cipherText)) {
      Matcher matcher = ENC_PATTERN.matcher(cipherText);
      if (matcher.find()) {
        try {
          return CryptoUtils.decrypt(publicKey, matcher.group(1));
        } catch (Exception e) {
          log.error("DynamicDataSourceProperties.decrypt error ", e);
        }
      }
    }
    return cipherText;
  }
}