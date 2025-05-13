package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@DisplayName("Тестирование реактивного репозиторий на основе MongoDB для работы с комментариями ")
@DataMongoTest
class CommentRepositoryTest {

    private Book book = new Book("b1", "Book_1",
            new Author("a1", "Author_1"),
            new Genre("g1", "Genre_1"));

    private List<Comment> comments = List.of(
            new Comment("c1", "Comment_1", book),
            new Comment("c2", "Comment_2", book),
            new Comment("c3", "Comment_3", book));

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    public void setUp() {
        reactiveMongoTemplate.remove(Comment.class).all().block();
        commentRepository.saveAll(comments).blockLast();
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsListForBookId() {
        StepVerifier.create(commentRepository
                        .findByBookId("b1"))
                .expectNextCount(3)
                .verifyComplete();
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldCorrectSaveNewGenre() {
        Mono<Comment> newComment = commentRepository.save(new Comment(null, "Comment_new", book));

        StepVerifier
                .create(newComment)
                .assertNext(comment -> assertNotNull(comment.getId()))
                .expectComplete()
                .verify();
    }
}