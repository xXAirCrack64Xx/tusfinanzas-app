package com.davivienda.tusfinanzas.repository;

import com.davivienda.tusfinanzas.entity.Ingreso;

import java.time.LocalDate;
import java.util.List;

public interface IIngresoRepository {
    List<Ingreso> findByUserId(Long userId);
    List<Ingreso> findByFilters(Long userId, LocalDate fechaInicio, LocalDate fechaFin, String tipo);
}
