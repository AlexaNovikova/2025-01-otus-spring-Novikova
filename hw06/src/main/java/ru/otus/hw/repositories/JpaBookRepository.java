package ru.otus.hw.repositories;

import jakarta.persistence.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public JpaBookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> bookEntityGraph = entityManager.getEntityGraph("book-entity-graph");
        TypedQuery<Book> bookTypedQuery = entityManager.createQuery("Select b from Book b where b.id = :id", Book.class);
        bookTypedQuery.setParameter("id", id);
        bookTypedQuery.setHint(FETCH.getKey(), bookEntityGraph);
        try {
            return Optional.ofNullable(bookTypedQuery.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-entity-graph");
        TypedQuery<Book> bookTypedQuery = entityManager.createQuery("Select b from Book b", Book.class);
        bookTypedQuery.setHint(FETCH.getKey(), entityGraph);
        return bookTypedQuery.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        Query query = entityManager.createQuery("delete " +
                "from Book b " +
                "where b.id = : id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
