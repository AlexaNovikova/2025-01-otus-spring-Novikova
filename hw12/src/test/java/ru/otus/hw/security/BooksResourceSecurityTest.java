package ru.otus.hw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.controller.BookRestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
@Import(SecurityConfiguration.class)
public class BooksResourceSecurityTest {

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
            new BookDto(1L, "BookTitle_1",
                    new AuthorDto(1L, "Author_1"),
                    new GenreDto(1L, "Genre_1")),
            new BookDto(2L, "BookTitle_2",
                    new AuthorDto(2L, "Author_2"),
                    new GenreDto(2L, "Genre_2")),
            new BookDto(3L, "BookTitle_3",
                    new AuthorDto(3L, "Author_3"),
                    new GenreDto(3L, "Genre_3")));

    private BookDtoWithComments bookDtoWithComments =
            new BookDtoWithComments(1L, "BookTitle_1",
                    new AuthorDto(1L, "Author_1"),
                    new GenreDto(1L, "Genre_1"),
                    List.of(
                            new CommentDto(1L, "comment_1 to book_1", 1L),
                            new CommentDto(2L, "comment_2 to book_1", 1L)));

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @Test
    public void testOnlyForAuthenticatedUsers() throws Exception {
        when(bookService.findAll()).thenReturn(bookDtos);

        when(bookService.findById(1L)).thenReturn(Optional.ofNullable(bookDtoWithComments));

        BookDto bookDto = bookDtos.get(0);
        given(bookService.save(any())).willReturn(bookDto);

        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsString(
                                        new BookToSaveDto(bookDtoWithComments))))
                .andExpect(status().isOk());
    }

    @Test
    public void redirectsToLoginFormIfNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.writeValueAsString(
                                        new BookToSaveDto(bookDtoWithComments))))
                .andExpect(status().is3xxRedirection());
    }
}
