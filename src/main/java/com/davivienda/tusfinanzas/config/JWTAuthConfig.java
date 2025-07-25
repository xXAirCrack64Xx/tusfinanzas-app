package com.davivienda.tusfinanzas.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "jwt")
public interface JWTAuthConfig {
    @WithDefault("https://davivienda-finanzas")
    String issuer();

    @WithDefault("60") // minutos
    long expirationMinutes();
}
