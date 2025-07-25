package com.davivienda.tusfinanzas.service;

import com.davivienda.tusfinanzas.dto.IngresoDTO;
import com.davivienda.tusfinanzas.entity.Ingreso;
import com.davivienda.tusfinanzas.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface IIngresoService {
    Ingreso createIngreso(IngresoDTO dto, User usuario);
    List<Ingreso> getIngresosByUser(User usuario);
    void deleteIngreso(Long id, User usuario);
    List<Ingreso> getIngresosByFilters(User usuario, LocalDate fechaInicio, LocalDate fechaFin, String tipo);
}
