package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.mongo.MongoAuthor;

@Repository
public interface MongoAuthorRepository extends MongoRepository<MongoAuthor, String> {

}
