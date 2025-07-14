package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.BookRestController;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayName("Тестирование rest-контроллера по работе с книгами")
@WebMvcTest(value = BookRestController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;


    private List<BookDto> bookDtos = List.of(
            new BookDto(1L, "BookTitle_1", false,
                    new AuthorDto(1L, "Author_1"),
                    new GenreDto(1L, "Genre_1")),
            new BookDto(2L, "BookTitle_2", false,
                    new AuthorDto(2L, "Author_2"),
                    new GenreDto(2L, "Genre_2")),
            new BookDto(3L, "BookTitle_3", false,
                    new AuthorDto(3L, "Author_3"),
                    new GenreDto(3L, "Genre_3")));

    private BookDtoWithComments bookDtoWithComments =
            new BookDtoWithComments(1L, "BookTitle_1", false,
                    new AuthorDto(1L, "Author_1"),
                    new GenreDto(1L, "Genre_1"),
                    List.of(
                            new CommentDto(1L, "comment_1 to book_1", 1L),
                            new CommentDto(2L, "comment_2 to book_1", 1L)));

    @DisplayName(" должен возвращать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        when(bookService.findAll()).thenReturn(bookDtos);
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDtos)));
    }

    @DisplayName(" должен находить книгу по id")
    @Test
    void shouldReturnCorrectBookById() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(bookDtoWithComments));
        var expectedBookString = mapper.writeValueAsString(bookDtoWithComments);
        mockMvc.perform(get(
                        "/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBookString));
    }

    @DisplayName(" должен возвращать NotFoundException при указании несуществующего id книги")
    @Test
    void shouldReturnEntityNotFoundExceptionWhenNonExistentIdIsSpecified() throws Exception {
        when(bookService.findById(1L)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isNotFound());
    }


    @DisplayName(" должен сохранять новую книгу")
    @Test
    void shouldCorrectSaveNewBook() throws Exception {
        BookDto bookDto = bookDtos.get(0);
        given(bookService.save(any())).willReturn(bookDto);
        String expectedResult = mapper.writeValueAsString(bookDto);
        mockMvc.perform(post("/api/v1/books").contentType(APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsString(
                                        new BookToSaveDto(bookDtoWithComments))))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
        verify(bookService, times(1)).save(any(BookToSaveDto.class));
    }


    @DisplayName(" должен удалять книгу с указанным Id")
    @Test
    void shouldDeleteBookById() throws Exception {
        mockMvc.perform(delete("/api/v1/books")
                        .param("id", "1"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(1);
    }
}
