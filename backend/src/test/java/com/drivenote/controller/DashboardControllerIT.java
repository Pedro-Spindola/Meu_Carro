package com.drivenote.controller;

import com.drivenote.it.BaseIntegrationTest;
import com.drivenote.TestDataFactory;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class DashboardControllerIT extends BaseIntegrationTest {

    @Test
    void deveBuscarResumoDoVeiculo() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();
        Long veiculoId = TestDataFactory.createVeiculo(user.id, token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/dashboard/veiculo/" + veiculoId)
                .then()
                .statusCode(200)
                .body("nomeVeiculo", notNullValue())
                .body("placa", notNullValue())
                .body("quilometragemAtual", notNullValue());
    }
}