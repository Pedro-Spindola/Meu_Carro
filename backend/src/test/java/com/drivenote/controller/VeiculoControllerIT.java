package com.drivenote.controller;

import com.drivenote.TestDataFactory;
import com.drivenote.dto.VeiculoCreateRequest;
import com.drivenote.dto.VeiculoUpdateRequest;
import com.drivenote.enums.TipoCombustivel;
import com.drivenote.it.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class VeiculoControllerIT extends BaseIntegrationTest {

    @Test
    void deveCriarBuscarListarEAtualizarVeiculo() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();

        Long veiculoId = TestDataFactory.createVeiculo(user.id, token);

        // Buscar por ID
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/usuarios/" + user.id + "/veiculos/" + veiculoId)
                .then()
                .statusCode(200)
                .body("id", is(veiculoId.intValue()))
                .body("marca", equalTo("Honda"))
                .body("combustivel", notNullValue());

        // Listar
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/usuarios/" + user.id + "/veiculos")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        // Atualizar
        String novaPlaca = "UPD" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        VeiculoUpdateRequest update = new VeiculoUpdateRequest(
                "Honda", "Civic", 2021, novaPlaca, TipoCombustivel.FLEX, 48000L
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(update)
                .when()
                .put("/api/usuarios/" + user.id + "/veiculos/" + veiculoId)
                .then()
                .statusCode(200)
                .body("modelo", equalTo("Civic"))
                .body("ano", is(2021))
                .body("combustivel", equalTo("FLEX"));
    }

    @Test
    void deveDeletarVeiculo() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();
        Long veiculoId = TestDataFactory.createVeiculo(user.id, token);

        // Deletar
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/usuarios/" + user.id + "/veiculos/" + veiculoId)
                .then()
                .statusCode(204);

        // Confirmar que não existe mais
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/usuarios/" + user.id + "/veiculos/" + veiculoId)
                .then()
                .statusCode(404);
    }

    @Test
    void deveRetornar409ComPlacaDuplicada() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();

        String placa = "DUP" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        VeiculoCreateRequest request = new VeiculoCreateRequest(
                "Honda", "Civic", 2020, placa, TipoCombustivel.GASOLINA, 45000L
        );

        // Primeiro cadastro
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post("/api/usuarios/" + user.id + "/veiculos")
                .then()
                .statusCode(200);

        // Segunda tentativa com mesma placa
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post("/api/usuarios/" + user.id + "/veiculos")
                .then()
                .statusCode(409)
                .body("message", containsString("Placa"));
    }
}