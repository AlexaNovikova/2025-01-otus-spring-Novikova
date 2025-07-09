package ru.otus.catalog.libraryclient.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoWithComments;
import ru.otus.hw.dto.BookToSaveDto;

import java.util.List;

@FeignClient(name = "library-server", fallback = BookClientFallback.class)
public interface BookFeignClient {

    @CircuitBreaker(name = "getBooks")
    @GetMapping("/api/v1/books")
    List<BookDto> getAllBooks();

    @CircuitBreaker(name = "getBook")
    @GetMapping("/api/v1/books/{id}")
    BookDtoWithComments getBookById(@PathVariable("id") long id);

    @CircuitBreaker(name = "saveBook")
    @PostMapping("/api/v1/books")
    BookDto insertBook(@RequestBody @Valid BookToSaveDto bookToSaveDto);

    @CircuitBreaker(name = "updateBook")
    @PutMapping("/api/v1/books")
    BookDto updateBook(@RequestBody @Valid BookToSaveDto bookToSaveDto);

    @DeleteMapping("/api/v1/books/{id}")
    void deleteBook(@PathVariable("id") long id);
}
