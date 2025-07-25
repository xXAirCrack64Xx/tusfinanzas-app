package com.davivienda.tusfinanzas.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.davivienda.tusfinanzas.dto.AuthRequest;
import com.davivienda.tusfinanzas.dto.AuthResponse;
import com.davivienda.tusfinanzas.dto.RegisterRequest;
import com.davivienda.tusfinanzas.service.AuthService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(AuthRequest request) {
        String token = authService.authenticate(request.getUsername(), request.getPassword());
        return Response.ok(new AuthResponse(token)).build();
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        authService.register(request);
        return Response.ok("Usuario registrado").build();
    }
}
