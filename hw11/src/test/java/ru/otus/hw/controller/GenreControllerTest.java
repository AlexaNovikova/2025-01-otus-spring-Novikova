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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.GenreToDtoConverter;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование rest-контроллера по работе с жанрами")
@WebFluxTest(GenreRestController.class)
public class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private GenreToDtoConverter genreToDtoConverter;

    private List<GenreDto> genreDtoList = List.of(
            new GenreDto("g1", "Genre_1"),
            new GenreDto("g2", "Genre_2"),
            new GenreDto("g3", "Genre_3"));

    private List<Genre> genres = List.of(
            new Genre("g1", "Genre_1"),
            new Genre("g2", "Genre_2"),
            new Genre("g3", "Genre_3"));

    @DisplayName(" должен возвращать корректный список всех жанров")
    @Test
    void testHandleGetAll() {
        given(genreRepository.findAll(any(Sort.class)))
                .willReturn(Flux.fromIterable(genres));

        given(genreToDtoConverter.convert(genres.get(0)))
                .willReturn(genreDtoList.get(0));
        given(genreToDtoConverter.convert(genres.get(1)))
                .willReturn(genreDtoList.get(1));
        given(genreToDtoConverter.convert(genres.get(2)))
                .willReturn(genreDtoList.get(2));

        webTestClient.get()
                .uri("/api/v1/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(3)
                .isEqualTo(genreDtoList);

        verify(genreRepository).findAll(any(Sort.class));
    }

    @DisplayName(" должен возвращать NotFoundException при указании несуществующего id жанра")
    @Test
    void shouldReturnEntityNotFoundExceptionWhenGenreNotFound() {
        when(genreRepository.findById("g1")).thenThrow(EntityNotFoundException.class);
        webTestClient.get()
                .uri("/api/v1/genres/g1")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName(" должен корректно сохранять жанр")
    void shouldCorrectSaveNewAuthor() {
        given(genreRepository.save(any())).willReturn(Mono.just(genres.get(0)));

        given(genreToDtoConverter.convert(genres.get(0)))
                .willReturn(genreDtoList.get(0));

        GenreDto newGenre = new GenreDto(null, "New");

        given(genreToDtoConverter.convertToEntity(newGenre))
                .willReturn(new Genre(null, "New"));

        GenreDto savedGenre = webTestClient
                .post()
                .uri("/api/v1/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newGenre)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GenreDto.class)
                .returnResult().getResponseBody();

        assertThat(savedGenre).isNotNull()
                .matches(g -> g.getName().equals(genreDtoList.get(0).getName()))
                .matches(g -> !g.getId().isEmpty());
    }
}
