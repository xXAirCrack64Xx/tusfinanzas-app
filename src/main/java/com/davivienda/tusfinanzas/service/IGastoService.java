package com.davivienda.tusfinanzas.service;

import com.davivienda.tusfinanzas.dto.GastoDTO;
import com.davivienda.tusfinanzas.entity.Gasto;
import com.davivienda.tusfinanzas.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface IGastoService {
    Gasto createGasto(GastoDTO dto, User usuario);
    List<Gasto> getGastosByUser(User usuario);
    void deleteGasto(Long id, User usuario);
    List<Gasto> getGastosByFilters(User usuario, LocalDate fechaInicio, LocalDate fechaFin, String tipo);
}

