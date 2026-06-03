package com.drivenote.controller;

import com.drivenote.it.BaseIntegrationTest;
import com.drivenote.TestDataFactory;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class ManutencaoControllerIT extends BaseIntegrationTest {

    @Test
    void deveCriarEListarManutencoes() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();
        Long veiculoId = TestDataFactory.createVeiculo(user.id, token);
        Long planoId = TestDataFactory.createPlano(veiculoId, token);

        TestDataFactory.createManutencao(veiculoId, planoId, token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/manutencoes/veiculo/" + veiculoId)
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/manutencoes/plano/" + planoId)
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }
}