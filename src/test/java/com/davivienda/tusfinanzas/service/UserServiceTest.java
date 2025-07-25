package com.davivienda.tusfinanzas.service;

import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.repository.UserRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class UserServiceTest {

    @Inject
    UserService userService;

    @InjectMock
    UserRepository userRepository;

    @Test
    public void testFindByUsernameFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("usuarioPrueba");
        user.setPassword("hashedPass");
        user.setRole("USER");

        Mockito.when(userRepository.findByUsername("usuarioPrueba")).thenReturn(user);

        User result = userService.findByUsername("usuarioPrueba");

        Assertions.assertNotNull(result, "El usuario no debe ser nulo");
        Assertions.assertEquals("usuarioPrueba", result.getUsername());
        Assertions.assertEquals("hashedPass", result.getPassword());
    }

    @Test
    public void testFindByUsernameNotFound() {
        Mockito.when(userRepository.findByUsername("inexistente")).thenReturn(null);

        User result = userService.findByUsername("inexistente");

        Assertions.assertNull(result, "Debe devolver null si el usuario no existe");
    }
}
