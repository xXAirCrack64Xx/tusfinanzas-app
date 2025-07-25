package com.davivienda.tusfinanzas.repository;

import com.davivienda.tusfinanzas.entity.Gasto;

import java.time.LocalDate;
import java.util.List;

public interface IGastoRepository {
    List<Gasto> findByUserId(Long userId);
    List<Gasto> findByFilters(Long userId, LocalDate fechaInicio, LocalDate fechaFin, String tipo);
}
