package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.AuthorRestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayName("Тестирование rest-контроллера по работе c авторами")
@WebMvcTest(value = AuthorRestController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AuthorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    private List<AuthorDto> authorsList = List.of(
            new AuthorDto(1L, "Author_1"),
            new AuthorDto(2L, "Author_2"),
            new AuthorDto(3L, "Author_3"));


    @DisplayName(" должен возвращать NotFoundException при указании несуществующего id автора")
    @Test
    void shouldReturnEntityNotFoundExceptionWhenPersonNotFound() throws Exception {
        when(authorService.findById(1L)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/api/v1/authors").param("id", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCorrectAuthorsList() throws Exception {

        given(authorService.findAll()).willReturn(authorsList);

        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authorsList)));
    }

    @Test
    void shouldReturnExpectedErrorWhenPersonsNotFound() throws Exception {
        given(authorService.findAll()).willReturn(List.of());

        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Authors not found!"));
    }

    @Test
    void shouldCorrectSaveNewAuthor() throws Exception {
        AuthorDto authorDto = new AuthorDto(1L, "Author1");
        given(authorService.save(any())).willReturn(authorDto);
        String expectedResult = mapper.writeValueAsString(authorDto);
        mockMvc.perform(post("/api/v1/authors").contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }
}
