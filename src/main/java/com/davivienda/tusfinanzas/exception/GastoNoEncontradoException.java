package com.davivienda.tusfinanzas.exception;

public class GastoNoEncontradoException extends RuntimeException {
    public GastoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
