/*Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.*/

DROP TABLE IF EXISTS "user_info";
CREATE TABLE IF NOT EXISTS "user_info"
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) DEFAULT NULL,
    age         INT         DEFAULT NULL,
    email       VARCHAR(255) DEFAULT NULL,
    gmt_deleted TIMESTAMP    DEFAULT '2001-01-01 00:00:00'
    );

DROP TABLE IF EXISTS "user_role";
CREATE TABLE IF NOT EXISTS "user_role"
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT      DEFAULT NULL,
    role_id VARCHAR(30) DEFAULT NULL
    );

DROP TABLE IF EXISTS "role_info";
CREATE TABLE IF NOT EXISTS "role_info"
(
    id        VARCHAR(30) PRIMARY KEY,
    role_name VARCHAR(30) DEFAULT NULL
    );

DROP TABLE IF EXISTS "product_info";
CREATE TABLE IF NOT EXISTS "product_info"
(
    id            BIGSERIAL PRIMARY KEY,
    product_name  VARCHAR(255) DEFAULT NULL,
    product_price FLOAT8 DEFAULT NULL,
    tenant_id     VARCHAR(255) DEFAULT NULL
    );

DROP TABLE IF EXISTS "product_category";
CREATE TABLE IF NOT EXISTS "product_category"
(
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGINT DEFAULT NULL,
    category_id BIGINT DEFAULT NULL,
    tenant_id   VARCHAR(255) DEFAULT NULL
    );
