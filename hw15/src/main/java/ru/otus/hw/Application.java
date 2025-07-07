package ru.otus.hw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Application.class, args);
    }
}
