package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.GenreToDtoConverter;
import ru.otus.hw.repositories.GenreRepository;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genres")
public class GenreRestController {

    private final GenreRepository genreRepository;

    private final GenreToDtoConverter genreToDtoConverter;

    @GetMapping
    public Flux<GenreDto> getAllGenres() {
        return genreRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .map(genreToDtoConverter::convert);
    }

    @GetMapping("/{id}")
    public Mono<GenreDto> getGenreById(@PathVariable String id) {
        return genreRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException("Genre with id " + id + " not found")))
                .map(genreToDtoConverter::convert);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GenreDto> addGenre(@Valid @RequestBody GenreDto genreDto) {
        var genre = genreToDtoConverter.convertToEntity(genreDto);
        genre.setId(String.valueOf(UUID.randomUUID()));
        return genreRepository.save(genre).map(genreToDtoConverter::convert);
    }

    @PutMapping
    public Mono<GenreDto> updateGenre(@Valid @RequestBody GenreDto genreDto) {
        var genre = genreToDtoConverter.convertToEntity(genreDto);
        return genreRepository.findById(genre.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException
                        ("Genre with specified id not found")))
                .then(genreRepository.save(genre)
                        .map(genreToDtoConverter::convert));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGenre(@RequestParam("id") String id) {
        return genreRepository.deleteById(id);
    }
}
