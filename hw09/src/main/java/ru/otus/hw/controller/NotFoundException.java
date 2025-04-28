package ru.otus.hw.controller;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Ресурс не найден.");
    }

    public NotFoundException(String message) {
        super("Ресурс не найден. " + message);
    }
}
