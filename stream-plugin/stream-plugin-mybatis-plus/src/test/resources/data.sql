DELETE
FROM user_info;

INSERT INTO user_info (id, name, age, email)
VALUES (1, 'Jone', 18, 'test1@baomidou.com'),
       (2, 'Jack', 18, 'test2@baomidou.com'),
       (3, 'Tom', 28, 'test3@baomidou.com'),
       (4, 'Sandy', 21, 'test4@baomidou.com'),
       (5, 'Billie', 24, 'test5@baomidou.com');

DELETE
FROM user_role;

INSERT INTO user_role (id, user_id, role_id)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 2, 1),
       (4, 2, 3),
       (5, 3, 1),
       (6, 4, 1),
       (7, 4, 2),
       (8, 5, 1),
       (9, 5, 2),
       (10, 5, 3);

DELETE
FROM role_info;

INSERT INTO role_info (id, role_name)
VALUES (1, 'admin'),
       (2, 'user'),
       (3, 'guest');
