DELETE
FROM user_info;

INSERT INTO user_info (name, age, email)
VALUES ('Jon', 18, 'myEmail1'),
       ('Jack', 18, 'myEmail2'),
       ('Tom', 28, 'myEmail3'),
       ('Sandy', 21, 'myEmail4'),
       ('Billie', 24, 'myEmail5');

DELETE
FROM user_role;

INSERT INTO user_role (user_id, role_id)
VALUES (1, '1'),
       (1, '2'),
       (2, '1'),
       (2, '3'),
       (3, '1'),
       (4, '1'),
       (4, '2'),
       (5, '1'),
       (5, '2'),
       (5, '3');

DELETE
FROM role_info;

INSERT INTO role_info (id, role_name)
VALUES ('1', 'admin'),
       ('2', 'user'),
       ('3', 'guest');

DELETE FROM product_info;

INSERT INTO product_info (product_name, product_price, tenant_id)
VALUES ('Apple iPhone 13', 699, '1'),
       ('Samsung Galaxy S21', 799, '1'),
       ('OnePlus 9 Pro', 969, '1'),
       ('Google Pixel 6', 599, '2'),
       ('Sony Xperia 1 III', 1199, '1');

DELETE FROM product_category;

INSERT INTO product_category (product_id, category_id, tenant_id)
VALUES (1, 1, '1'),
       (1, 2, '1'),
       (2, 1, '1'),
       (2, 3, '1'),
       (3, 1, '1'),
       (4, 1, '2'),
       (4, 2, '2'),
       (5, 1, '1'),
       (5, 2, '1'),
       (5, 3, '1');