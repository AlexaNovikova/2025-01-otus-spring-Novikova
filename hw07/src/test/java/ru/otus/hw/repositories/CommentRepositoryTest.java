package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Тестирование репозитория на основе Jpa для работы с комментариями к книгам")
@DataJpaTest
public class CommentRepositoryTest {

    private static final long COMMENT_ID = 1L;
    private static final long BOOK_ID = 2L;
    private static final long COMMENT_ID_TO_DELETE = 1L;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName(" должен загружать информацию о нужном комментарии по id")
    @Test
    void shouldFindExpectedCommentById() {
        var optionalActualComment = commentRepository.findById(COMMENT_ID);
        var expectedComment = testEntityManager.find(Comment.class, COMMENT_ID);
        assertThat(optionalActualComment).isPresent()
                .get().isEqualTo(expectedComment);
    }

    @DisplayName(" должен загружать информацию о комментариях по id книги")
    @Test
    void shouldFindExpectedCommentListByBookId() {
        var optionalActualCommentsList = commentRepository.findByBookId(BOOK_ID);
        TypedQuery<Comment> commentTypedQuery = testEntityManager
                .getEntityManager()
                .createQuery("Select c from Comment c where c.book.id = :bookId", Comment.class);
        commentTypedQuery.setParameter("bookId", BOOK_ID);
        var expectedComment = commentTypedQuery.getResultList();
        assertThat(optionalActualCommentsList)
                .containsExactlyElementsOf(expectedComment);
    }

    @DisplayName(" должен удалять комментарий по id")
    @Test
    void shouldDeleteCommentById() {
        commentRepository.deleteById(COMMENT_ID_TO_DELETE);
        assertThat(testEntityManager.find(Comment.class, COMMENT_ID_TO_DELETE))
                .isEqualTo(null);
    }

    @DisplayName(" должен сохранять новый комментарий к книге с указанным id")
    @Test
    void shouldSaveNewCommentToBook() {
        Book book = testEntityManager.find(Book.class, BOOK_ID);
        Comment comment = Comment.builder().id(0).text("New test comment").book(book).build();
        Comment saved = commentRepository.save(comment);
        assertThat(testEntityManager.find(Comment.class, saved.getId())).isEqualTo(saved);
    }
}
