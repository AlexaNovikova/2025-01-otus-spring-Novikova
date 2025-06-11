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

