package com.davivienda.tusfinanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GastoDTO {
    private Double monto;
    private String descripcion;
    private LocalDate fecha;
    private String tipo;
}
