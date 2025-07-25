package com.davivienda.tusfinanzas.repository;

import jakarta.enterprise.context.ApplicationScoped;
import com.davivienda.tusfinanzas.entity.Ingreso;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class IngresoRepository implements PanacheRepository<Ingreso>, IIngresoRepository {

    @Override
    public List<Ingreso> findByUserId(Long userId) {
        return list("usuario.id", userId);
    }

    @Override
    public List<Ingreso> findByFilters(Long userId, LocalDate fechaInicio, LocalDate fechaFin, String tipo) {
        String query = "usuario.id = ?1";
        if (fechaInicio != null) {
            query += " and fecha >= ?2";
        }
        if (fechaFin != null) {
            query += " and fecha <= ?3";
        }
        if (tipo != null && !tipo.isBlank()) {
            query += " and tipo = ?4";
        }
        return find(query, userId, fechaInicio, fechaFin, tipo).list();
    }
}


