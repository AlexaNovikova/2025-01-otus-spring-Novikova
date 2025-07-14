package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/authors")
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        var authors = authorService.findAll();
        if (authors.isEmpty()) {
            throw new EntityNotFoundException("Authors not found!");
        }
        return authors;
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable Long id) {
        var author = authorService.findById(id);
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author with id " + id + " not found");
        }
        return author.get();
    }

    @PostMapping
    public AuthorDto addAuthor(@Valid @RequestBody AuthorDto authorDto) {
        return authorService.save(authorDto);
    }

    @PutMapping
    public AuthorDto updateAuthor(@Valid @RequestBody AuthorDto authorDto) {
        return authorService.save(authorDto);
    }

    @DeleteMapping
    public void deleteAuthor(@RequestParam("id") Long id) {
        authorService.deleteById(id);
    }
}
