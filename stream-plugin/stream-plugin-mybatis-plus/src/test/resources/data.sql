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
