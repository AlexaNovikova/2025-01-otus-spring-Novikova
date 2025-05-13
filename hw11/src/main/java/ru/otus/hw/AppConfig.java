package ru.otus.hw;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableMongock
@EnableReactiveMongoRepositories
public class AppConfig {
}
