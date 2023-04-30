insert into "user" (id, username, password, first_name, last_name, email, phone, image, city,
                    country, number, street, role)
values (1, 'admin', '$2a$10$sQmerVLYB7V0OsgmnY6OLuPRNujaCLWvihgy2NHjUMIlRGjKkLlFC', 'admin',
        'admin', 'admin@localhost', '123456789', 'admin_205e460b479e2e5b48aec07710c08d50.png',
        'admin', 'admin', 'admin', 'admin', 'ROLE_ADMIN'),
       (2, 'user', '$2a$10$S5YmghHbNLRVbit6hfbmzOpqkNkX2rrKmyuoIxwJ5EyBlrDFVlqiW', 'user', 'user',
        'user@localhost', '123456789', 'user_65-250x250.jpg', 'user', 'user', 'user', 'user',
        'ROLE_USER');

alter sequence user_id_seq restart with 3;

insert into wallet (user_id, amount, currency)
values (1, 100, 'USD'),
       (2, 200, 'USD');

insert into inventory (user_id)
values (1),
       (2);

insert into article (id, name, description, image)
values (1, 'article1', 'description1', '34x34icons.png'),
       (2, 'article2', 'description2', '34x34icons.png'),
       (3, 'article3', 'description3', '34x34icons.png'),
       (4, 'article4', 'description4', '34x34icons.png'),
       (5, 'article5', 'description5', '34x34icons.png');

alter sequence article_id_seq restart with 6;

insert into inventory_article_items (inventory_user_id, article_id, quantity)
values (1, 1, 1),
       (1, 2, 2),
       (1, 3, 3),
       (2, 4, 4),
       (2, 5, 5);