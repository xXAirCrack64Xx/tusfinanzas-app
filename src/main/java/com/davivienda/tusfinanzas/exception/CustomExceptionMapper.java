package com.davivienda.tusfinanzas.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("message", ex.getMessage());

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        if (ex instanceof UsuarioNoEncontradoException) {
            status = Response.Status.NOT_FOUND;
        } else if (ex instanceof ContrasenaInvalidaException) {
            status = Response.Status.UNAUTHORIZED;
        } else if (ex instanceof AccesoDenegadoException) {
            status = Response.Status.FORBIDDEN;
        } else if (ex instanceof DatosFinancierosNoEncontradosException) {
            status = Response.Status.NOT_FOUND;
        } else if (ex instanceof GastoNoEncontradoException) {
            status = Response.Status.NOT_FOUND;
        } else if (ex instanceof SinPermisoEliminarGastoException) {
            status = Response.Status.FORBIDDEN;
        } else if (ex instanceof IngresoNotFoundException) {
            status = Response.Status.NOT_FOUND;
        } else if (ex instanceof UnauthorizedAccessException
                || ex instanceof UnauthorizedIngresoAccessException) {
            status = Response.Status.FORBIDDEN;
        }

        error.put("status", status.getStatusCode());
        return Response.status(status).entity(error).build();
    }
}
