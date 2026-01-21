package com.bancario.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cuentas")
@Data
public class Cuenta {
    
    @Id
    @Column(name = "numero_cuenta", unique = true, nullable = false)
    private String numeroCuenta;
    
    @Column(name = "tipo_cuenta", nullable = false)
    private String tipoCuenta; 
    
    @Column(name = "saldo_inicial", nullable = false)
    private Double saldoInicial = 0.0;
    
    @Column(name = "saldo_disponible", nullable = false)
    private Double saldoDisponible = 0.0;
    
    @Column(name = "estado")
    private Boolean estado = true;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}