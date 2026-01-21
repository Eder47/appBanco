package com.bancario.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MovimientoDTO {
    @NotBlank(message = "Número de cuenta es requerido")
    private String numeroCuenta;
    
    @NotNull(message = "Tipo movimiento es requerido")
    @Pattern(regexp = "^(DEPOSITO|RETIRO)$", message = "Tipo debe ser DEPOSITO o RETIRO")
    private String tipoMovimiento;
    
    @NotNull(message = "Valor es requerido")
    @DecimalMin(value = "0.01", message = "Valor mínimo 0.01")
    private Double valor;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha = LocalDate.now();
}