package dev.riossolutions.api_gateway.exception.handler;

import dev.riossolutions.api_gateway.exception.error.ErrorCode;
import dev.riossolutions.api_gateway.exception.error.ErrorDetails;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for authentication and authorization APIs.
 */
@Slf4j
@ControllerAdvice
public final class AuthServiceExceptionHandler {

    private ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status,
                                                           ErrorCode code, @Nullable String customMessage) {

        final String description = customMessage != null ? customMessage : code.getDefaultMessage();

        List<ErrorDetails> errors = List.of(ErrorDetails.builder().code(code).error(description).build());

        return ResponseEntity.status(status).body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ErrorDetails> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

        ErrorDetails error = ErrorDetails.builder()
                .code(ErrorCode.TYPE_MISMATCH)
                .error(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        log.error("Exception Message [" + ex.getMessage() + "].");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    private ResponseEntity<?> handleServerErrorException(HttpServerErrorException ex) {
        ErrorDetails error = ErrorDetails.builder()
                .code(ErrorCode.INTERNAL_ERROR)
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
    @ExceptionHandler(HttpClientErrorException.class)
    private ResponseEntity<?> handleClientErrorException(HttpClientErrorException ex) {
        ErrorDetails error = ErrorDetails.builder()
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        final String message = ErrorCode.METHOD_NOT_CURRENTLY_ALLOWED.getDefaultMessage() + ". Unable to find method [" + ex.getMethod() + "], supported methods [" + ex.getSupportedHttpMethods() + "].";
        log.error("Response Code: " + HttpStatus.METHOD_NOT_ALLOWED + ", Retrieved status [" + message + "].");
        return this.handleExceptionInternal(ex, HttpStatus.METHOD_NOT_ALLOWED ,ErrorCode.METHOD_NOT_CURRENTLY_ALLOWED, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorDetails errorDetail = ErrorDetails.builder()
                .code(ErrorCode.TYPE_MISMATCH)
                .errors(ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.toList()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorDetails errorDetail = ErrorDetails.builder()
                .code(ErrorCode.BAD_REQUEST)
                .error(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ErrorDetails errorDetail = ErrorDetails.builder()
                .code(ErrorCode.BAD_REQUEST)
                .errors(ex.getConstraintViolations().stream().map(violation -> violation.getRootBeanClass().getName() + " " +
                        violation.getPropertyPath() + ": " + violation.getMessage()).collect(Collectors.toList()))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }


}