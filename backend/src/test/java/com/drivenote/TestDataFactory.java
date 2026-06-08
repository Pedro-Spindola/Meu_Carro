package com.drivenote;

import com.drivenote.dto.*;
import com.drivenote.enums.TipoCombustivel;
import com.drivenote.enums.TipoManutencao;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class TestDataFactory {

    public static class UserData {
        public final Long id;
        public final String email;
        public final String senha;

        public UserData(Long id, String email, String senha) {
            this.id = id;
            this.email = email;
            this.senha = senha;
        }
    }

    public static UserData createUsuario() {
        String email = "user_" + UUID.randomUUID() + "@drivenote.com";
        String senha = "123456";

        UsuarioCreateRequest request = new UsuarioCreateRequest(
                "Usuário Teste",
                email,
                senha,
                "62999999999"
        );

        Number idNumber =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/api/usuarios")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .path("id");

        Long id = idNumber.longValue();
        return new UserData(id, email, senha);
    }

    // login que retorna token + cookie refresh
    public static TestAuthData loginAndGetAuthData(String email, String senha) {
        AuthRequest request = new AuthRequest(email, senha);

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/api/auth/login")
                        .then()
                        .statusCode(200)
                        .extract();

        AuthResponse auth = response.as(AuthResponse.class);
        Cookie refreshCookie = response.detailedCookies().get("refreshToken");

        return new TestAuthData(auth, refreshCookie);
    }

    public static AuthResponse loginAndGetTokens(String email, String senha) {
        AuthRequest request = new AuthRequest(email, senha);

        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/auth/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(AuthResponse.class);
    }

    public static AuthRequest loginRequest(String email, String senha) {
        return new AuthRequest(email, senha);
    }

    public static Long createVeiculo(Long usuarioId, String token) {
        String placa = "TST" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        VeiculoCreateRequest request = new VeiculoCreateRequest(
                "Honda",
                "Civic",
                2020,
                placa,
                TipoCombustivel.GASOLINA,
                45000L
        );

        Number idNumber =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + token)
                        .body(request)
                        .when()
                        .post("/api/usuarios/" + usuarioId + "/veiculos")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("id");

        return idNumber.longValue();
    }

    public static Long createPlano(Long veiculoId, String token) {
        PlanoManutencaoCreateRequest request = new PlanoManutencaoCreateRequest(
                veiculoId,
                TipoManutencao.OLEO_MOTOR,
                "Troca a cada 10.000 km",
                10000L,
                180,
                true
        );

        Number idNumber =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + token)
                        .body(request)
                        .when()
                        .post("/api/planos-manutencao")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("id");

        return idNumber.longValue();
    }

    public static Long createManutencao(Long veiculoId, Long planoId, String token) {
        ManutencaoCreateRequest request = new ManutencaoCreateRequest(
                veiculoId,
                planoId,
                "Troca de óleo e filtro",
                LocalDate.now().minusDays(5),
                46000L,
                java.math.BigDecimal.valueOf(320.00)
        );

        Number idNumber =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + token)
                        .body(request)
                        .when()
                        .post("/api/manutencoes")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("id");

        return idNumber.longValue();
    }
}