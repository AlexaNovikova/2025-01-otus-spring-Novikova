package ru.otus.lib.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.common.error.ShopException;
import ru.otus.common.error.ErrorDto;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static ru.otus.common.error.ShopExceptionCode.ACCESS_DENIED;
import static ru.otus.common.error.ShopExceptionCode.BAD_REQUEST;
import static ru.otus.common.error.ShopExceptionCode.ENTITY_ALREADY_EXISTS;
import static ru.otus.common.error.ShopExceptionCode.INTERNAL_SYSTEM_ERROR;
import static ru.otus.common.error.ShopExceptionCode.NOT_FOUND;
import static ru.otus.common.error.ShopExceptionCode.UNAUTHORIZED;
import static ru.otus.common.error.ShopExceptionCode.UNPROCESSABLE_ENTITY_EXCEPTION;

@Slf4j
@ControllerAdvice
public class ShopExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorDto> handleAuthenticationException(Exception ex) {
        log.error("Authentication exception : ", ex);
        var message = ex.getMessage() != null ? ex.getMessage() : UNAUTHORIZED.getCode();
        var errorDto = ErrorDto.builder()
                .code(UNAUTHORIZED.getText())
                .message(message)
                .build();
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorDto> handleAccessDeniedException(
            Exception ex) {
        log.error("Access Denied exception: ", ex);
        var errorDto = ErrorDto.builder()
                .code(ACCESS_DENIED.getCode())
                .message(ACCESS_DENIED.getText())
                .build();
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorDto> handleNoSuchElementException(Exception ex) {
        log.error("Entity not found exception: ", ex);
        var errorDto = ErrorDto.builder()
                .code(NOT_FOUND.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : NOT_FOUND.getText())
                .build();
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ShopException.class})
    public ResponseEntity<ErrorDto> handleShopException(Exception ex) {
        log.error("Shop exception: ", ex);
        var shopException = (ShopException) ex;
        var errorDto = ErrorDto.builder()
                .code(shopException.getCode())
                .message(shopException.getMessage())
                .build();
        var status = shopException.getHttpStatus() != null ?
                HttpStatus.valueOf(shopException.getHttpStatus()) :
                HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<>(errorDto, new HttpHeaders(), status);
    }


    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<ErrorDto> handleEntityExistsException(Exception ex) {
        log.error("Entity exists exception: ", ex);
        var errorDto = ErrorDto.builder()
                .code(ENTITY_ALREADY_EXISTS.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : ENTITY_ALREADY_EXISTS.getText())
                .build();
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorDto> handleIllegalException(Exception ex) {
        log.error("Illegal state or argument exception: ", ex);
        var errorDto = ErrorDto.builder()
                .code(UNPROCESSABLE_ENTITY_EXCEPTION.getCode())
                .message(ex.getMessage() != null ? ex.getMessage()
                        : UNPROCESSABLE_ENTITY_EXCEPTION.getText())
                .build();
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        var errorDto = ErrorDto.builder()
                .code(BAD_REQUEST.getCode())
                .message(BAD_REQUEST.getText())
                .info(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handle(Exception ex) {
        log.error("Exception occurred: ", ex);
        var errorDto = ErrorDto.builder()
                .code(INTERNAL_SYSTEM_ERROR.getCode())
                .message(INTERNAL_SYSTEM_ERROR.getText())
                .build();
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}