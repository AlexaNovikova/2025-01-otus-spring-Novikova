package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование репозитрия на основе jpa по работе с авторами")
@DataJpaTest
@Import(JpaAuthorRepository.class)
public class JpaAuthorRepositoryTest {

    private static final long AUTHOR_ID = 1L;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName(" должен загружать автора по id")
    @Test
    void shouldFindAuthorById(){
        var optionalActualAuthor = jpaAuthorRepository.findById(AUTHOR_ID);
        var expectedAuthor = testEntityManager.find(Author.class, AUTHOR_ID);
        assertThat(optionalActualAuthor)
                .isPresent().get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName(" должен загружать список всех авторов")
    @Test
    void shouldFindAllAuthors(){
        var actualAuthorsList = jpaAuthorRepository.findAll();
        var expectedAuthorsList = testEntityManager
                .getEntityManager()
                .createQuery("Select a from Author a", Author.class)
                .getResultList();
        assertThat(actualAuthorsList).containsExactlyElementsOf(expectedAuthorsList);
    }

}
