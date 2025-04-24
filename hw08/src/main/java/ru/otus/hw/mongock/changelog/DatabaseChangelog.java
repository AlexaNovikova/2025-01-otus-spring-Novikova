package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import java.util.List;


@ChangeLog
public class DatabaseChangelog {

    private List<Author> authorList = List.of();

    private List<Genre> genreList = List.of();

    private List<Book> bookList = List.of();

    private List<Comment> commentList = List.of();

    @ChangeSet(order = "001", id = "dropDb", author = "alexaNovikova", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "alexaNovikova")
    public void insertAuthors(AuthorRepository authorRepository) {
        var authors = List.of(
                new Author("a1", "Author_1"),
                new Author("a2", "Author_2"),
                new Author("a3", "Author_3"));

        authorList = authorRepository.saveAll(authors);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "alexaNovikova")
    public void insertGenres(GenreRepository genreRepository) {
        var genres = List.of(
                new Genre("g1", "Genre_1"),
                new Genre("g2", "Genre_2"),
                new Genre("g3", "Genre_3"));

        genreList = genreRepository.saveAll(genres);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "alexaNovikova")
    public void insertBooks(BookRepository bookRepository) {
        var authorOne = authorList.get(0);
        var authorTwo = authorList.get(1);
        var authorThree = authorList.get(2);

        var genreOne = genreList.get(0);
        var genreTwo = genreList.get(1);
        var genreThree = genreList.get(2);

        var books = List.of(
                new Book("b1", "BookTitle_1", authorOne, genreOne),
                new Book("b2", "BookTitle_2", authorTwo, genreTwo),
                new Book("b3", "BookTitle_3", authorThree, genreThree));

        bookList = bookRepository.saveAll(books);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "alexaNovikova")
    public void insertComments(BookRepository bookRepository, CommentRepository commentRepository) {
        var bookOne = bookList.get(0);
        var bookTwo = bookList.get(1);
        var bookThree = bookList.get(2);

        var comments = List.of(
                new Comment("c1", "Comment_1", bookOne),
                new Comment("c2", "Comment_2", bookOne),
                new Comment("c3", "Comment_3", bookOne),
                new Comment("c4", "Comment_4", bookTwo),
                new Comment("c5", "Comment_5", bookTwo),
                new Comment("c6", "Comment_6", bookTwo),
                new Comment("c7", "Comment_7", bookThree),
                new Comment("c8", "Comment_8", bookThree));

        commentList = commentRepository.saveAll(comments);

        bookOne.setComments(List.of(commentList.get(0), commentList.get(1), commentList.get(2)));
        bookTwo.setComments(List.of(commentList.get(3), commentList.get(4), commentList.get(5)));
        bookThree.setComments(List.of(commentList.get(6), commentList.get(7)));
        bookRepository.save(bookOne);
        bookRepository.save(bookTwo);
        bookRepository.save(bookThree);
    }

}
