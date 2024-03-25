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
