package com.drivenote.dto;

public record AuthTokens(String token, String refreshToken, Long usuarioId) {}