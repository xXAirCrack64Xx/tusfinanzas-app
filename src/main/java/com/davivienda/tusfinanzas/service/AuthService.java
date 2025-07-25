package com.davivienda.tusfinanzas.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.davivienda.tusfinanzas.dto.RegisterRequest;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.repository.UserRepository;
import com.davivienda.tusfinanzas.util.JwtUtil;
import com.davivienda.tusfinanzas.exception.UsuarioNoEncontradoException;
import com.davivienda.tusfinanzas.exception.ContrasenaInvalidaException;
import jakarta.transaction.Transactional;

@Transactional
@ApplicationScoped
public class AuthService implements IAuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordService passwordService;

    @Inject
    JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Usuario ya existe");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordService.hashPassword(request.getPassword()));
        user.setRole("USER");
        userRepository.persist(user);
    }

    @Override
    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }

        if (!passwordService.verifyPassword(password, user.getPassword())) {
            throw new ContrasenaInvalidaException("Contraseña incorrecta");
        }

        return jwtUtil.generateToken(username, user.getRole());
    }
}

