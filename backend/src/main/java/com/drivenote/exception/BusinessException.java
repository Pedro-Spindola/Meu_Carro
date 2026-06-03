package com.drivenote.exception;

/**
 * Exceção para regras de negócio inválidas.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}