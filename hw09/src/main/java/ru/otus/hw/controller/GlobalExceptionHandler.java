package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handeNotFoundException(NotFoundException ex) {
        String errorText = "Entity not found";
        return new ModelAndView("customError", "errorText", errorText);
    }

}
