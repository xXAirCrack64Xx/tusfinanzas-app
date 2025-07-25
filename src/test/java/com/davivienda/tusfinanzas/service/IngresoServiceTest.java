package com.davivienda.tusfinanzas.service;

import com.davivienda.tusfinanzas.dto.IngresoDTO;
import com.davivienda.tusfinanzas.entity.Ingreso;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.repository.IngresoRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
public class IngresoServiceTest {

    @Inject
    IngresoService ingresoService;

    @InjectMock
    IngresoRepository ingresoRepository;

    @Test
    public void testCreateIngreso() {
        User user = new User();
        user.setId(1L);

        IngresoDTO dto = new IngresoDTO();
        dto.setMonto(500.0);
        dto.setDescripcion("Sueldo");
        dto.setFecha(LocalDate.of(2025, 7, 24));
        dto.setTipo("SALARIO");

        Mockito.doAnswer(invocation -> {
            Ingreso ingreso = invocation.getArgument(0);
            ingreso.setId(1L);
            return null;
        }).when(ingresoRepository).persist(any(Ingreso.class));

        Ingreso result = ingresoService.createIngreso(dto, user);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Sueldo", result.getDescripcion());
        Assertions.assertEquals(500.0, result.getMonto());
        Assertions.assertEquals("SALARIO", result.getTipo());
        Assertions.assertEquals(user, result.getUsuario());
    }

    @Test
    public void testGetIngresosByUser() {
        User user = new User();
        user.setId(1L);

        Ingreso ingreso = new Ingreso();
        ingreso.setId(1L);
        ingreso.setDescripcion("Venta producto");
        ingreso.setMonto(200.0);
        ingreso.setUsuario(user);

        Mockito.when(ingresoRepository.findByUserId(1L))
                .thenReturn(Collections.singletonList(ingreso));

        List<Ingreso> ingresos = ingresoService.getIngresosByUser(user);

        Assertions.assertEquals(1, ingresos.size());
        Assertions.assertEquals("Venta producto", ingresos.get(0).getDescripcion());
    }

    @Test
    public void testDeleteIngresoCorrectUser() {
        User user = new User();
        user.setId(1L);

        Ingreso ingreso = new Ingreso();
        ingreso.setId(1L);
        ingreso.setUsuario(user);

        Mockito.when(ingresoRepository.findById(1L)).thenReturn(ingreso);

        Assertions.assertDoesNotThrow(() -> ingresoService.deleteIngreso(1L, user));
        Mockito.verify(ingresoRepository).delete(ingreso);
    }

    @Test
    public void testDeleteIngresoWrongUser() {
        User user = new User();
        user.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Ingreso ingreso = new Ingreso();
        ingreso.setId(1L);
        ingreso.setUsuario(otherUser);

        Mockito.when(ingresoRepository.findById(1L)).thenReturn(ingreso);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () ->
                ingresoService.deleteIngreso(1L, user));
        Assertions.assertFalse(exception.getMessage().contains("sin permisos"));
    }

    @Test
    public void testGetIngresosByFilters() {
        User user = new User();
        user.setId(1L);

        Ingreso ingreso = new Ingreso();
        ingreso.setId(1L);
        ingreso.setDescripcion("Proyecto freelance");
        ingreso.setUsuario(user);

        Mockito.when(ingresoRepository.findByFilters(eq(1L), any(), any(), eq("FREELANCE")))
                .thenReturn(Arrays.asList(ingreso));

        List<Ingreso> ingresos = ingresoService.getIngresosByFilters(user,
                LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 31), "FREELANCE");

        Assertions.assertEquals(1, ingresos.size());
        Assertions.assertEquals("Proyecto freelance", ingresos.get(0).getDescripcion());
    }
}
