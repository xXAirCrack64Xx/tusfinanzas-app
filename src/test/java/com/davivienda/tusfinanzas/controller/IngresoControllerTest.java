package com.davivienda.tusfinanzas.controller;

import com.davivienda.tusfinanzas.dto.IngresoDTO;
import com.davivienda.tusfinanzas.entity.Ingreso;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.service.IngresoService;
import com.davivienda.tusfinanzas.service.UserService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
public class IngresoControllerTest {

    @InjectMock
    IngresoService ingresoService;

    @InjectMock
    UserService userService;

    @Test
    @TestSecurity(user = "testuser", roles = {"USER"})
    public void testCreateIngreso() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        Ingreso mockIngreso = new Ingreso();
        mockIngreso.setId(10L);
        mockIngreso.setMonto(500.0);
        mockIngreso.setDescripcion("Salario");
        mockIngreso.setFecha(LocalDate.of(2025, 7, 24));
        mockIngreso.setTipo("SALARIO");
        mockIngreso.setUsuario(mockUser);

        Mockito.when(userService.findByUsername("testuser")).thenReturn(mockUser);
        Mockito.when(ingresoService.createIngreso(any(IngresoDTO.class), eq(mockUser)))
                .thenReturn(mockIngreso);

        IngresoDTO request = new IngresoDTO();
        request.setMonto(500.0);
        request.setDescripcion("Salario");
        request.setFecha(LocalDate.of(2025, 7, 24));
        request.setTipo("SALARIO");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/ingresos")
                .then()
                .statusCode(201)
                .body("descripcion", equalTo("Salario"));
    }

    @Test
    @TestSecurity(user = "testuser", roles = {"USER"})
    public void testGetIngresos() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        Ingreso ingreso = new Ingreso();
        ingreso.setId(1L);
        ingreso.setDescripcion("Salario");
        ingreso.setMonto(500.0);
        ingreso.setFecha(LocalDate.of(2025, 7, 24));
        ingreso.setTipo("SALARIO");
        ingreso.setUsuario(mockUser);

        Mockito.when(userService.findByUsername("testuser")).thenReturn(mockUser);
        Mockito.when(ingresoService.getIngresosByUser(mockUser))
                .thenReturn(Collections.singletonList(ingreso));

        given()
                .when().get("/ingresos")
                .then()
                .statusCode(200)
                .body("[0].descripcion", equalTo("Salario"));
    }

    @Test
    @TestSecurity(user = "testuser", roles = {"USER"})
    public void testDeleteIngreso() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        Mockito.when(userService.findByUsername("testuser")).thenReturn(mockUser);

        given()
                .when().delete("/ingresos/1")
                .then()
                .statusCode(204);
    }

    @Test
    @TestSecurity(user = "testuser", roles = {"USER"})
    public void testFiltrarIngresos() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        Ingreso ingreso = new Ingreso();
        ingreso.setId(1L);
        ingreso.setDescripcion("Bono");
        ingreso.setMonto(200.0);
        ingreso.setFecha(LocalDate.of(2025, 7, 20));
        ingreso.setTipo("BONO");
        ingreso.setUsuario(mockUser);

        Mockito.when(userService.findByUsername("testuser")).thenReturn(mockUser);
        Mockito.when(ingresoService.getIngresosByFilters(eq(mockUser), any(), any(), any()))
                .thenReturn(List.of(ingreso));

        given()
                .queryParam("fechaInicio", "2025-07-01")
                .queryParam("fechaFin", "2025-07-31")
                .queryParam("tipo", "BONO")
                .when().get("/ingresos/filtrar")
                .then()
                .statusCode(200)
                .body("[0].descripcion", equalTo("Bono"));
    }
}
