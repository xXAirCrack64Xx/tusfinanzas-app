package com.davivienda.tusfinanzas.service;

import com.davivienda.tusfinanzas.dto.RegisterRequest;

public interface IAuthService {

    void register(RegisterRequest request);

    String authenticate(String username, String password);
}
