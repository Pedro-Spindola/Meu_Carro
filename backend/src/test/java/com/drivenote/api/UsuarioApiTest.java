package com.drivenote.api;

import com.drivenote.dto.UsuarioCreateRequest;
import com.drivenote.it.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class UsuarioApiTest extends BaseIntegrationTest {

    @Test
    void deveCriarUsuarioViaApi() {
        // UUID garante email único a cada execução — evita 409 em re-runs
        String email = "teste_" + UUID.randomUUID() + "@drivenote.com";

        UsuarioCreateRequest request = new UsuarioCreateRequest(
                "Usuário Teste",
                email,
                "123456",
                "62888888888"
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/usuarios")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", equalTo("Usuário Teste"))
                .body("email", equalTo(email))
                .body("telefone", equalTo("62888888888"))
                .body("createdAt", notNullValue());
    }

    @Test
    void deveRetornar409ComEmailDuplicado() {
        String email = "dup_" + UUID.randomUUID() + "@drivenote.com";

        UsuarioCreateRequest request = new UsuarioCreateRequest(
                "Usuário Teste",
                email,
                "123456",
                "62888888888"
        );

        // Primeiro cadastro — deve funcionar
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/usuarios")
                .then()
                .statusCode(201);

        // Segundo cadastro com mesmo email — deve retornar 409
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/usuarios")
                .then()
                .statusCode(409)
                .body("status", is(409))
                .body("message", notNullValue());
    }

    @Test
    void deveRetornar400ComDadosInvalidos() {
        // Email inválido e nome em branco
        UsuarioCreateRequest request = new UsuarioCreateRequest(
                "",
                "email-invalido",
                "123456",
                "62888888888"
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/usuarios")
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("fieldErrors", notNullValue());
    }
}