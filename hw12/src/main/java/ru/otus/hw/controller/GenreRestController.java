package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genres")
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> getAllGenres() {
        var genres = genreService.findAll();
        if (genres.isEmpty()) {
            throw new EntityNotFoundException("Genres not found!");
        }
        return genres;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable Long id) {
        var genre = genreService.findById(id);
        if (genre.isEmpty()) {
            throw new EntityNotFoundException("Genre with id " + id + " not found");
        }
        return ResponseEntity.ok(genre.get());
    }

    @PostMapping
    public ResponseEntity<GenreDto> addGenre(@Valid @RequestBody GenreDto genreDto) {
        var genre = genreService.save(genreDto);
        return ResponseEntity.ok(genre);
    }

    @PutMapping
    public ResponseEntity<GenreDto> updateGenre(@Valid @RequestBody GenreDto genreDto) {
        var genre = genreService.save(genreDto);
        return ResponseEntity.ok(genre);
    }

    @DeleteMapping
    public void deleteGenre(@RequestParam("id") Long id) {
        genreService.deleteById(id);
    }
}
