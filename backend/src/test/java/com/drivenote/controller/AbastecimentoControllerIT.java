package com.drivenote.controller;

import com.drivenote.TestDataFactory;
import com.drivenote.dto.AbastecimentoCreateRequest;
import com.drivenote.enums.TipoCombustivel;
import com.drivenote.it.BaseIntegrationTest;
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
                45200L  // maior que 45000L do veículo criado no factory
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post("/api/abastecimentos")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("consumoMedio", notNullValue())
                .body("custoPorKm", notNullValue());

        // Listar por veículo
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/abastecimentos/veiculo/" + veiculoId)
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        // Listar por período
        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("inicio", LocalDate.now().minusDays(10).toString())
                .queryParam("fim", LocalDate.now().toString())
                .when()
                .get("/api/abastecimentos/veiculo/" + veiculoId + "/periodo")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void deveRejeitar_CombustivelInvalido() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();
        Long veiculoId = TestDataFactory.createVeiculo(user.id, token); // veículo GASOLINA

        // Tenta abastecer com ETANOL em veículo GASOLINA
        AbastecimentoCreateRequest request = new AbastecimentoCreateRequest(
                veiculoId,
                LocalDate.now(),
                TipoCombustivel.ETANOL,  // <- inválido para veículo GASOLINA
                new BigDecimal("200.00"),
                new BigDecimal("35.0"),
                null,
                45500L
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post("/api/abastecimentos")
                .then()
                .statusCode(409)
                .body("message", containsString("Combustível inválido"));
    }

    @Test
    void deveRejeitar_QuilometragemMenorQueAtual() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();
        Long veiculoId = TestDataFactory.createVeiculo(user.id, token); // km atual = 45000

        AbastecimentoCreateRequest request = new AbastecimentoCreateRequest(
                veiculoId,
                LocalDate.now(),
                TipoCombustivel.GASOLINA,
                new BigDecimal("200.00"),
                new BigDecimal("35.0"),
                null,
                40000L  // <- menor que 45000
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post("/api/abastecimentos")
                .then()
                .statusCode(409)
                .body("message", containsString("Quilometragem"));
    }
}