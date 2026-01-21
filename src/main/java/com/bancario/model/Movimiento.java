package com.bancario.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "movimientos")
@Data
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Long movimientoId;
    
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    
    @Column(name = "tipo_movimiento", nullable = false)
    private String tipoMovimiento;
    
    @Column(name = "valor", nullable = false)
    private Double valor;
    
    @Column(name = "saldo", nullable = false)
    private Double saldo;
    
    @Column(name = "saldo_disponible", nullable = false)
    private Double saldoDisponible;
    
    @ManyToOne
    @JoinColumn(name = "numero_cuenta", nullable = false)
    private Cuenta cuenta;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}