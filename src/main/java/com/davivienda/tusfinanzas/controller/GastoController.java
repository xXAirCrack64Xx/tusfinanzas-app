package com.davivienda.tusfinanzas.controller;

import com.davivienda.tusfinanzas.dto.GastoDTO;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.exception.GastoNoEncontradoException;
import com.davivienda.tusfinanzas.exception.SinPermisoEliminarGastoException;
import com.davivienda.tusfinanzas.service.GastoService;
import com.davivienda.tusfinanzas.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;

@Path("/gastos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("USER")
public class GastoController {

    @Inject
    GastoService gastoService;

    @Inject
    UserService userService;

    @Context
    SecurityContext securityContext;

    @POST
    public Response createGasto(GastoDTO dto) {
        User usuario = userService.findByUsername(securityContext.getUserPrincipal().getName());
        return Response.ok(gastoService.createGasto(dto, usuario)).build();
    }

    @GET
    public Response getGastos() {
        User usuario = userService.findByUsername(securityContext.getUserPrincipal().getName());
        return Response.ok(gastoService.getGastosByUser(usuario)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteGasto(@PathParam("id") Long id) {
        User usuario = userService.findByUsername(securityContext.getUserPrincipal().getName());
        try {
            gastoService.deleteGasto(id, usuario);
            return Response.noContent().build();
        } catch (RuntimeException ex) {
            if (ex.getMessage().contains("no encontrado")) {
                throw new GastoNoEncontradoException("El gasto con id " + id + " no existe");
            } else if (ex.getMessage().contains("sin permisos")) {
                throw new SinPermisoEliminarGastoException("No tienes permisos para eliminar este gasto");
            }
            throw ex;
        }
    }

    @GET
    @Path("/filtrar")
    public Response filtrarGastos(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("tipo") String tipo) {

        User usuario = userService.findByUsername(securityContext.getUserPrincipal().getName());

        LocalDate fechaInicio = (fechaInicioStr != null && !fechaInicioStr.isEmpty())
                ? LocalDate.parse(fechaInicioStr) : null;

        LocalDate fechaFin = (fechaFinStr != null && !fechaFinStr.isEmpty())
                ? LocalDate.parse(fechaFinStr) : null;

        // Normaliza tipo vacío
        if (tipo != null && tipo.isBlank()) {
            tipo = null;
        }

        return Response.ok(gastoService.getGastosByFilters(usuario, fechaInicio, fechaFin, tipo)).build();
    }


}


