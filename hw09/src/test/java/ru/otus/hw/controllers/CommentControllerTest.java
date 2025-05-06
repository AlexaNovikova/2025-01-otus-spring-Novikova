package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.CommentController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Тестирование контроллера по работе со страницами для отображения комментариев")
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @DisplayName(" должен сохранять новый комментарий к указанной книге" +
            " и перенаправлять на страницу с книгой")
    @Test
    void shouldSaveNewCommentAndRedirectToContextPath() throws Exception {
        mockMvc.perform(post("/comment/save")
                        .param("id", "1")
                        .param("text", "new comment"))
                .andExpect(view().name("redirect:/bookWithComments?id=1"));
        verify(commentService, times(1)).save(any(CommentDto.class));
    }

    @DisplayName(" должен удалять комментарий по id" +
            " и перенаправлять на страницу с книгой")
    @Test
    void shouldDeleteCommentByIdAndRedirectToContextPath() throws Exception {
        when(commentService.findById(1L)).thenReturn(
                Optional.of(new CommentDto(1L, "comment_1 to book_1", 1L)));
        mockMvc.perform(get("/comment/delete")
                        .param("id", "1")
                        .param("bookId", "1"))
                .andExpect(view().name("redirect:/bookWithComments?id=1"));
        verify(commentService, times(1)).deleteById(1L);
    }
}
