package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class MyHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        var books = bookRepository.findAll();
        if (books.isEmpty()) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Данные в БД отсутствуют!")
                    .build();
        } else {
            return Health.up()
                    .withDetail("message", "Данные в БД загружены.")
                    .build();
        }
    }
}
