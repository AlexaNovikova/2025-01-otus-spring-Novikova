package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование репозитрия на основе jpa по работе с книгами")
@DataJpaTest
public class BookRepositoryTest {

    private static final long BOOK_ID = 1L;
    private static final long AUTHOR_ID = 1L;
    private static final long AUTHOR_ID_NEW = 2L;
    private static final long GENRE_ID = 1L;
    private static final long BOOK_ID_TO_DELETE = 2L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @DisplayName(" должен загружать информацию о нужной книге по id")
    @Test
    void shouldFindExpectedBookById() {
        var optionalActualBook = bookRepository.findById(BOOK_ID);
        var expectedBook = testEntityManager.find(Book.class, BOOK_ID);
        assertThat(optionalActualBook).isPresent()
                .get().isEqualTo(expectedBook);
    }

    @DisplayName(" должен загружать список всех книг")
    @Test
    void shouldFindAllBooks() {
        var actualBooksList = bookRepository.findAll();
        var expectedBooksList = testEntityManager
                .getEntityManager()
                .createQuery("Select b from Book b", Book.class)
                .getResultList();
        assertThat(actualBooksList).containsExactlyElementsOf(expectedBooksList);
    }

    @DisplayName(" должен удалять книгу по id вместе с комментариями")
    @Test
    void shouldDeleteBookById() {
        bookRepository.deleteById(BOOK_ID_TO_DELETE);
        assertThat(testEntityManager.find(Book.class, BOOK_ID_TO_DELETE))
                .isEqualTo(null);
        assertThat(testEntityManager.getEntityManager()
                .createQuery("Select c from Comment c where c.book.id = :bookId")
                .setParameter("bookId", BOOK_ID_TO_DELETE)
                .getResultList()).isEqualTo(Collections.EMPTY_LIST);
    }

    @DisplayName(" должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Author author = testEntityManager.find(Author.class, AUTHOR_ID);
        Genre genre = testEntityManager.find(Genre.class, GENRE_ID);
        Book book = Book.builder().id(0L).title("NewTestBook").adultOnly(false)
                .author(author).genre(genre).build();
        Book saved = bookRepository.save(book);
        assertThat(saved.getId()).isNotZero();
        assertThat(testEntityManager.find(Book.class, saved.getId())).isEqualTo(saved);
    }

    @DisplayName(" должен изменять книгу с указанным id")
    @Test
    void shouldUpdateBookById() {
        Author author = testEntityManager.find(Author.class, AUTHOR_ID_NEW);
        Book bookFromDB = testEntityManager.find(Book.class, BOOK_ID);
        Book updatedBook = bookFromDB.toBuilder().title("Updated").author(author).build();
        Book savedBook = bookRepository.save(updatedBook);
        assertThat(savedBook.getId()).isEqualTo(updatedBook.getId());
        assertThat(testEntityManager.find(Book.class, savedBook.getId())).isEqualTo(savedBook);
    }
}
