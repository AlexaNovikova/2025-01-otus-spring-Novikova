package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.DeleteEntityException;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handeNotFoundException(NotFoundException ex) {
        String errorText = ex.getMessage();
        return new ModelAndView("customError", "errorText", errorText);
    }

    @ExceptionHandler(DeleteEntityException.class)
    public ModelAndView handeDeleteEntityException(DeleteEntityException ex) {
        String errorText = ex.getMessage();
        return new ModelAndView("customError", "errorText", errorText);
    }
}
