package com.drivenote.controller;

import com.drivenote.config.RefreshCookieProperties;
import com.drivenote.dto.*;
import com.drivenote.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final RefreshCookieProperties cookieProps;

    @Operation(security = {})
    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid AuthRequest request,
                              HttpServletResponse response) {

        AuthTokens auth = service.login(request);
        addRefreshCookie(response, auth.refreshToken());
        return new AuthResponse(auth.token(), auth.usuarioId());
    }

    @Operation(security = {})
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody(required = false) RefreshRequest request,
                                HttpServletRequest http,
                                HttpServletResponse response) {

        String refresh = getRefreshToken(request, http);
        AuthTokens auth = service.refresh(refresh);

        addRefreshCookie(response, auth.refreshToken());
        return new AuthResponse(auth.token(), auth.usuarioId());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest http,
                       HttpServletResponse response,
                       @RequestBody(required = false) LogoutRequest request) {

        String refresh = getRefreshToken(request, http);

        String auth = http.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new BadCredentialsException("Access token ausente");
        }

        String token = auth.substring(7);
        service.logout(token, refresh);
        clearRefreshCookie(response);
    }

    private String getRefreshToken(Object request, HttpServletRequest http) {
        if (request instanceof RefreshRequest rr && rr.refreshToken() != null) return rr.refreshToken();
        if (request instanceof LogoutRequest lr && lr.refreshToken() != null) return lr.refreshToken();

        if (http.getCookies() != null) {
            for (var c : http.getCookies()) {
                if ("refreshToken".equals(c.getName())) return c.getValue();
            }
        }
        throw new BadCredentialsException("Refresh token ausente");
    }

    private void addRefreshCookie(HttpServletResponse response, String refresh) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refresh)
                .httpOnly(true)
                .secure(cookieProps.isSecure())
                .path("/api/auth")
                .sameSite(cookieProps.getSameSite())
                .maxAge(60 * 60 * 24 * 7)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}