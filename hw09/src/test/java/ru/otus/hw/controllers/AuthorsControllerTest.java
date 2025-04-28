package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.AuthorController;
import ru.otus.hw.controller.NotFoundException;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Тестирование контроллера по работе со страницами для отображения авторов")
@WebMvcTest(AuthorController.class)
public class AuthorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    private List<AuthorDto> authorsList = List.of(
            new AuthorDto(1L, "Author_1"),
            new AuthorDto(2L, "Author_2"),
            new AuthorDto(3L, "Author_3"));

    @DisplayName(" должен возвращать страницу со всеми авторами")
    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        when(authorService.findAll()).thenReturn(authorsList);
        mockMvc.perform(get("/authors"))
                .andExpect(view().name("authors"))
                .andExpect(model().attribute("authors", authorsList));
    }

    @DisplayName(" должен возвращать страницу с указанным по id автором для редактирования")
    @Test
    void shouldRenderEditPageWithCorrectViewAndModelAttributes() throws Exception {
        when(authorService.findById(1L)).thenReturn(Optional.of(authorsList.get(0)));

        mockMvc.perform(get("/author/edit").param("id", "1"))
                .andExpect(view().name("editOrNewAuthorPage"))
                .andExpect(model().attribute("author", authorsList.get(0)));
    }

    @DisplayName(" должен возвращать NotFoundException при указании несуществующего id автора")
    @Test
    void shouldRenderErrorPageWhenAuthorNotFound() throws Exception {
        when(authorService.findById(1L)).thenThrow(new NotFoundException());
        mockMvc.perform(get("/author/edit").param("id", "1"))
                .andExpect(view().name("customError"));
    }

    @DisplayName(" должен сохранять отредактированного автора" +
            " и перенаправлять на страницу со всеми авторами")
    @Test
    void shouldSaveEditedAuthorAndRedirectToContextPath() throws Exception {
        mockMvc.perform(post("/author/editOrSaveNew")
                        .flashAttr("author",
                                new AuthorDto(4L, "Author_4")))
                .andExpect(view().name("redirect:/authors"));
        verify(authorService, times(1)).save(any(AuthorDto.class));
    }

    @DisplayName(" должен сохранять нового автора" +
            " и перенаправлять на страницу со всеми авторами")
    @Test
    void shouldSaveNewAuthorAndRedirectToContextPath() throws Exception {
        mockMvc.perform(post("/author/editOrSaveNew")
                        .flashAttr("author",
                              new AuthorDto(null, "Author_5")))
                .andExpect(view().name("redirect:/authors"));
        verify(authorService, times(1)).save(any(AuthorDto.class));
    }

    @DisplayName(" должен удалять автора с указанным Id и перенаправлять на страницу со всеми авторами")
    @Test
    void shouldDeleteAuthorByIdAndRedirectToContextPath() throws Exception {
        when(authorService.findById(1L)).thenReturn(Optional.of(authorsList.get(0)));
        mockMvc.perform(get("/author/delete")
                        .param("id", "1"))
                .andExpect(view().name("redirect:/authors"));
        verify(authorService, times(1)).deleteById(1);
    }
}
