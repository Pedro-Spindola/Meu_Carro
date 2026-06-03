package com.drivenote.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "ApiError")
public record ApiError(
        int status,
        String error,
        String message,
        String path,
        String correlationId,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,
        List<FieldValidationError> fieldErrors
) {
    public record FieldValidationError(String field, String message) {}
}