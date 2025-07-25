package com.davivienda.tusfinanzas.util;

import com.davivienda.tusfinanzas.config.JWTAuthConfig;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JwtUtil {

    @Inject
    JWTAuthConfig jwtConfig;

    public String generateToken(String username, String role) {
        return Jwt.issuer(jwtConfig.issuer())
                .upn(username)
                .groups(Set.of(role))
                .expiresIn(Duration.ofMinutes(jwtConfig.expirationMinutes()))
                .sign();
    }
}



