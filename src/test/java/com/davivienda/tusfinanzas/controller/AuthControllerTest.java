package com.davivienda.tusfinanzas.controller;

import com.davivienda.tusfinanzas.dto.AuthRequest;
import com.davivienda.tusfinanzas.dto.AuthResponse;
import com.davivienda.tusfinanzas.dto.RegisterRequest;
import com.davivienda.tusfinanzas.service.AuthService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
class AuthControllerTest {

    @InjectMock
    AuthService authService;

    @Test
    void testLoginSuccess() {
        // Arrange: Simular token retornado por el servicio
        Mockito.when(authService.authenticate("john", "1234"))
                .thenReturn("fake-jwt-token");

        // Act & Assert
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
        // Arrange: Simulamos que no lanza excepción al registrar
        RegisterRequest request = new RegisterRequest("newuser", "password");
        Mockito.doNothing().when(authService).register(Mockito.any(RegisterRequest.class));

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .body(equalTo("Usuario registrado"));
    }
}
