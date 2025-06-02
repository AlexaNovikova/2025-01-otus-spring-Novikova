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
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorToDtoConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование rest-контроллера по работе c авторами")
@WebFluxTest(AuthorRestController.class)
public class AuthorsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private AuthorToDtoConverter authorToDtoConverter;


    private final List<Author> authorsList = List.of(
            new Author("a1", "Author_1"),
            new Author("a2", "Author_2"),
            new Author("a3", "Author_3"));

    private final List<AuthorDto> authorsDtoList = List.of(
            new AuthorDto("a1", "Author_1"),
            new AuthorDto("a2", "Author_2"),
            new AuthorDto("a3", "Author_3"));

    @DisplayName(" должен возвращать корректный список всех авторов")
    @Test
    void testHandleGetAll() {
        given(authorRepository.findAll(any(Sort.class)))
                .willReturn(Flux.fromIterable(authorsList));

        given(authorToDtoConverter.convert(authorsList.get(0)))
                .willReturn(authorsDtoList.get(0));
        given(authorToDtoConverter.convert(authorsList.get(1)))
                .willReturn(authorsDtoList.get(1));
        given(authorToDtoConverter.convert(authorsList.get(2)))
                .willReturn(authorsDtoList.get(2));

        webTestClient.get()
                .uri("/api/v1/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(3)
                .isEqualTo(authorsDtoList);

        verify(authorRepository).findAll(any(Sort.class));
    }

    @DisplayName(" должен возвращать NotFoundException при указании несуществующего id автора")
    @Test
    void shouldReturnEntityNotFoundExceptionWhenPersonNotFound() {
        when(authorRepository.findById("a1")).thenThrow(EntityNotFoundException.class);
        webTestClient.get()
                .uri("/api/v1/authors/a1")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName(" должен корректно сохранять автора")
    void shouldCorrectSaveNewAuthor() {
        given(authorRepository.save(any())).willReturn(Mono.just(authorsList.get(0)));

        given(authorToDtoConverter.convert(authorsList.get(0)))
                .willReturn(authorsDtoList.get(0));

        AuthorDto newAuthor = new AuthorDto(null, "New");

        given(authorToDtoConverter.convertToEntity(newAuthor))
                .willReturn(new Author(null, "New"));

        AuthorDto authorDto = webTestClient
                .post()
                .uri("/api/v1/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newAuthor)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AuthorDto.class)
                .returnResult().getResponseBody();

        assertThat(authorDto).isNotNull()
                .matches(a -> a.getFullName().equals(authorsList.get(0).getFullName()))
                .matches(a -> !a.getId().isEmpty());
    }
}
