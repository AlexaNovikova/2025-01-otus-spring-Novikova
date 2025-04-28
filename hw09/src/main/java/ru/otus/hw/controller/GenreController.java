package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.ExceptionSupplier;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public String findAllGenres(Model model) {
        var genres = genreService.findAll();
        model.addAttribute("genres", genres);
        model.addAttribute("genre", new GenreDto());
        return "genres";
    }

    @PostMapping("/genre/new")
    public String saveGenre(@Valid @ModelAttribute("genre") GenreDto genreDto,
                             final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "genres";
        }
        genreService.save(genreDto);
        return "redirect:/genres";
    }

    @GetMapping("/genre/delete")
    public String deleteGenre(@RequestParam("id") Long id) {
        var genre = genreService.findById(id)
                .orElseThrow(ExceptionSupplier
                        .createNotFoundException("Жанр с указанным id не найден"));
        genreService.deleteById(genre.getId());
        return "redirect:/genres";
    }
}
