package com.davivienda.tusfinanzas.repository;

import com.davivienda.tusfinanzas.entity.Ingreso;
import com.davivienda.tusfinanzas.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@QuarkusTest
@TestTransaction
public class IngresoRepositoryTest {

    @Inject
    IngresoRepository ingresoRepository;

    @Inject
    UserRepository userRepository;

    @Test
    public void testFindByUserId() {
        // Crear usuario
        User user = new User();
        user.setUsername("usuarioIngreso");
        user.setPassword("hashpass");
        user.setRole("USER");
        userRepository.persist(user);

        // Crear ingreso
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(500.0);
        ingreso.setDescripcion("Salario");
        ingreso.setFecha(LocalDate.now());
        ingreso.setTipo("SALARIO");
        ingreso.setUsuario(user);
        ingresoRepository.persist(ingreso);

        // Ejecutar query
        List<Ingreso> result = ingresoRepository.findByUserId(user.getId());

        // Verificaciones
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Salario", result.get(0).getDescripcion());
    }

    @Test
    public void testFindByFilters() {
        // Crear usuario
        User user = new User();
        user.setUsername("usuarioFiltroIngreso");
        user.setPassword("hashpass");
        user.setRole("USER");
        userRepository.persist(user);

        // Crear ingresos
        Ingreso ingreso1 = new Ingreso();
        ingreso1.setMonto(1000.0);
        ingreso1.setDescripcion("Proyecto freelance");
        ingreso1.setFecha(LocalDate.of(2025, 7, 20));
        ingreso1.setTipo("FREELANCE");
        ingreso1.setUsuario(user);

        Ingreso ingreso2 = new Ingreso();
        ingreso2.setMonto(2000.0);
        ingreso2.setDescripcion("Bonificación");
        ingreso2.setFecha(LocalDate.of(2025, 7, 21));
        ingreso2.setTipo("BONO");
        ingreso2.setUsuario(user);

        ingresoRepository.persist(ingreso1);
        ingresoRepository.persist(ingreso2);

        // Filtrar por tipo FREELANCE
        List<Ingreso> result = ingresoRepository.findByFilters(
                user.getId(),
                LocalDate.of(2025, 7, 19),
                LocalDate.of(2025, 7, 21),
                "FREELANCE");

        // Verificaciones
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Proyecto freelance", result.get(0).getDescripcion());
    }
}
