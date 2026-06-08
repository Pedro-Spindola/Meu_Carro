package com.drivenote.controller;

import com.drivenote.TestDataFactory;
import com.drivenote.dto.AuthRequest;
import com.drivenote.it.BaseIntegrationTest;
import io.restassured.http.Cookie;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthControllerIT extends BaseIntegrationTest {

    @Test
    void deveLogarComCredenciaisValidas() {
        // Cria usuário dinamicamente — não depende do DataSeeder
        TestDataFactory.UserData user = TestDataFactory.createUsuario();

        AuthRequest request = new AuthRequest(user.email, user.senha);

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
        assertNotNull(refreshCookie, "Refresh cookie deve estar presente após login");
    }

    @Test
    void deveRetornar400ComCredenciaisInvalidas() {
        // Cria usuário e tenta logar com senha errada
        TestDataFactory.UserData user = TestDataFactory.createUsuario();

        AuthRequest request = new AuthRequest(user.email, "senha_errada");

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

    @Test
    void deveRetornar400ComEmailInexistente() {
        AuthRequest request = new AuthRequest("naoexiste@drivenote.com", "123456");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("message", notNullValue());
    }
}