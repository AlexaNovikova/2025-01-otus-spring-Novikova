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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorToDtoConverter;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/authors")
public class AuthorRestController {

    private final AuthorRepository authorRepository;

    private final AuthorToDtoConverter authorToDtoConverter;

    @GetMapping
    public Flux<AuthorDto> getAllAuthors() {
        return authorRepository.findAll(Sort.by(Sort.Direction.ASC, "fullName"))
                .map(authorToDtoConverter::convert);

    }

    @GetMapping("/{id}")
    public Mono<AuthorDto> getAuthorById(@PathVariable String id) {
        return authorRepository.findById(id)
                .switchIfEmpty(Mono.error
                        (new EntityNotFoundException("Author with specified id not found")))
                .map(authorToDtoConverter::convert);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthorDto> addAuthor(@Valid @RequestBody AuthorDto authorDto) {
        var author = authorToDtoConverter.convertToEntity(authorDto);
        author.setId(String.valueOf(UUID.randomUUID()));
        return authorRepository.save(author).map(authorToDtoConverter::convert);
    }

    @PutMapping
    public Mono<AuthorDto> updateAuthor(@Valid @RequestBody AuthorDto authorDto) {
        var author = authorToDtoConverter.convertToEntity(authorDto);
        return authorRepository.findById(author.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException
                        ("Author with specified id not found")))
                .then(authorRepository.save(author)
                        .map(authorToDtoConverter::convert));

    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAuthor(@RequestParam("id") String id) {
        return authorRepository.deleteById(id);
    }
}
