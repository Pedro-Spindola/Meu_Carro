package com.drivenote.exception;

/**
 * Exceção para recurso inexistente.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}