package com.drivenote.controller;

import com.drivenote.it.BaseIntegrationTest;
import com.drivenote.TestAuthData;
import com.drivenote.TestDataFactory;
import com.drivenote.dto.AuthResponse;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

class AuthRefreshLogoutIT extends BaseIntegrationTest {

    @Test
    void deveRefrescarTokenERotacionarRefresh() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();

        TestAuthData loginData = TestDataFactory.loginAndGetAuthData(user.email, user.senha);

        Cookie refreshCookie = loginData.refreshCookie();

        var refreshedResp =
                given()
                        .contentType(ContentType.JSON)
                        .cookie(refreshCookie)
                        .when()
                        .post("/api/auth/refresh")
                        .then()
                        .statusCode(200)
                        .body("token", notNullValue())
                        .extract();

        AuthResponse refreshed = refreshedResp.as(AuthResponse.class);
        Cookie newRefreshCookie = refreshedResp.detailedCookies().get("refreshToken");

        org.junit.jupiter.api.Assertions.assertNotEquals(
                refreshCookie.getValue(), newRefreshCookie.getValue()
        );
    }

    @Test
    void deveFazerLogoutEInvalidarAccessERefreshGlobal() {
        TestDataFactory.UserData user = TestDataFactory.createUsuario();

        TestAuthData loginData = TestDataFactory.loginAndGetAuthData(user.email, user.senha);

        AuthResponse login = loginData.auth();
        Cookie refreshCookie = loginData.refreshCookie();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + login.token())
                .cookie(refreshCookie)
                .when()
                .post("/api/auth/logout")
                .then()
                .statusCode(200);

        given()
                .header("Authorization", "Bearer " + login.token())
                .when()
                .get("/api/dashboard/veiculo/1")
                .then()
                .statusCode(401);

        given()
                .contentType(ContentType.JSON)
                .cookie(refreshCookie)
                .when()
                .post("/api/auth/refresh")
                .then()
                .statusCode(401);
    }

    @Test
    void deveFalharRefreshSemCookie() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/api/auth/refresh")
                .then()
                .statusCode(401);
    }
}