package com.drivenote.controller;

import com.drivenote.TestDataFactory;
import com.drivenote.dto.SenhaUpdateRequest;
import com.drivenote.dto.UsuarioUpdateRequest;
import com.drivenote.it.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class UsuarioControllerIT extends BaseIntegrationTest {

    @Test
    void deveCriarUsuarioComSucesso() {
        // Delegado para UsuarioApiTest — aqui testamos operações autenticadas
        TestDataFactory.UserData user = TestDataFactory.createUsuario();

        given()
                .header("Authorization", "Bearer " +
                        TestDataFactory.loginAndGetTokens(user.email, user.senha).token())
                .when()
                .get("/api/usuarios/" + user.id)
                .then()
                .statusCode(200)
                .body("id", is(user.id.intValue()))
                .body("email", equalTo(user.email));
    }

    @Test
    void deveAtualizarUsuario() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();

        UsuarioUpdateRequest update = new UsuarioUpdateRequest("Novo Nome", "62911111111");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(update)
                .when()
                .put("/api/usuarios/" + user.id)
                .then()
                .statusCode(200)
                .body("nome", equalTo("Novo Nome"))
                .body("telefone", equalTo("62911111111"));
    }

    @Test
    void deveAlterarSenhaComSucesso() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();

        SenhaUpdateRequest senhaRequest = new SenhaUpdateRequest(user.senha, "novaSenha123");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(senhaRequest)
                .when()
                .put("/api/usuarios/" + user.id + "/senha")
                .then()
                .statusCode(204);
    }

    @Test
    void deveRetornar401SemToken() {
        given()
                .when()
                .get("/api/usuarios/1")
                .then()
                .statusCode(401);
    }
}