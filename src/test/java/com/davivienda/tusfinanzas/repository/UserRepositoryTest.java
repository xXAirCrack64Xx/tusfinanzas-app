package com.davivienda.tusfinanzas.repository;

import com.davivienda.tusfinanzas.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestTransaction
public class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        // Crear un usuario
        User user = new User();
        user.setUsername("usuarioPrueba");
        user.setPassword("hashpass");
        user.setRole("USER");
        userRepository.persist(user);

        // Buscar por username
        User result = userRepository.findByUsername("usuarioPrueba");

        // Verificaciones
        Assertions.assertNotNull(result);
        Assertions.assertEquals("usuarioPrueba", result.getUsername());
        Assertions.assertEquals("USER", result.getRole());
    }

    @Test
    public void testFindByUsernameNotFound() {
        User result = userRepository.findByUsername("no_existe");
        Assertions.assertNull(result);
    }
}
