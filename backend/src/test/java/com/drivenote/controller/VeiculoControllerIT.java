package com.drivenote.controller;

import com.drivenote.it.BaseIntegrationTest;
import com.drivenote.TestDataFactory;
import com.drivenote.dto.VeiculoUpdateRequest;
import com.drivenote.enums.TipoCombustivel;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class VeiculoControllerIT extends BaseIntegrationTest {

    @Test
    void deveCriarBuscarListarEAtualizarVeiculo() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();

        Long veiculoId = TestDataFactory.createVeiculo(user.id, token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/usuarios/" + user.id + "/veiculos/" + veiculoId)
                .then()
                .statusCode(200)
                .body("id", is(veiculoId.intValue()));

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/usuarios/" + user.id + "/veiculos")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        VeiculoUpdateRequest update = new VeiculoUpdateRequest(
                "Honda",
                "Civic",
                2021,
                "UPD" + veiculoId,
                TipoCombustivel.FLEX,
                48000L
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(update)
                .when()
                .put("/api/usuarios/" + user.id + "/veiculos/" + veiculoId)
                .then()
                .statusCode(200)
                .body("modelo", is("Civic"))
                .body("ano", is(2021));
    }
}