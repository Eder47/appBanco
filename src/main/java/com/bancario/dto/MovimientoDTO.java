package com.bancario.dto;

import java.time.LocalDate;

import com.bancario.controller.TipoMovimiento;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimientoDTO {

    private Long movimientoId;

    @NotBlank(message = "Número de cuenta es requerido")
    private String numeroCuenta;

    @NotNull(message = "Tipo movimiento es requerido")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "Valor es requerido")
    @DecimalMin(value = "0.01", message = "Valor mínimo 0.01")
    private Double valor;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    private Double saldo; 
    private Double saldoDisponible;

    private Long clienteId;
}
