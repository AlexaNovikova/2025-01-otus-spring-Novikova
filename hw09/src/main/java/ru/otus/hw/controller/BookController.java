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
import ru.otus.hw.dto.BookToSaveDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.ExceptionSupplier;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/books")
    public String findAllBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book/edit")
    public String openEditOrSaveNewBookPage(@RequestParam(value = "id") Long id, Model model) {
        var book = bookService.findById(id)
                .orElseThrow(ExceptionSupplier
                        .createNotFoundException("Книга с указанным id не найдена"));
        model.addAttribute("book", new BookToSaveDto(book));
        return "editOrNewBookPage";
    }

    @GetMapping("/book/new")
    public String openSaveNewBookPage(Model model) {
        var book = new BookToSaveDto();
        model.addAttribute("book", book);
        return "editOrNewBookPage";
    }

    @GetMapping("/bookWithComments")
    public String previewBook(@RequestParam(value = "id") Long id, Model model) {
        var book = bookService.findById(id)
                .orElseThrow(ExceptionSupplier
                        .createNotFoundException("Книга с указанным id не найдена"));
        model.addAttribute("book", book);
        return "bookWithComments";
    }


    @PostMapping("/book/editOrSaveNew")
    public String saveBook(@Valid @ModelAttribute("book") BookToSaveDto bookDto,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editOrNewBookPage";
        }
        bookService.save(bookDto);
        return "redirect:/books";
    }

    @GetMapping("/book/delete")
    public String deleteBook(@RequestParam("id") Long id) {
        var book = bookService.findById(id)
                .orElseThrow(ExceptionSupplier
                        .createNotFoundException("Книга с указанным id не найдена"));
        bookService.deleteById(book.getId());
        return "redirect:/books";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("genresMap",
                genreService.findAll()
                        .stream()
                        .collect(Collectors.toMap(GenreDto::getId, Function.identity())));
        model.addAttribute("authorsMap",
                authorService.findAll()
                        .stream()
                        .collect(Collectors.toMap(AuthorDto::getId, Function.identity())));
    }
}
