package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.mongo.MongoBook;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoBookRepository extends MongoRepository<MongoBook, String> {

    Optional<MongoBook> findById(long id);

    List<MongoBook> findAll();


}
