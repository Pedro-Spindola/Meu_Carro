package com.drivenote.controller;

import com.drivenote.it.BaseIntegrationTest;
import com.drivenote.TestDataFactory;
import com.drivenote.dto.AbastecimentoCreateRequest;
import com.drivenote.enums.TipoCombustivel;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AbastecimentoControllerIT extends BaseIntegrationTest {

    @Test
    void deveCriarEListarAbastecimentos() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();
        Long veiculoId = TestDataFactory.createVeiculo(user.id, token);

        AbastecimentoCreateRequest request = new AbastecimentoCreateRequest(
                veiculoId,
                LocalDate.now().minusDays(3),
                TipoCombustivel.GASOLINA,
                new BigDecimal("230.50"),
                new BigDecimal("38.0"),
                new BigDecimal("6.07"),
                45200L
        );

        Long abastecimentoId =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + token)
                        .body(request)
                        .when()
                        .post("/api/abastecimentos")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        Long id = ((Number) abastecimentoId).longValue();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/abastecimentos/veiculo/" + veiculoId)
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("inicio", LocalDate.now().minusDays(10))
                .queryParam("fim", LocalDate.now())
                .when()
                .get("/api/abastecimentos/veiculo/" + veiculoId + "/periodo")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }
}
