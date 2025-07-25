package com.davivienda.tusfinanzas.service;

import com.davivienda.tusfinanzas.dto.RegisterRequest;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.exception.ContrasenaInvalidaException;
import com.davivienda.tusfinanzas.exception.UsuarioNoEncontradoException;
import com.davivienda.tusfinanzas.repository.UserRepository;
import com.davivienda.tusfinanzas.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordService passwordService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("nuevoUser", "pass123");
        when(userRepository.findByUsername("nuevoUser")).thenReturn(null);
        when(passwordService.hashPassword("pass123")).thenReturn("hashedPass");

        authService.register(request);

        verify(userRepository).persist(any(User.class));
    }

    @Test
    void testRegister_UserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("existente", "pass123");
        when(userRepository.findByUsername("existente")).thenReturn(new User());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(request));
        assertEquals("Usuario ya existe", ex.getMessage());
    }

    @Test
    void testAuthenticate_Success() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("hashedPass");
        user.setRole("USER");

        when(userRepository.findByUsername("john")).thenReturn(user);
        when(passwordService.verifyPassword("1234", "hashedPass")).thenReturn(true);
        when(jwtUtil.generateToken("john", "USER")).thenReturn("fakeToken");

        String token = authService.authenticate("john", "1234");

        assertEquals("fakeToken", token);
    }

    @Test
    void testAuthenticate_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        assertThrows(UsuarioNoEncontradoException.class,
                () -> authService.authenticate("unknown", "pass"));
    }

    @Test
    void testAuthenticate_InvalidPassword() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("hashedPass");
        user.setRole("USER");

        when(userRepository.findByUsername("john")).thenReturn(user);
        when(passwordService.verifyPassword("wrongPass", "hashedPass")).thenReturn(false);

        assertThrows(ContrasenaInvalidaException.class,
                () -> authService.authenticate("john", "wrongPass"));
    }
}
