package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.jpa.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
