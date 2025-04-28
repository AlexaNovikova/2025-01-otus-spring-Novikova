package ru.otus.hw.exceptions;

import ru.otus.hw.controller.NotFoundException;

import java.util.function.Supplier;

public class ExceptionSupplier {
    public static Supplier<NotFoundException> createNotFoundException(String message) {
        return () -> new NotFoundException(message);
    }
}
