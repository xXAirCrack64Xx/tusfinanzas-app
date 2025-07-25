package com.davivienda.tusfinanzas.exception;

public class IngresoNotFoundException extends RuntimeException {
    public IngresoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
