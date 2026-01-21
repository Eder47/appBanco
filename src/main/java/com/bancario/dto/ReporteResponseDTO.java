package com.bancario.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteResponseDTO {

    private String cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<ReporteDTO> movimientos;
    private Double totalCreditos;
    private Double totalDebitos;
}

