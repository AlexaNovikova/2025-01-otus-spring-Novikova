insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('comment_1 to book_1', 1),
       ('comment_2 to book_1', 1),
       ('comment_1 to book_2', 2),
       ('comment_2 to book_2', 2),
       ('comment_1 to book_3', 3),
       ('comment_2 to book_3', 3);

insert into roles (name)
values
('ROLE_USER'),
('ROLE_ADMIN');

insert into users (username, password, email)
values
('user', '$2a$12$iVYwxYegApZ7yEsSXRCDZODkaxi2RK5ZvoBxlFqbHhrlQO4UcdAD6', 'user@gmail.com'),
('admin', '$2a$12$JF2BEYsVk9WhYuSoInGtiO9KtfYw74BcZyyBnqzMVLvGB/vZGmdvm', 'admin@gmail.com');

insert into users_roles (user_id, role_id)
values
(1, 1),
(2, 2);