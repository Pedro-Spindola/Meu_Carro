package com.drivenote.controller;

import com.drivenote.dto.AuthRequest;
import com.drivenote.it.BaseIntegrationTest;
import io.restassured.http.Cookie;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AuthControllerIT extends BaseIntegrationTest {

    @Test
    void deveLogarComCredenciaisValidas() {
        AuthRequest request = new AuthRequest("joao@drivenote.com", "123456");

        var resp =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/api/auth/login")
                        .then()
                        .statusCode(200)
                        .body("token", notNullValue())
                        .extract();

        Cookie refreshCookie = resp.detailedCookies().get("refreshToken");
        org.junit.jupiter.api.Assertions.assertNotNull(refreshCookie);
    }

    @Test
    void deveRetornar400ComCredenciaisInvalidas() {
        AuthRequest request = new AuthRequest("joao@drivenote.com", "senha_errada");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("error", is("Bad Request"))
                .body("message", notNullValue())
                .body("path", is("/api/auth/login"))
                .body("correlationId", notNullValue());
    }
}