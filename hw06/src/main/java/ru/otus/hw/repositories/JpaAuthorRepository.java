package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaAuthorRepository implements AuthorRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public JpaAuthorRepository(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> authorTypedQuery = entityManager
                .createQuery("Select a from Author a", Author.class);
        return authorTypedQuery.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }
}
