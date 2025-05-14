package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@DisplayName("Тестирование реактивного репозиторий на основе MongoDB для работы с книгами ")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    private List<Book> books = List.of(
            new Book("b1", "BookTitle_1",
                    new Author("a1", "Author_1"),
                    new Genre("g1", "Genre_1")),
            new Book("b2", "BookTitle_2",
                    new Author("a2", "Author_2"),
                    new Genre("g2", "Genre_2")),
            new Book("b3", "BookTitle_3",
                    new Author("a3", "Author_3"),
                    new Genre("g3", "Genre_3")));

    private Comment comment = new Comment("c1", "New_Comment", books.get(0));


    @BeforeEach
    public void setUp() {
        books.get(0).setComments(List.of(comment));
        reactiveMongoTemplate.remove(Book.class).all().block();
        bookRepository.saveAll(books).blockLast();
        commentRepository.save(comment).block();
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        StepVerifier.create(bookRepository.findById("b1"))
                .expectNextMatches(book -> book.getTitle().equals("BookTitle_1"))
                .verifyComplete();
    }


    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        StepVerifier.create(bookRepository
                        .findAll(Sort.by(Sort.Direction.ASC, "title")))
                .expectNextMatches(book -> book.getTitle().equals("BookTitle_1"))
                .expectNextMatches(book -> book.getTitle().equals("BookTitle_2"))
                .expectNextMatches(book -> book.getTitle().equals("BookTitle_3"))
                .verifyComplete();
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Mono<Book> bookMono = bookRepository.save(new Book(null, "Book_new",
                new Author("a1", "Author_1"),
                new Genre("g1", "Genre_1")));

        StepVerifier
                .create(bookMono)
                .assertNext(book -> {
                    assertNotNull(book.getId());
                    assertThat(book.getAuthor().getFullName()).isEqualTo("Author_1");
                    assertThat(book.getGenre().getName()).isEqualTo("Genre_1");
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        Mono<Void> result = bookRepository.deleteById("b1");

        Mono<Book> deletedBook = bookRepository.findById("b1");

        StepVerifier
                .create(result)
                .expectComplete()
                .verify();

        StepVerifier
                .create(deletedBook)
                .expectNextCount(0)
                .expectComplete()
                .verify();

    }
}
