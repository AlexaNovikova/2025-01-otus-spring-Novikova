package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.jpa.Author;
import ru.otus.hw.models.jpa.Book;
import ru.otus.hw.models.jpa.Genre;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    @EntityGraph(type = FETCH, attributePaths = {
            "author", "genre", "comments"
    })
    Optional<Book> findById(long id);


    @EntityGraph(type = FETCH, attributePaths = {
            "author", "genre"
    })
    List<Book> findAll();

    List<Book> findByAuthor(Author author);

    List<Book> findByGenre(Genre genre);
}
