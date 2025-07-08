insert into authors(full_name)
 values ('Author_1'), ('Author_2'), ('Author_3');

 insert into genres(name)
 values ('Genre_1'), ('Genre_2'), ('Genre_3');

 insert into books(title, adult_only, author_id, genre_id)
 values ('BookTitle_1', false, 1, 1),
        ('BookTitle_2', false, 2, 2),
        ('BookTitle_3', false, 3, 3),
        ('Book_for_adults', true, 3, 3);

 insert into comments(text, book_id)
 values ('comment_1 to book_1', 1),
        ('comment_2 to book_1', 1),
        ('comment_1 to book_2', 2),
        ('comment_2 to book_2', 2),
        ('comment_1 to book_3', 3),
        ('comment_2 to book_3', 3),
        ('comment_1 to book_4', 4);

 insert into roles (name)
 values
 ('USER'),
 ('ADMIN'),
 ('KID'),
 ('EDITOR'),
 ('ADULT');

 insert into users (username, password, email, age)
 values
 ('kid_user', '$2a$12$97sRvzFu4n652AhQIHhBbuMaZhBA4JHeY6cZGy81cbvmS0OpVz0GW', 'kid_user@gmail.com', 12),
 ('user', '$2a$12$iVYwxYegApZ7yEsSXRCDZODkaxi2RK5ZvoBxlFqbHhrlQO4UcdAD6', 'user@gmail.com', 32),
 ('admin', '$2a$12$JF2BEYsVk9WhYuSoInGtiO9KtfYw74BcZyyBnqzMVLvGB/vZGmdvm', 'admin@gmail.com', 34);

 insert into users_roles (user_id, role_id)
 values
 (2, 1),
 (3, 2),
 (1, 3),
 (1, 1),
 (3, 4),
 (2, 5),
 (3, 5);

 INSERT INTO acl_sid (principal, sid) VALUES
 (true, 'admin'),
 (true, 'user'),
 (false, 'ROLE_EDITOR'),
 (false, 'ROLE_KID'),
 (false, 'ROLE_ADULT');

 INSERT INTO acl_class (class) VALUES
 ('ru.otus.hw.models.Comment'),
 ('ru.otus.hw.models.Book'),
 ('ru.otus.hw.dto.BookDto'),
 ('ru.otus.hw.dto.BookDtoWithComments');

 INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
 (1, 1, NULL, 3, false),
 (1, 2, NULL, 3, false),
 (1, 3, NULL, 3, false),
 (1, 4, NULL, 3, false),
 (1, 5, NULL, 3, false),
 (1, 6, NULL, 3, false),
 (1, 7, NULL, 3, false),
 (2, 1, NULL, 3, false),
 (2, 2, NULL, 3, false),
 (2, 3, NULL, 3, false),
 (2, 4, NULL, 3, false),
 (3, 1, NULL, 3, false),
 (3, 2, NULL, 3, false),
 (3, 3, NULL, 3, false),
 (3, 4, NULL, 3, false),
 (4, 1, NULL, 3, false),
 (4, 2, NULL, 3, false),
 (4, 3, NULL, 3, false),
 (4, 4, NULL, 3, false);


 INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask,
                        granting, audit_success, audit_failure) VALUES
 (1 , 1, 3, 4, true, true, true),
 (2 , 1, 3, 4, true, true, true),
 (3 , 1, 3, 4, true, true, true),
 (4 , 1, 3, 4, true, true, true),
 (5 , 1, 3, 4, true, true, true),
 (6 , 1, 3, 4, true, true, true),
 (7 , 1, 1, 4, true, true, true),
 (1 , 2, 1, 4, true, true, true),
 (2 , 2, 1, 4, true, true, true),
 (3 , 2, 1, 4, true, true, true),
 (4 , 2, 1, 4, true, true, true),
 (5 , 2, 1, 4, true, true, true),
 (6 , 2, 1, 4, true, true, true),
 (7 , 2, 1, 4, true, true, true),
 (1 , 3, 2, 1, true, true, true),
 (2 , 3, 2, 1, true, true, true),
 (3 , 3, 2, 1, true, true, true),
 (4 , 3, 2, 1, true, true, true),
 (5 , 3, 2, 1, true, true, true),
 (6 , 3, 2, 1, true, true, true),
 (7 , 3, 2, 1, true, true, true),
 (8 , 1, 4, 1, true, true, true),
 (9 , 1, 4, 1, true, true, true),
 (10, 1, 4, 1, true, true, true),
 (11, 1, 4, 1, false, true, true),
  (8, 2, 5, 1, true, true, true),
  (9, 2, 5, 1, true, true, true),
 (10, 2, 5, 1, true, true, true),
 (11, 2, 5, 1, true, true, true),
 (12, 1, 4, 1, true, true, true),
 (13, 1, 4, 1, true, true, true),
 (14, 1, 4, 1, true, true, true),
 (15, 1, 4, 1, false, true, true),
 (12, 2, 5, 1, true, true, true),
 (13, 2, 5, 1, true, true, true),
 (14, 2, 5, 1, true, true, true),
 (15, 2, 5, 1, true, true, true),
 (16, 1, 4, 1, true, true, true),
 (17, 1, 4, 1, true, true, true),
 (18, 1, 4, 1, true, true, true),
 (19, 1, 4, 1, false, true, true),
 (16, 2, 5, 1, true, true, true),
 (17, 2, 5, 1, true, true, true),
 (18, 2, 5, 1, true, true, true),
 (19, 2, 5, 1, true, true, true);
