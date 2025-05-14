package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;


import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;


@ChangeLog
public class DatabaseChangelog {

    private List<Author> authorList = List.of(
            new Author("a1", "Author_1"),
            new Author("a2", "Author_2"),
            new Author("a3", "Author_3"));

    private List<Genre> genreList = List.of(
            new Genre("g1", "Genre_1"),
            new Genre("g2", "Genre_2"),
            new Genre("g3", "Genre_3"));

    private List<Book> bookList = List.of(
            new Book("b1", "BookTitle_1", authorList.get(0), genreList.get(0)),
            new Book("b2", "BookTitle_2", authorList.get(1), genreList.get(1)),
            new Book("b3", "BookTitle_3", authorList.get(2), genreList.get(2)));

    private List<Comment> commentList = List.of(
            new Comment("c1", "Comment_1", bookList.get(0)),
            new Comment("c2", "Comment_2", bookList.get(0)),
            new Comment("c3", "Comment_3", bookList.get(0)),
            new Comment("c4", "Comment_4", bookList.get(1)),
            new Comment("c5", "Comment_5", bookList.get(1)),
            new Comment("c6", "Comment_6", bookList.get(1)),
            new Comment("c7", "Comment_7", bookList.get(2)),
            new Comment("c8", "Comment_8", bookList.get(2)));

    @ChangeSet(order = "001", id = "dropDb", author = "alexaNovikova", runAlways = true)
    public void dropDb(MongoDatabase mongoDatabase) {
        mongoDatabase.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "alexaNovikova")
    public void insertAuthors(MongockTemplate mongockTemplate) {
        mongockTemplate.insertAll(authorList);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "alexaNovikova")
    public void insertGenres(MongockTemplate mongockTemplate) {
        mongockTemplate.insertAll(genreList);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "alexaNovikova")
    public void insertBooks(MongockTemplate mongockTemplate) {
        mongockTemplate.insertAll(bookList);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "alexaNovikova")
    public void insertComments(MongockTemplate mongockTemplate) {
        mongockTemplate.insertAll(commentList);
        bookList.get(0).setComments(List.of(commentList.get(0), commentList.get(1), commentList.get(2)));
        bookList.get(1).setComments(List.of(commentList.get(3), commentList.get(4), commentList.get(5)));
        bookList.get(2).setComments(List.of(commentList.get(6), commentList.get(7)));
        mongockTemplate.save(bookList.get(0));
        mongockTemplate.save(bookList.get(1));
        mongockTemplate.save(bookList.get(2));
    }
}
