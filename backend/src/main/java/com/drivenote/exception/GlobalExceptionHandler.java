package com.drivenote.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("NOT_FOUND path={} msg={}", request.getRequestURI(), ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(BusinessException.class) // <- adicionado
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex, HttpServletRequest request) {
        log.warn("BUSINESS_ERROR path={} msg={}", request.getRequestURI(), ex.getMessage());
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("BAD_REQUEST path={} msg={}", request.getRequestURI(), ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ApiError.FieldValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .toList();

        log.warn("VALIDATION_ERROR path={} fields={}", request.getRequestURI(), errors.size());
        return buildError(HttpStatus.BAD_REQUEST, "Erro de validação", request, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, HttpServletRequest request) {
        List<ApiError.FieldValidationError> errors = ex.getConstraintViolations().stream()
                .map(v -> new ApiError.FieldValidationError(
                        v.getPropertyPath().toString(),
                        v.getMessage()))
                .toList();

        log.warn("CONSTRAINT_VIOLATION path={} violations={}", request.getRequestURI(), errors.size());
        return buildError(HttpStatus.BAD_REQUEST, "Erro de validação", request, errors);
    }

    @ExceptionHandler(Exception.class) // <- sempre por último
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("INTERNAL_ERROR path={} msg={}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor", request, null);
    }

    private ResponseEntity<ApiError> buildError(HttpStatus status, String message,
                                                HttpServletRequest request,
                                                List<ApiError.FieldValidationError> fieldErrors) {
        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                MDC.get("correlationId"),
                LocalDateTime.now(),
                fieldErrors != null ? fieldErrors : List.of()
        );
        return ResponseEntity.status(status).body(apiError);
    }

    private ApiError.FieldValidationError mapFieldError(FieldError fieldError) {
        return new ApiError.FieldValidationError(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex,
                                                         HttpServletRequest request) {
        log.warn("BAD_CREDENTIALS path={} msg={}", request.getRequestURI(), ex.getMessage());

        // Refresh/logout usam 401; login usa 400
        boolean isAuthEndpoint = request.getRequestURI().contains("/auth/login");
        HttpStatus status = isAuthEndpoint ? HttpStatus.BAD_REQUEST : HttpStatus.UNAUTHORIZED;

        return buildError(status, ex.getMessage(), request, null);
    }
}