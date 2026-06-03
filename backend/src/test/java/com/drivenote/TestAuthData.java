package com.drivenote;

import com.drivenote.dto.AuthResponse;
import io.restassured.http.Cookie;

public record TestAuthData(AuthResponse auth, Cookie refreshCookie) {}