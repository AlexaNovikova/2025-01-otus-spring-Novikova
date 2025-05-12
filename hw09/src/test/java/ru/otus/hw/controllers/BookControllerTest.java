package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.BookController;
import ru.otus.hw.controller.NotFoundException;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Тестирование контроллера по работе со страницами для отображения книг")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

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

    @DisplayName(" должен возвращать страницу со всеми книгами")
    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findAll()).thenReturn(bookDtos);
        mockMvc.perform(get("/books"))
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", bookDtos));
    }

    @DisplayName(" должен возвращать страницу с указанной по id книгой для редактирования")
    @Test
    void shouldRenderEditPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(bookDtoWithComments));
        var expectedBook = new BookToSaveDto(bookDtoWithComments);
        mockMvc.perform(get("/book/edit").param("id", "1"))
                .andExpect(view().name("editOrNewBookPage"))
                .andExpect(model().attribute("book", expectedBook));
    }

    @DisplayName(" должен возвращать NotFoundException при указании несуществующего id книги")
    @Test
    void shouldRenderErrorPageWhenBookNotFound() throws Exception {
        when(bookService.findById(1L)).thenThrow(new NotFoundException());
        mockMvc.perform(get("/book/edit").param("id", "1"))
                .andExpect(view().name("customError"));
    }

    @DisplayName(" должен сохранять отредактированную книгу" +
            " и перенаправлять на страницу со всеми книгами")
    @Test
    void shouldSaveEditedBookAndRedirectToContextPath() throws Exception {
        mockMvc.perform(post("/book/editOrSaveNew")
                        .flashAttr("book",
                                new BookToSaveDto(1L, "New_title",
                                        3L, 3L)))
                .andExpect(view().name("redirect:/books"));
        verify(bookService, times(1)).save(any(BookToSaveDto.class));
    }

    @DisplayName(" должен сохранять новую книгу" +
            " и перенаправлять на страницу со всеми книгами")
    @Test
    void shouldSaveNewBookAndRedirectToContextPath() throws Exception {
        mockMvc.perform(post("/book/editOrSaveNew")
                        .flashAttr("book",
                                new BookToSaveDto(null, "New_title",
                                        3L, 3L)))
                .andExpect(view().name("redirect:/books"));
        verify(bookService, times(1)).save(any(BookToSaveDto.class));
    }

    @DisplayName(" должен возвращать страницу с указанной по id книгой со всеми комментариями")
    @Test
    void shouldOpenPreviewBookWithCommentsPageWithCorrectViewAndModelAttributes() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(bookDtoWithComments));
        mockMvc.perform(get("/bookWithComments")
                        .param("id", "1"))
                .andExpect(view().name("bookWithComments"))
                .andExpect(model().attribute("book", bookDtoWithComments));
    }

    @DisplayName(" должен удалять книгу с указанным Id и перенаправлять на страницу со всеми книгами")
    @Test
    void shouldDeleteBookByIdAndRedirectToContextPath() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(bookDtoWithComments));
        mockMvc.perform(get("/book/delete")
                        .param("id", "1"))
                .andExpect(view().name("redirect:/books"));
        verify(bookService, times(1)).deleteById(1);
    }
}
