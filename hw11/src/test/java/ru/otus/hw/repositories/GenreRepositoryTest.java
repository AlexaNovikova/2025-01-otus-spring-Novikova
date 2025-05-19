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
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@DisplayName("Тестирование реактивного репозитория на основе MongoDB для работы с жанрами ")
@DataMongoTest
class GenreRepositoryTest {

    private List<Genre> genres = List.of(
            new Genre("g1", "Genre_1"),
            new Genre("g2", "Genre_2"),
            new Genre("g3", "Genre_3"));

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    public void setUp() {
        reactiveMongoTemplate.remove(Genre.class).all().block();
        genreRepository.saveAll(genres).blockLast();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        StepVerifier.create(genreRepository
                        .findAll(Sort.by(Sort.Direction.ASC, "name")))
                .expectNextMatches(genre -> genre.getName().equals("Genre_1"))
                .expectNextMatches(genre -> genre.getName().equals("Genre_2"))
                .expectNextMatches(genre -> genre.getName().equals("Genre_3"))
                .verifyComplete();
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {
        StepVerifier.create(genreRepository.findById("g1"))
                .expectNextMatches(genre -> genre.getName().equals("Genre_1"))
                .verifyComplete();
    }

    @DisplayName("должен сохранять новый жанр")
    @Test
    void shouldCorrectSaveNewGenre() {
        Mono<Genre> genreMono = genreRepository.save(new Genre(null, "Genre_New"));

        StepVerifier
                .create(genreMono)
                .assertNext(genre -> assertNotNull(genre.getId()))
                .expectComplete()
                .verify();
    }
}