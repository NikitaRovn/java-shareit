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
        log.warn("Пользователь не найден: {}", e.getErrors());

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

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ValidationErrorResponse handleUserFoundException(UserAlreadyExistsException e, HttpServletRequest r) {
        log.warn("Пользователь найден: {}", e.getErrors());

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
        log.warn("Запрет доступа: {}", e.getErrors());

        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "Вы не владелец предмета.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse handleItemNotFoundException(ItemNotFoundException e, HttpServletRequest r) {
        log.warn("Предмет не найден: {}", e.getErrors());

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

    @ExceptionHandler(InvalidBookingTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleInvalidBookingTimeException(InvalidBookingTimeException e,
                                                                     HttpServletRequest r) {
        log.warn("Некорректные даты бронирования: {}", e.getErrors());

        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Некорректные даты бронирования.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(ItemUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleItemUnavailableException(ItemUnavailableException e,
                                                                  HttpServletRequest r) {
        log.warn("Вещь недоступна для бронирования: {}", e.getErrors());

        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Вещь недоступна для бронирования.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse handleBookingNotFoundException(BookingNotFoundException e,
                                                                  HttpServletRequest r) {
        log.warn("Бронирование не найдено: {}", e.getErrors());

        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "Бронирование не найдено.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(BookingAlreadyApprovedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleBookingAlreadyApprovedException(BookingAlreadyApprovedException e,
                                                                         HttpServletRequest r) {
        log.warn("Бронирование уже подтверждено: {}", e.getErrors());

        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Бронирование уже подтверждено.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(BookingOwnerItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleBookingOwnerItemException(BookingOwnerItemException e,
                                                                   HttpServletRequest r) {
        log.warn("Попытка забронировать свою же вещь: {}", e.getErrors());

        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Нельзя бронировать собственную вещь.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(BookingNotOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ValidationErrorResponse handleBookingNotOwnerException(BookingNotOwnerException e,
                                                                  HttpServletRequest r) {
        log.warn("Доступ к бронированию запрещён: {}", e.getErrors());

        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "Вы не владелец этого бронирования.",
                r.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(InvalidStateBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleInvalidStateBooking(InvalidStateBookingException e,
                                                             HttpServletRequest r) {
        log.warn("Некорректный state бронирования: {}", e.getErrors());

        List<ErrorResponse> errors = e.getErrors().stream().toList();

        return new ValidationErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Некорректный параметр state.",
                r.getRequestURI(),
                errors
        );
    }
}
