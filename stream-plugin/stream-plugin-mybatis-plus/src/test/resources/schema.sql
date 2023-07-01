drop table if exists user_info;
create table if not exists user_info
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(50) DEFAULT NULL,
    age         INT         DEFAULT NULL,
    email       VARCHAR(50) DEFAULT NULL,
    gmt_deleted DATETIME    DEFAULT '2001-01-01 00:00:00',
    PRIMARY KEY (id)
);

drop table if exists user_role;
create table if not exists user_role
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id BIGINT      DEFAULT NULL,
    role_id VARCHAR(30) DEFAULT NULL,
    PRIMARY KEY (id)
);


drop table if exists role_info;
create table if not exists role_info
(
    id        VARCHAR(30) NOT NULL,
    role_name VARCHAR(30) DEFAULT NULL,
    PRIMARY KEY (id)
);

drop table if exists product_info;
create table if not exists product_info
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    product_name VARCHAR(50) DEFAULT NULL,
    product_price FLOAT DEFAULT NULL,
    tenant_id   VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
    );

drop table if exists product_category;
create table if not exists product_category
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    product_id BIGINT DEFAULT NULL,
    category_id BIGINT DEFAULT NULL,
    tenant_id VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (id)
    );