package com.davivienda.tusfinanzas.controller;

import com.davivienda.tusfinanzas.CustomTestProfile;
import com.davivienda.tusfinanzas.dto.AuthRequest;
import com.davivienda.tusfinanzas.dto.RegisterRequest;
import com.davivienda.tusfinanzas.service.AuthService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@TestProfile(CustomTestProfile.class)    // <–– usa el perfil renombrado
class AuthControllerTest {

    @InjectMock
    AuthService authService;

    @Test
    void testLoginSuccess() {
        Mockito.when(authService.authenticate("john", "1234"))
                .thenReturn("fake-jwt-token");

        given()
                .contentType(ContentType.JSON)
                .body(new AuthRequest("john", "1234"))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", equalTo("fake-jwt-token"));
    }

    @Test
    void testRegisterSuccess() {
        Mockito.doNothing().when(authService).register(Mockito.any(RegisterRequest.class));

        given()
                .contentType(ContentType.JSON)
                .body(new RegisterRequest("newuser", "password"))
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .body(equalTo("Usuario registrado"));
    }
}

