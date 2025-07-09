package ru.otus.hw.exceptions;

public class DeleteEntityException extends RuntimeException {

    public DeleteEntityException(String message) {
        super(message);
    }
}
