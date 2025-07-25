package com.davivienda.tusfinanzas.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import com.davivienda.tusfinanzas.dto.GastoDTO;
import com.davivienda.tusfinanzas.entity.Gasto;
import com.davivienda.tusfinanzas.entity.User;
import com.davivienda.tusfinanzas.repository.GastoRepository;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@Transactional
public class GastoService implements IGastoService {

    @Inject
    GastoRepository gastoRepository;

    @Override
    public Gasto createGasto(GastoDTO dto, User usuario) {
        Gasto gasto = new Gasto();
        gasto.setMonto(dto.getMonto());
        gasto.setDescripcion(dto.getDescripcion());
        gasto.setFecha(dto.getFecha());
        gasto.setUsuario(usuario);
        gasto.setTipo(dto.getTipo());
        gastoRepository.persist(gasto);
        return gasto;
    }

    @Override
    public List<Gasto> getGastosByUser(User usuario) {
        return gastoRepository.findByUserId(usuario.getId());
    }

    @Override
    public void deleteGasto(Long id, User usuario) {
        Gasto gasto = gastoRepository.findById(id);
        if (gasto == null || !gasto.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Gasto no encontrado o sin permisos");
        }
        gastoRepository.delete(gasto);
    }

    @Override
    public List<Gasto> getGastosByFilters(User usuario, LocalDate fechaInicio, LocalDate fechaFin, String tipo) {
        // Si no se envió ningún filtro, devolver todos los gastos del usuario
        if (fechaInicio == null && fechaFin == null && (tipo == null || tipo.isBlank())) {
            return gastoRepository.findByUserId(usuario.getId());
        }

        // Si hay algún filtro, aplicar el filtrado normal
        return gastoRepository.findByFilters(usuario.getId(), fechaInicio, fechaFin, tipo);
    }
}

