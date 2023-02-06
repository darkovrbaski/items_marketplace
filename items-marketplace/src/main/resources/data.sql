insert into "user" (id, username, password, first_name, last_name, email, phone, image,
                    wallet_balance, administrator, city, country, number, street)
values (1, 'admin', 'admin', 'admin', 'admin', 'admin@localhost', '123456789', 'admin.png',
        0, true, 'admin', 'admin', 'admin', 'admin'),
       (2, 'user', 'user', 'user', 'user', 'user@localhost', '123456789', 'user.png',
        0, false, 'user', 'user', 'user', 'user');

insert into inventory (user_id)
values (1),
       (2);

insert into article (id, name, description, image, quantity_type)
values (1, 'article1', 'description1', 'https://picsum.photos/250', 'DECIMAL'),
       (2, 'article2', 'description2', 'https://picsum.photos/250', 'NUMERIC'),
       (3, 'article3', 'description3', 'https://picsum.photos/250', 'NUMERIC'),
       (4, 'article4', 'description4', 'https://picsum.photos/250', 'DECIMAL'),
       (5, 'article5', 'description5', 'https://picsum.photos/250', 'DECIMAL');

insert into inventory_article_items (inventory_user_id, article_id, quantity)
values (1, 1, 1),
       (1, 2, 2),
       (1, 3, 3),
       (2, 4, 4),
       (2, 5, 5);