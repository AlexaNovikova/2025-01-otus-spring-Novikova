package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;


public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findByBook(Book book);

    Flux<Comment> findByBookId(String bookId);

}
