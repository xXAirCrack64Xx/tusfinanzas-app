package com.davivienda.tusfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // <-- Esto agrega el constructor (username, password)
public class RegisterRequest {
    private String username;
    private String password;
}
