package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.GenreController;
import ru.otus.hw.controller.NotFoundException;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Тестирование контроллера по работе со страницами для отображения жанров")
@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;


    private List<GenreDto> genreDtoList = List.of(
            new GenreDto(1L, "Genre_1"),
            new GenreDto(2L, "Genre_2"),
            new GenreDto(3L, "Genre_3"));

    @DisplayName(" должен возвращать страницу со всеми жанрами")
    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        when(genreService.findAll()).thenReturn(genreDtoList);
        mockMvc.perform(get("/genres"))
                .andExpect(view().name("genres"))
                .andExpect(model().attribute("genres", genreDtoList));
    }


    @DisplayName(" должен сохранять новый жанр" +
            " и перенаправлять на страницу со всеми жанрами")
    @Test
    void shouldSaveNewGenreAndRedirectToContextPath() throws Exception {
        mockMvc.perform(post("/genre/new")
                        .flashAttr("genre",
                                new GenreDto(null, "Genre_5")))
                .andExpect(view().name("redirect:/genres"));
        verify(genreService, times(1)).save(any(GenreDto.class));
    }

    @DisplayName(" должен удалять жанр с указанным Id и перенаправлять на страницу со всеми жанрами")
    @Test
    void shouldDeleteGenreByIdAndRedirectToContextPath() throws Exception {
        when(genreService.findById(1L)).thenReturn(Optional.of(genreDtoList.get(0)));
        mockMvc.perform(get("/genre/delete")
                        .param("id", "1"))
                .andExpect(view().name("redirect:/genres"));
        verify(genreService, times(1)).deleteById(1);
    }
}
