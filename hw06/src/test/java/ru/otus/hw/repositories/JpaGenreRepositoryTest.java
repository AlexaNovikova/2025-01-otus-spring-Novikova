package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование репозитория на основе Jpa для работы с жанрами")
@DataJpaTest
@Import(JpaGenreRepository.class)
public class JpaGenreRepositoryTest {

    private final static long GENRE_ID = 1L;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName(" должен загружать список всех жанров")
    @Test
    void shouldFindAllGenres() {
        var actualGenresList = jpaGenreRepository.findAll();
        var expectedGenresList = testEntityManager
                .getEntityManager()
                .createQuery("Select g from Genre g", Genre.class)
                .getResultList();
        assertThat(actualGenresList).containsExactlyElementsOf(expectedGenresList);
    }

    @DisplayName(" должен находить жанр по id")
    @Test
    void shouldFindGenreById() {
        var optionalActualGenre = jpaGenreRepository.findById(GENRE_ID);
        var expectedGenre = testEntityManager.find(Genre.class, GENRE_ID);
        assertThat(optionalActualGenre).isPresent().get()
                .isEqualTo(expectedGenre);
    }
}
