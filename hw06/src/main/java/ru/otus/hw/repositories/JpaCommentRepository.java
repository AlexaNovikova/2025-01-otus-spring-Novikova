package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public JpaCommentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        TypedQuery<Comment> commentTypedQuery = entityManager.createQuery("Select c from Comment c where c.book.id = :bookId", Comment.class);
        commentTypedQuery.setParameter("bookId", bookId);
        return commentTypedQuery.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            entityManager.persist(comment);
            return comment;
        }
        return entityManager.merge(comment);
    }

    @Override
    public void deleteById(Long commentId) {
        Query query = entityManager.createQuery("delete " +
                "from Comment c " +
                "where c.id = :commentId");
        query.setParameter("commentId", commentId);
        query.executeUpdate();
    }
}
