package ru.otus.hw.exceptions;

public class CheckAnswerException extends RuntimeException{
    public CheckAnswerException(String message, Throwable ex) {
        super(message, ex);
    }

    public CheckAnswerException(String message) {
        super(message);
    }
}
