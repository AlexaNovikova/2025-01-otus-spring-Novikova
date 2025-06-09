package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.GenreRestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тестирование контроллера по работе со страницами для отображения жанров")
@WebMvcTest(value = GenreRestController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;

    private List<GenreDto> genreDtoList = List.of(
            new GenreDto(1L, "Genre_1"),
            new GenreDto(2L, "Genre_2"),
            new GenreDto(3L, "Genre_3"));

    @DisplayName(" должен возвращать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        when(genreService.findAll()).thenReturn(genreDtoList);
        mockMvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genreDtoList)));
    }


    @DisplayName(" должен сохранять новый жанр")
    @Test
    void shouldSaveNewGenre() throws Exception {
        GenreDto genreDto = genreDtoList.get(0);
        given(genreService.save(any())).willReturn(genreDto);
        String expectedResult = mapper.writeValueAsString(genreDto);
        mockMvc.perform(post("/api/v1/genres").contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(genreDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
        verify(genreService, times(1)).save(any(GenreDto.class));
    }

    @DisplayName(" должен удалять жанр с указанным Id")
    @Test
    void shouldDeleteGenreById() throws Exception {
        when(genreService.findById(1L)).thenReturn(Optional.of(genreDtoList.get(0)));
        mockMvc.perform(delete("/api/v1/genres")
                        .param("id", "1"))
                .andExpect(status().isOk());
        verify(genreService, times(1)).deleteById(1);
    }
}
