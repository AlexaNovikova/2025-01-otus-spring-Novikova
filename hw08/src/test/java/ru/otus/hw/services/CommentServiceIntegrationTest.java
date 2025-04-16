package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.CommentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Интеграционный тест сервиса по работе с комментариями к книгам")
@SpringBootTest
public class CommentServiceIntegrationTest {

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName(" должен загружать комментарий по id")
    @Test
    void shouldFindCommentById() {
        var optionalActualComment = commentService.findById("c1");
        assertThat(optionalActualComment).isPresent()
                .get()
                .matches(c -> c.getText().equals("Comment_1"))
                .matches(c -> c.getBookId().equals("b1"));
    }


    @DisplayName(" должен возвращать пустой Optional, если комментарий по id не найден ")
    @Test
    void shouldReturnEmptyOptionalWhenCommentIsNotFoundById() {
        var optionalActualComment = commentService.findById("c10");
        assertThat(optionalActualComment).isEmpty();
    }

    @DisplayName(" должен загружать список комментариев по id книги")
    @Test
    void shouldFindCommentListForBookId() {
        var comments = commentService.findByBookId("b1");

        var expectedComments = List.of(
                new CommentDto("c1", "Comment_1", "b1"),
                new CommentDto("c2", "Comment_2", "b1"),
                new CommentDto("c3", "Comment_3", "b1")
        );
        assertThat(comments.size()).isEqualTo(expectedComments.size());
        assertThat(comments).containsExactlyElementsOf(expectedComments);
    }

    @DisplayName(" должен сохранить новый комментарий к указанной книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldSaveNewCommentToBook() {
        var savedComment = commentService.save(new CommentDto("c10", "NewComment", "b1"));
        var id = savedComment.getId();
        assertThat(id).isNotEmpty();
        var optionalCommentFromDB = commentService.findById(id);
        assertThat(optionalCommentFromDB).isPresent()
                .get().isEqualTo(savedComment);
    }

    @DisplayName(" должен удалять комментарий по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldDeleteCommentById() {
        commentService.deleteById("c1");
        assertThat(commentService.findById("c1")).isEmpty();
    }
}
