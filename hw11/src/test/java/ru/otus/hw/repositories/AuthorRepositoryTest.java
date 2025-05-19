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

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@DisplayName("Тестирование реактивного репозитория на основе MongoDB для работы с авторами ")
@DataMongoTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    private final List<Author> authorsList = List.of(
            new Author("a1", "Author_1"),
            new Author("a2", "Author_2"),
            new Author("a3", "Author_3"));

    @BeforeEach
    public void setUp() {
        reactiveMongoTemplate.remove(Author.class).all().block();
        authorRepository.saveAll(authorsList).blockLast();
    }


    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        StepVerifier.create(authorRepository.findById("a1"))
                .expectNextMatches(author -> author.getFullName().equals("Author_1"))
                .verifyComplete();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorList() {
        StepVerifier.create(authorRepository
                        .findAll(Sort.by(Sort.Direction.ASC, "fullName")))
                .expectNextMatches(author -> author.getFullName().equals("Author_1"))
                .expectNextMatches(author -> author.getFullName().equals("Author_2"))
                .expectNextMatches(author -> author.getFullName().equals("Author_3"))
                .verifyComplete();
    }

    @DisplayName("должен сохранять нового автора")
    @Test
    void shouldCorrectSaveNewAuthor() {
        Mono<Author> authorMono = authorRepository.save(new Author(null, "Author_new"));

        StepVerifier
                .create(authorMono)
                .assertNext(author -> assertNotNull(author.getId()))
                .expectComplete()
                .verify();
    }
}