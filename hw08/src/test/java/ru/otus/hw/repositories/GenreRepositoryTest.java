package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с жанрами ")
@DataMongoTest
class GenreRepositoryTest {

    private static final int EXPECTED_GENRES_COUNT = 3;

    private static final String FIRST_GENRE_ID = "g1";

    private static final String THIRD_GENRE_ID = "g3";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var genres = genreRepository.findAll();
        var genresExpected = mongoOperations.findAll(Genre.class);

        assertThat(genres).usingRecursiveComparison().isEqualTo(genresExpected);
        assertThat(genres).isNotNull().hasSize(EXPECTED_GENRES_COUNT)
                .allMatch(genre -> !genre.getName().isEmpty());

    }
}