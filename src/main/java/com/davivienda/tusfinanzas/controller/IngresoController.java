package com.davivienda.tusfinanzas.controller;

import com.davivienda.tusfinanzas.dto.IngresoDTO;
import com.davivienda.tusfinanzas.entity.Ingreso;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.exception.IngresoNotFoundException;
import com.davivienda.tusfinanzas.exception.UnauthorizedAccessException;
import com.davivienda.tusfinanzas.service.IngresoService;
import com.davivienda.tusfinanzas.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.util.List;

@Path("/ingresos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("USER")
public class IngresoController {

    @Inject
    IngresoService ingresoService;

    @Inject
    UserService userService;

    @Context
    SecurityContext securityContext;

    @POST
    public Response createIngreso(IngresoDTO dto) {
        String username = securityContext.getUserPrincipal().getName();
        User usuario = userService.findByUsername(username);

        if (usuario == null) {
            throw new UnauthorizedAccessException("Usuario no autorizado");
        }

        Ingreso ingreso = ingresoService.createIngreso(dto, usuario);
        return Response.status(Response.Status.CREATED).entity(ingreso).build();
    }

    @GET
    public Response getIngresos() {
        String username = securityContext.getUserPrincipal().getName();
        User usuario = userService.findByUsername(username);

        if (usuario == null) {
            throw new UnauthorizedAccessException("Usuario no autorizado");
        }

        List<Ingreso> ingresos = ingresoService.getIngresosByUser(usuario);
        return Response.ok(ingresos).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteIngreso(@PathParam("id") Long id) {
        String username = securityContext.getUserPrincipal().getName();
        User usuario = userService.findByUsername(username);

        if (usuario == null) {
            throw new UnauthorizedAccessException("Usuario no autorizado");
        }

        ingresoService.deleteIngreso(id, usuario);
        return Response.noContent().build();
    }

    @GET
    @Path("/filtrar")
    public Response filtrarIngresos(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("tipo") String tipo) {

        String username = securityContext.getUserPrincipal().getName();
        User usuario = userService.findByUsername(username);

        if (usuario == null) {
            throw new UnauthorizedAccessException("Usuario no autorizado");
        }

        LocalDate fechaInicio = (fechaInicioStr != null && !fechaInicioStr.isEmpty())
                ? LocalDate.parse(fechaInicioStr) : null;

        LocalDate fechaFin = (fechaFinStr != null && !fechaFinStr.isEmpty())
                ? LocalDate.parse(fechaFinStr) : null;

        List<Ingreso> ingresos = ingresoService.getIngresosByFilters(usuario, fechaInicio, fechaFin, tipo);

        if (ingresos == null || ingresos.isEmpty()) {
            throw new IngresoNotFoundException("No se encontraron ingresos con los filtros aplicados");
        }

        return Response.ok(ingresos).build();
    }
}

