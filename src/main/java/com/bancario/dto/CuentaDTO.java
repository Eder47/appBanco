package com.bancario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CuentaDTO {
    @NotBlank(message = "Número de cuenta es requerido")
    @Pattern(regexp = "^\\d{6,20}$", message = "Número de cuenta inválido")
    private String numeroCuenta;
    
    @NotBlank(message = "Tipo de cuenta es requerido")
    @Pattern(regexp = "^(AHORRO|CORRIENTE)$", message = "Tipo debe ser AHORRO o CORRIENTE")
    private String tipoCuenta;
    
    @NotNull(message = "Saldo inicial es requerido")
    @DecimalMin(value = "0.0", message = "Saldo no puede ser negativo")
    private Double saldoInicial;
    
    private Boolean estado = true;
    
    @NotNull(message = "Cliente ID es requerido")
    private Long clienteId;
}