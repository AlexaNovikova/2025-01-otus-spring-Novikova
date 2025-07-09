package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.DeleteEntityException;
import ru.otus.hw.exceptions.EntityNotFoundException;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handeNotFoundException(EntityNotFoundException ex) {
        String errorText = ex.getMessage();
        return ResponseEntity.status(404).body(errorText);
    }

    @ExceptionHandler(DeleteEntityException.class)
    public ResponseEntity<String> handeDeleteEntityException(DeleteEntityException ex) {
        String errorText = ex.getMessage();
        return ResponseEntity.status(404).body(errorText);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handeNotFoundException(MethodArgumentNotValidException ex) {
        String errorText = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        System.out.println(errorText);
        return ResponseEntity.status(400).body(errorText);
    }
}
