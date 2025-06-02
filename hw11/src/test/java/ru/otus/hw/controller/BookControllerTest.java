package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookToDtoConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование rest-контроллера по работе с книгами")
@WebFluxTest(BookRestController.class)
public class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookToDtoConverter bookToDtoConverter;

    private List<Book> books = List.of(
            new Book("b1", "BookTitle_1",
                    new Author("a1", "Author_1"),
                    new Genre("g1", "Genre_1")),
            new Book("b2", "BookTitle_2",
                    new Author("a2", "Author_2"),
                    new Genre("g2", "Genre_2")),
            new Book("b3", "BookTitle_3",
                    new Author("a3", "Author_3"),
                    new Genre("g3", "Genre_3")));


    private List<BookDto> bookDtos = List.of(
            new BookDto("b1", "BookTitle_1",
                    new AuthorDto("a1", "Author_1"),
                    new GenreDto("g1", "Genre_1")),
            new BookDto("b2", "BookTitle_2",
                    new AuthorDto("a2", "Author_2"),
                    new GenreDto("g2", "Genre_2")),
            new BookDto("b3", "BookTitle_3",
                    new AuthorDto("a3", "Author_3"),
                    new GenreDto("g3", "Genre_3")));

    private BookDtoWithComments bookDtoWithComments =
            new BookDtoWithComments("b1", "BookTitle_1",
                    new AuthorDto("a1", "Author_1"),
                    new GenreDto("g1", "Genre_1"),
                    List.of(
                            new CommentDto("c1", "comment_1 to book_1", "b1"),
                            new CommentDto("c2", "comment_2 to book_1", "b1")));

    private Comment comment = new Comment(null, "New_Comment", books.get(0));

    @DisplayName(" должен возвращать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {

        given(bookRepository.findAll(any(Sort.class)))
                .willReturn(Flux.fromIterable(books));

        given(bookToDtoConverter.convert(books.get(0)))
                .willReturn(bookDtos.get(0));
        given(bookToDtoConverter.convert(books.get(1)))
                .willReturn(bookDtos.get(1));
        given(bookToDtoConverter.convert(books.get(2)))
                .willReturn(bookDtos.get(2));

        webTestClient.get()
                .uri("/api/v1/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(3)
                .isEqualTo(bookDtos);

        verify(bookRepository).findAll(any(Sort.class));
    }

    @DisplayName(" должен находить книгу по id")
    @Test
    void shouldReturnCorrectBookById() throws Exception {

        given(bookRepository.findById("b1"))
                .willReturn(Mono.just(books.get(0)));

        given(bookToDtoConverter
                .convertToBookDtoWithComments(books.get(0)))
                .willReturn(bookDtoWithComments);

        webTestClient.get()
                .uri("/api/v1/books/b1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDtoWithComments.class)
                .isEqualTo(bookDtoWithComments);

        verify(bookRepository, times(1)).findById("b1");

    }

    @DisplayName(" должен возвращать NotFoundException при указании несуществующего id книги")
    @Test
    void shouldReturnEntityNotFoundExceptionWhenNonExistentIdIsSpecified() throws Exception {
        when(bookRepository.findById("b1")).thenThrow(EntityNotFoundException.class);
        webTestClient.get()
                .uri("/api/v1/books/b1")
                .exchange()
                .expectStatus()
                .isNotFound();
    }


    @DisplayName(" должен сохранять новую книгу")
    @Test
    void shouldCorrectSaveNewBook() throws Exception {
        given(bookRepository.save(any())).willReturn(Mono.just(books.get(0)));

        given(bookToDtoConverter.convert(books.get(0)))
                .willReturn(bookDtos.get(0));

        BookToSaveDto newBook = new BookToSaveDto(null, "New", "a1", "g1");

        Author author = new Author("a1", "Author_1");
        Genre genre = new Genre("g1", "Genre_1");

        given(authorRepository.findById("a1")).willReturn(Mono.just(author));
        given(genreRepository.findById("g1")).willReturn(Mono.just(genre));

        given(bookToDtoConverter.convertToEntity(newBook))
                .willReturn(new Book(null, "New", null, null));

        BookDto savedBook = webTestClient
                .post()
                .uri("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .returnResult().getResponseBody();

        assertThat(savedBook).isNotNull().isEqualTo(bookDtos.get(0));
    }

    @DisplayName(" должен сохранять новый комментарий")
    @Test
    void shouldCorrectSaveNewComment() throws Exception {
        given(commentRepository.save(any())).willReturn(Mono.just(comment));
        given(bookRepository.save(any())).willReturn(Mono.just(books.get(0)));

        given(bookRepository.findById("b1")).willReturn(Mono.just(books.get(0)));

        given(bookToDtoConverter.convertToBookDtoWithComments(books.get(0)))
                .willReturn(bookDtoWithComments);

        BookDtoWithComments bookDtoWithCommentsFromDB = webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/books/b1/comments")
                        .queryParam("text", "New_comment")
                        .build())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDtoWithComments.class)
                .returnResult().getResponseBody();
    }


    @DisplayName(" должен удалять книгу с указанным Id cо всеми комментариями")
    @Test
    void shouldDeleteBookById() throws Exception {
        given(bookRepository.findById("b1")).willReturn(Mono.just(books.get(0)));
        given(bookRepository.delete(books.get(0))).willReturn(Mono.empty());
        List<Comment> comments = List.of(comment);
        given(commentRepository.deleteAll(comments)).willReturn(Mono.empty());
        given(commentRepository.findByBookId("b1"))
                .willReturn(Flux.fromIterable(comments));

        webTestClient.delete()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/v1/books")
                                .queryParam("id", "b1")
                                .build())
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}
