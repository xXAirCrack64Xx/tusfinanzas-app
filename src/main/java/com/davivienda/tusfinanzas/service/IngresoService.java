package com.davivienda.tusfinanzas.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import com.davivienda.tusfinanzas.dto.IngresoDTO;
import com.davivienda.tusfinanzas.entity.Ingreso;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.exception.IngresoNotFoundException;
import com.davivienda.tusfinanzas.exception.UnauthorizedIngresoAccessException;
import com.davivienda.tusfinanzas.repository.IngresoRepository;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@Transactional
public class IngresoService implements IIngresoService {

    @Inject
    IngresoRepository ingresoRepository;

    @Override
    public Ingreso createIngreso(IngresoDTO dto, User usuario) {
        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(dto.getMonto());
        ingreso.setDescripcion(dto.getDescripcion());
        ingreso.setFecha(dto.getFecha());
        ingreso.setUsuario(usuario);
        ingreso.setTipo(dto.getTipo());
        ingresoRepository.persist(ingreso);
        return ingreso;
    }

    @Override
    public List<Ingreso> getIngresosByUser(User usuario) {
        return ingresoRepository.findByUserId(usuario.getId());
    }

    @Override
    public void deleteIngreso(Long id, User usuario) {
        Ingreso ingreso = ingresoRepository.findById(id);
        if (ingreso == null) {
            throw new IngresoNotFoundException("Ingreso con id " + id + " no encontrado");
        }
        if (!ingreso.getUsuario().getId().equals(usuario.getId())) {
            throw new UnauthorizedIngresoAccessException("No tienes permisos para eliminar este ingreso");
        }
        ingresoRepository.delete(ingreso);
    }

    @Override
    public List<Ingreso> getIngresosByFilters(User usuario, LocalDate fechaInicio, LocalDate fechaFin, String tipo) {
        return ingresoRepository.findByFilters(usuario.getId(), fechaInicio, fechaFin, tipo);
    }
}
