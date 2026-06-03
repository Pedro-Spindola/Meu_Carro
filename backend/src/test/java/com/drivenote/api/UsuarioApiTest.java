package com.drivenote.api;

import com.drivenote.dto.UsuarioCreateRequest;
import com.drivenote.it.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class UsuarioApiTest extends BaseIntegrationTest {

    @Test
    void deveCriarUsuarioViaApi() {
        UsuarioCreateRequest request = new UsuarioCreateRequest(
                "Maria",
                "maria@email.com",
                "123456",
                "11888888888"
        );

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/usuarios")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("email", equalTo("maria@email.com"));
    }
}