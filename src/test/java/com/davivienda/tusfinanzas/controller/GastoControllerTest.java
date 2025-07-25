package com.davivienda.tusfinanzas.controller;

import com.davivienda.tusfinanzas.CustomTestProfile;
import com.davivienda.tusfinanzas.dto.GastoDTO;
import com.davivienda.tusfinanzas.entity.Gasto;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.service.GastoService;
import com.davivienda.tusfinanzas.service.UserService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestProfile(CustomTestProfile.class)
class GastoControllerTest {

    @InjectMock
    GastoService gastoService;

    @InjectMock
    UserService userService;

    private User usuario;

    @BeforeEach
    void setup() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setUsername("testUser");
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"USER"})
    void testCreateGasto() {
        GastoDTO dto = new GastoDTO(100.0, "Compra", LocalDate.of(2025, 7, 24), "ALIMENTACION");
        Gasto gasto = new Gasto();
        gasto.setId(1L);
        gasto.setDescripcion("Compra");
        gasto.setMonto(100.0);
        gasto.setFecha(LocalDate.of(2025, 7, 24));
        gasto.setTipo("ALIMENTACION");
        gasto.setUsuario(usuario);

        when(userService.findByUsername("testUser")).thenReturn(usuario);
        when(gastoService.createGasto(any(GastoDTO.class), eq(usuario))).thenReturn(gasto);

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/gastos")
                .then()
                .statusCode(200)
                .body("descripcion", is("Compra"))
                .body("monto", is(100.0F))
                .body("tipo", is("ALIMENTACION"));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"USER"})
    void testGetGastos() {
        Gasto gasto = new Gasto();
        gasto.setId(1L);
        gasto.setDescripcion("Compra");
        gasto.setMonto(100.0);
        gasto.setFecha(LocalDate.of(2025, 7, 24));
        gasto.setTipo("ALIMENTACION");
        gasto.setUsuario(usuario);

        when(userService.findByUsername("testUser")).thenReturn(usuario);
        when(gastoService.getGastosByUser(usuario)).thenReturn(List.of(gasto));

        given()
                .when()
                .get("/gastos")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].descripcion", is("Compra"));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"USER"})
    void testDeleteGasto() {
        when(userService.findByUsername("testUser")).thenReturn(usuario);

        given()
                .when()
                .delete("/gastos/1")
                .then()
                .statusCode(204);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"USER"})
    void testFiltrarGastos() {
        Gasto gasto = new Gasto();
        gasto.setId(1L);
        gasto.setDescripcion("Compra");
        gasto.setMonto(100.0);
        gasto.setFecha(LocalDate.of(2025, 7, 24));
        gasto.setTipo("ALIMENTACION");
        gasto.setUsuario(usuario);

        when(userService.findByUsername("testUser")).thenReturn(usuario);
        when(gastoService.getGastosByFilters(eq(usuario), any(), any(), any()))
                .thenReturn(List.of(gasto));

        given()
                .queryParam("tipo", "ALIMENTACION")
                .when()
                .get("/gastos/filtrar")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].tipo", is("ALIMENTACION"));
    }
}


