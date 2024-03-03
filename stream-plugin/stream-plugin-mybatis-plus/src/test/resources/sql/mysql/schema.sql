DROP TABLE IF EXISTS user_info;
CREATE TABLE IF NOT EXISTS user_info
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name        VARCHAR(50) DEFAULT NULL,
    age         INT         DEFAULT NULL,
    email       VARCHAR(50) DEFAULT NULL,
    gmt_deleted DATETIME    DEFAULT '2001-01-01 00:00:00'
    );

DROP TABLE IF EXISTS user_role;
CREATE TABLE IF NOT EXISTS user_role
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    user_id BIGINT      DEFAULT NULL,
    role_id VARCHAR(30) DEFAULT NULL
    );

DROP TABLE IF EXISTS role_info;
CREATE TABLE IF NOT EXISTS role_info
(
    id        VARCHAR(30) NOT NULL,
    role_name VARCHAR(30) DEFAULT NULL,
    PRIMARY KEY (id)
    );

DROP TABLE IF EXISTS product_info;
CREATE TABLE IF NOT EXISTS product_info
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    product_name  VARCHAR(50) DEFAULT NULL,
    product_price FLOAT DEFAULT NULL,
    tenant_id     VARCHAR(50) DEFAULT NULL
    );

DROP TABLE IF EXISTS product_category;
CREATE TABLE IF NOT EXISTS product_category
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    product_id  BIGINT DEFAULT NULL,
    category_id BIGINT DEFAULT NULL,
    tenant_id   VARCHAR(50) DEFAULT NULL
    );
