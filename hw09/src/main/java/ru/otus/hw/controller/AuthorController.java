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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.ExceptionSupplier;
import ru.otus.hw.services.AuthorService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String findAllAuthors(Model model) {
        var authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("/author/edit")
    public String openEditOrSaveNewAuthorPage(@RequestParam Long id, Model model) {
        var author = authorService.findById(id)
                .orElseThrow(ExceptionSupplier
                        .createNotFoundException("Автор с указанным id не найден"));
        model.addAttribute("author", author);
        return "editOrNewAuthorPage";
    }

    @GetMapping("/author/new")
    public String openSaveNewAuthorPage(Model model) {
        var author = new AuthorDto();
        model.addAttribute("author", author);
        return "editOrNewAuthorPage";
    }

    @PostMapping("/author/editOrSaveNew")
    public String saveAuthor(@Valid @ModelAttribute("author") AuthorDto authorDto,
                             final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editOrNewAuthorPage";
        }
        authorService.save(authorDto);
        return "redirect:/authors";
    }

    @GetMapping("/author/delete")
    public String deleteAuthor(@RequestParam Long id) {
        authorService.deleteById(id);
        return "redirect:/authors";
    }
}
