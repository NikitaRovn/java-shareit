package ru.practicum.shareit.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidation(MethodArgumentNotValidException e, HttpServletRequest r) {
        e.getBindingResult().getFieldErrors().forEach(error ->
                log.warn("Поле: '{}'. Ошибка: {}",
                        error.getField(),
                        error.getDefaultMessage()));

        List<ErrorResponse> errors = e.getBindingResult().getFieldErrors().stream()
                .map(f -> new ErrorResponse(f.getField(), f.getDefaultMessage()))
                .toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Ошибка валидации данных.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse handleUserNotFoundException(UserNotFoundException e, HttpServletRequest r) {
        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "Пользователь не найден.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(UserFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ValidationErrorResponse handleUserFoundException(UserFoundException e, HttpServletRequest r) {
        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Пользователь найден.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(NotOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ValidationErrorResponse handleNotOwnerException(NotOwnerException e, HttpServletRequest r) {
        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "Пользователь не вы.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse handleItemNotFoundException(ItemNotFoundException e, HttpServletRequest r) {
        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "Предмет не найден.",
                r.getRequestURI(),
                errors
        );
    }
}
