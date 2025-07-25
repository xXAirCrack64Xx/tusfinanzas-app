package com.davivienda.tusfinanzas.service;

import com.davivienda.tusfinanzas.dto.GastoDTO;
import com.davivienda.tusfinanzas.entity.Gasto;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.repository.GastoRepository;
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
public class GastoServiceTest {

    @Inject
    GastoService gastoService;

    @InjectMock
    GastoRepository gastoRepository;

    @Test
    public void testCreateGasto() {
        User user = new User();
        user.setId(1L);

        GastoDTO dto = new GastoDTO();
        dto.setMonto(100.0);
        dto.setDescripcion("Compra supermercado");
        dto.setFecha(LocalDate.of(2025, 7, 24));
        dto.setTipo("ALIMENTACION");

        Mockito.doAnswer(invocation -> {
            Gasto gasto = invocation.getArgument(0);
            gasto.setId(1L);
            return null;
        }).when(gastoRepository).persist(any(Gasto.class));

        Gasto result = gastoService.createGasto(dto, user);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Compra supermercado", result.getDescripcion());
        Assertions.assertEquals(100.0, result.getMonto());
        Assertions.assertEquals("ALIMENTACION", result.getTipo());
        Assertions.assertEquals(user, result.getUsuario());
    }

    @Test
    public void testGetGastosByUser() {
        User user = new User();
        user.setId(1L);

        Gasto gasto = new Gasto();
        gasto.setId(1L);
        gasto.setDescripcion("Transporte");
        gasto.setMonto(50.0);
        gasto.setUsuario(user);

        Mockito.when(gastoRepository.findByUserId(1L))
                .thenReturn(Collections.singletonList(gasto));

        List<Gasto> gastos = gastoService.getGastosByUser(user);

        Assertions.assertEquals(1, gastos.size());
        Assertions.assertEquals("Transporte", gastos.get(0).getDescripcion());
    }

    @Test
    public void testDeleteGastoCorrectUser() {
        User user = new User();
        user.setId(1L);

        Gasto gasto = new Gasto();
        gasto.setId(1L);
        gasto.setUsuario(user);

        Mockito.when(gastoRepository.findById(1L)).thenReturn(gasto);

        Assertions.assertDoesNotThrow(() -> gastoService.deleteGasto(1L, user));
        Mockito.verify(gastoRepository).delete(gasto);
    }

    @Test
    public void testDeleteGastoWrongUser() {
        User user = new User();
        user.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Gasto gasto = new Gasto();
        gasto.setId(1L);
        gasto.setUsuario(otherUser);

        Mockito.when(gastoRepository.findById(1L)).thenReturn(gasto);

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () ->
                gastoService.deleteGasto(1L, user));
        Assertions.assertTrue(exception.getMessage().contains("sin permisos"));
    }

    @Test
    public void testGetGastosByFiltersWithFilters() {
        User user = new User();
        user.setId(1L);

        Gasto gasto = new Gasto();
        gasto.setId(1L);
        gasto.setDescripcion("Gasolina");
        gasto.setUsuario(user);

        Mockito.when(gastoRepository.findByFilters(eq(1L), any(), any(), eq("TRANSPORTE")))
                .thenReturn(Collections.singletonList(gasto));

        List<Gasto> gastos = gastoService.getGastosByFilters(user,
                LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 31), "TRANSPORTE");

        Assertions.assertEquals(1, gastos.size());
        Assertions.assertEquals("Gasolina", gastos.get(0).getDescripcion());
    }

    @Test
    public void testGetGastosByFiltersNoFilters() {
        User user = new User();
        user.setId(1L);

        Gasto gasto = new Gasto();
        gasto.setId(1L);
        gasto.setDescripcion("Ropa");
        gasto.setUsuario(user);

        Mockito.when(gastoRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(gasto));

        List<Gasto> gastos = gastoService.getGastosByFilters(user, null, null, null);

        Assertions.assertEquals(1, gastos.size());
        Assertions.assertEquals("Ropa", gastos.get(0).getDescripcion());
    }
}
