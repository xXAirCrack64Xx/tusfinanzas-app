package com.davivienda.tusfinanzas.repository;

import jakarta.enterprise.context.ApplicationScoped;
import com.davivienda.tusfinanzas.entity.Gasto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GastoRepository implements PanacheRepository<Gasto>, IGastoRepository {

    @Override
    public List<Gasto> findByUserId(Long userId) {
        return list("usuario.id", userId);
    }


    @Override
    public List<Gasto> findByFilters(Long userId, LocalDate fechaInicio, LocalDate fechaFin, String tipo) {
        StringBuilder query = new StringBuilder("usuario.id = ?1");
        List<Object> params = new ArrayList<>();
        params.add(userId);

        int index = 2;
        if (fechaInicio != null) {
            query.append(" and fecha >= ?" + index++);
            params.add(fechaInicio);
        }
        if (fechaFin != null) {
            query.append(" and fecha <= ?" + index++);
            params.add(fechaFin);
        }
        if (tipo != null && !tipo.isBlank()) {
            query.append(" and tipo = ?" + index++);
            params.add(tipo);
        }

        return find(query.toString(), params.toArray()).list();
    }


}

