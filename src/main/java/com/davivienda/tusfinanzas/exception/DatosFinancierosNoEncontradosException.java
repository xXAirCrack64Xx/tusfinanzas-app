package com.davivienda.tusfinanzas.exception;

public class DatosFinancierosNoEncontradosException extends RuntimeException {
    public DatosFinancierosNoEncontradosException(String mensaje) {
        super(mensaje);
    }
}
