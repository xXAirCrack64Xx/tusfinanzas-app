package com.davivienda.tusfinanzas.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class IngresoDTO {
    private Double monto;
    private String descripcion;
    private LocalDate fecha;
    private String tipo;
}
