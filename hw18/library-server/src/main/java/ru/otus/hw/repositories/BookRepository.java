package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RepositoryRestResource(path = "book")
public interface BookRepository extends JpaRepository<Book, Long> {


    @EntityGraph(type = FETCH, attributePaths = {
            "author", "genre", "comments"
    })
    Optional<Book> findById(long id);


    @EntityGraph(type = FETCH, attributePaths = {
            "author", "genre"
    })
    List<Book> findAll();

    @RestResource(path = "author", rel = "author")
    List<Book> findByAuthor(Author author);

    @RestResource(path = "genre", rel = "genre")
    List<Book> findByGenre(Genre genre);
}
