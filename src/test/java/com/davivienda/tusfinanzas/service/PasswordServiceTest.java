package com.davivienda.tusfinanzas.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PasswordServiceTest {

    @Inject
    PasswordService passwordService;

    @Test
    public void testHashPasswordNotNullAndDifferent() {
        String plainPassword = "MiContraseñaSegura123";
        String hashed = passwordService.hashPassword(plainPassword);

        Assertions.assertNotNull(hashed, "El hash no debe ser nulo");
        Assertions.assertNotEquals(plainPassword, hashed, "El hash no debe ser igual al texto plano");
    }

    @Test
    public void testVerifyPasswordCorrect() {
        String plainPassword = "OtraClaveSegura456";
        String hashed = passwordService.hashPassword(plainPassword);

        boolean result = passwordService.verifyPassword(plainPassword, hashed);
        Assertions.assertTrue(result, "La verificación debe ser correcta con la misma contraseña");
    }

    @Test
    public void testVerifyPasswordIncorrect() {
        String plainPassword = "ClaveOriginal789";
        String hashed = passwordService.hashPassword(plainPassword);

        boolean result = passwordService.verifyPassword("ClaveDistinta", hashed);
        Assertions.assertFalse(result, "La verificación debe fallar con una contraseña distinta");
    }
}
