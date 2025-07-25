package com.davivienda.tusfinanzas.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordService implements IPasswordService {

    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean verifyPassword(String plain, String hashed) {
        return BCrypt.checkpw(plain, hashed);
    }
}
