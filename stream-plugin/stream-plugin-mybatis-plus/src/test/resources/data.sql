DELETE
FROM user_info;

INSERT INTO user_info (name, age, email)
VALUES ('Jone', 18, 'test1@baomidou.com'),
       ('Jack', 18, 'test2@baomidou.com'),
       ('Tom', 28, 'test3@baomidou.com'),
       ('Sandy', 21, 'test4@baomidou.com'),
       ('Billie', 24, 'test5@baomidou.com');

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
