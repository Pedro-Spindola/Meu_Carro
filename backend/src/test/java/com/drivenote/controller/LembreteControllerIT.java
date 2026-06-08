package com.drivenote.controller;

import com.drivenote.TestDataFactory;
import com.drivenote.dto.LembreteCreateRequest;
import com.drivenote.dto.LembreteUpdateRequest;
import com.drivenote.enums.TipoLembrete;
import com.drivenote.it.BaseIntegrationTest;
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
                        .body("titulo", equalTo("IPVA 2026"))
                        .body("status", equalTo("PENDENTE"))
                        .extract()
                        .path("id");

        Long lembreteId = ((Number) idNumber).longValue();

        // Listar
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/lembretes/veiculo/" + veiculoId)
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));

        // Concluir
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .put("/api/lembretes/" + lembreteId + "/concluir")
                .then()
                .statusCode(200)
                .body("status", equalTo("CONCLUIDO"));
    }

    @Test
    void deveAtualizarLembrete() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();
        Long veiculoId = TestDataFactory.createVeiculo(user.id, token);

        LembreteCreateRequest criar = new LembreteCreateRequest(
                veiculoId, "Seguro", "Vencimento em março",
                TipoLembrete.SEGURO, LocalDate.now().plusDays(30)
        );

        Long lembreteId = ((Number) given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(criar)
                .when()
                .post("/api/lembretes")
                .then()
                .statusCode(200)
                .extract()
                .path("id")).longValue();

        LembreteUpdateRequest update = new LembreteUpdateRequest(
                "Seguro Atualizado",
                "Vencimento em abril",
                TipoLembrete.SEGURO,
                LocalDate.now().plusDays(60)
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(update)
                .when()
                .put("/api/lembretes/" + lembreteId)
                .then()
                .statusCode(200)
                .body("titulo", equalTo("Seguro Atualizado"))
                .body("status", equalTo("PENDENTE"));
    }

    @Test
    void deveDeletarLembrete() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();
        String token = TestDataFactory.loginAndGetTokens(user.email, user.senha).token();
        Long veiculoId = TestDataFactory.createVeiculo(user.id, token);

        LembreteCreateRequest criar = new LembreteCreateRequest(
                veiculoId, "IPVA", "Para deletar",
                TipoLembrete.IPVA, LocalDate.now().plusDays(5)
        );

        Long lembreteId = ((Number) given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(criar)
                .when()
                .post("/api/lembretes")
                .then()
                .statusCode(200)
                .extract()
                .path("id")).longValue();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/lembretes/" + lembreteId)
                .then()
                .statusCode(204);
    }
}