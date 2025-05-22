package org.lib.subscriptionservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(IllegalDurationException.class)
    public ResponseEntity<ErrorResponse> handleIllegalDuration(IllegalDurationException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalSubscriptionType.class)
    public ResponseEntity<ErrorResponse> handleIllegalSubscription(IllegalSubscriptionType ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalVisitLimitException.class)
    public ResponseEntity<ErrorResponse> handleIllegalVisitLimit(IllegalVisitLimitException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(StatusAlreadySetException.class)
    public ResponseEntity<ErrorResponse> handleStatusAlreadySet(StatusAlreadySetException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSubscriptionNotFoundException(SubscriptionNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(SubscriptionPlanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSubscriptionPlanNotFound(SubscriptionPlanNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyHasActiveSubscriptionException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyHasActiveSubscription(UserAlreadyHasActiveSubscriptionException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace(); // вывод в консоль
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error: " + ex.getMessage(), request);
    }
}