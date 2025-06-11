package ru.otus.hw;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import ru.otus.hw.models.jpa.Author;
import ru.otus.hw.models.jpa.Book;
import ru.otus.hw.models.jpa.Genre;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.repositories.jpa.AuthorRepository;
import ru.otus.hw.repositories.jpa.BookRepository;
import ru.otus.hw.repositories.jpa.GenreRepository;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SpringBatchTest
public class LibraryMigrationJobTest {

    @Autowired
    private MongoBookRepository mongoBookRepository;

    @Autowired
    private BookRepository jpaBookRepository;

    @Autowired
    private AuthorRepository jpaAuthorRepository;

    @Autowired
    private GenreRepository jpaGenreRepository;

    @Autowired
    private MongoAuthorRepository mongoAuthorRepository;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private MongoGenreRepository mongoGenreRepository;

    private List<MongoBook> mongoBooks;

    private List<MongoAuthor> mongoAuthors;

    private List<MongoGenre> mongoGenres;

    private List<Book> jpaBooks;

    private List<Author> jpaAuthors;

    private List<Genre> jpaGenres;


    @Test
    public void executeMigrationJobAndCheckCompleted() throws Exception {

        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        mongoBooks = mongoBookRepository.findAll();
        mongoAuthors = mongoAuthorRepository.findAll();
        mongoGenres = mongoGenreRepository.findAll();

        jpaBooks = jpaBookRepository.findAll();
        jpaAuthors = jpaAuthorRepository.findAll();
        jpaGenres = jpaGenreRepository.findAll();

        assertEquals(jpaBooks.size(), mongoBooks.size());
        assertThat(jpaBooks)
                .usingRecursiveComparison()
                .comparingOnlyFields(
                        "title",
                        "genre.name",
                        "author.fullName")
                .isEqualTo(mongoBooks);

        assertEquals(jpaAuthors.size(), mongoAuthors.size());
        assertThat(jpaAuthors)
                .usingRecursiveComparison()
                .comparingOnlyFields("fullName")
                .isEqualTo(mongoAuthors);


        assertEquals(jpaGenres.size(), mongoGenres.size());
        assertThat(jpaGenres)
                .usingRecursiveComparison()
                .comparingOnlyFields("name")
                .isEqualTo(mongoGenres);
    }

}
