insert into "user" (id, username, password, first_name, last_name, email, phone, image, city,
                    country, number, street)
values (1, 'admin', 'admin', 'admin', 'admin', 'admin@localhost', '123456789',
        'https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50', 'admin', 'admin',
        'admin', 'admin'),
       (2, 'user', 'user', 'user', 'user', 'user@localhost', '123456789',
        'https://picsum.photos/id/65/250', 'user', 'user', 'user', 'user');

alter sequence user_id_seq restart with 3;

insert into wallet (user_id, amount, currency)
values (1, 100, 'USD'),
       (2, 200, 'USD');

insert into inventory (user_id)
values (1),
       (2);

insert into article (id, name, description, image)
values (1, 'article1', 'description1', 'https://picsum.photos/250'),
       (2, 'article2', 'description2', 'https://picsum.photos/250'),
       (3, 'article3', 'description3', 'https://picsum.photos/250'),
       (4, 'article4', 'description4', 'https://picsum.photos/250'),
       (5, 'article5', 'description5', 'https://picsum.photos/250');

alter sequence article_id_seq restart with 6;

insert into inventory_article_items (inventory_user_id, article_id, quantity)
values (1, 1, 1),
       (1, 2, 2),
       (1, 3, 3),
       (2, 4, 4),
       (2, 5, 5);