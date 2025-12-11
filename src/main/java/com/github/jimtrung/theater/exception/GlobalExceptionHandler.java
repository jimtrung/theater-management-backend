package com.github.jimtrung.theater.exception;

import com.github.jimtrung.theater.dto.ErrorResponse;
import com.github.jimtrung.theater.exception.system.DatabaseOperationException;
import com.github.jimtrung.theater.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, WebRequest req) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest req) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), req);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, WebRequest req) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), req);
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserData(InvalidUserDataException ex, WebRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(DatabaseOperationException ex, WebRequest req) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi cơ sở dữ liệu: " + ex.getMessage(), req);
    }

    @ExceptionHandler(MismatchedAuthProviderException.class)
    public ResponseEntity<ErrorResponse> handleMismatchedAuthProviderException(MismatchedAuthProviderException ex, WebRequest req) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex, WebRequest req) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi không mong muốn: " + ex.getMessage(), req);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, WebRequest req) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            req.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(status).body(error);
    }
}
