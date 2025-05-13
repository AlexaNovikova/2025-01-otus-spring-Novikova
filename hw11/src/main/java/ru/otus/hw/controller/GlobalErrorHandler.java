package ru.otus.hw.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        return Mono.just(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private int status;
        private String message;
        private List<String> details;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}