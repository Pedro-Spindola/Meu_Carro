package com.drivenote.controller;

import com.drivenote.it.BaseIntegrationTest;
import com.drivenote.TestDataFactory;
import com.drivenote.dto.LembreteCreateRequest;
import com.drivenote.enums.TipoLembrete;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class LembreteControllerIT extends BaseIntegrationTest {

    @Test
    void deveCriarListarEConcluirLembrete() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();
        Long veiculoId = TestDataFactory.createVeiculo(user.id, token);

        LembreteCreateRequest request = new LembreteCreateRequest(
                veiculoId,
                "IPVA 2026",
                "Vencimento em janeiro",
                TipoLembrete.IPVA,
                LocalDate.now().plusDays(10)
        );

        Number idNumber =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + token)
                        .body(request)
                        .when()
                        .post("/api/lembretes")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("id");

        Long lembreteId = idNumber.longValue();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/lembretes/veiculo/" + veiculoId)
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .put("/api/lembretes/" + lembreteId + "/concluir")
                .then()
                .statusCode(200)
                .body("status", notNullValue());
    }
}