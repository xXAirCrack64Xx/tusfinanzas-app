package com.davivienda.tusfinanzas.repository;

import com.davivienda.tusfinanzas.entity.Gasto;
import com.davivienda.tusfinanzas.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@QuarkusTest
@TestTransaction // asegura rollback automático después de cada test
public class GastoRepositoryTest {

    @Inject
    GastoRepository gastoRepository;

    @Inject
    UserRepository userRepository;

    @Test
    public void testFindByUserId() {
        // Crear usuario
        User user = new User();
        user.setUsername("usuarioGasto");
        user.setPassword("hashpass");
        user.setRole("USER");
        userRepository.persist(user);

        // Crear gasto
        Gasto gasto = new Gasto();
        gasto.setMonto(200.0);
        gasto.setDescripcion("Compra mercado");
        gasto.setFecha(LocalDate.now());
        gasto.setTipo("ALIMENTACION");
        gasto.setUsuario(user);
        gastoRepository.persist(gasto);

        // Ejecutar query
        List<Gasto> result = gastoRepository.findByUserId(user.getId());

        // Verificaciones
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Compra mercado", result.get(0).getDescripcion());
    }

    @Test
    public void testFindByFilters() {
        // Crear usuario
        User user = new User();
        user.setUsername("usuarioFiltro");
        user.setPassword("hashpass");
        user.setRole("USER");
        userRepository.persist(user);

        // Crear gastos
        Gasto gasto1 = new Gasto();
        gasto1.setMonto(100.0);
        gasto1.setDescripcion("Cine");
        gasto1.setFecha(LocalDate.of(2025, 7, 20));
        gasto1.setTipo("ENTRETENIMIENTO");
        gasto1.setUsuario(user);

        Gasto gasto2 = new Gasto();
        gasto2.setMonto(50.0);
        gasto2.setDescripcion("Almuerzo");
        gasto2.setFecha(LocalDate.of(2025, 7, 21));
        gasto2.setTipo("ALIMENTACION");
        gasto2.setUsuario(user);

        gastoRepository.persist(gasto1);
        gastoRepository.persist(gasto2);

        // Filtrar por tipo
        List<Gasto> result = gastoRepository.findByFilters(user.getId(),
                LocalDate.of(2025, 7, 19),
                LocalDate.of(2025, 7, 21),
                "ENTRETENIMIENTO");

        // Verificaciones
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Cine", result.get(0).getDescripcion());
    }
}
