package com.bancario.repository;

import com.bancario.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaNumeroCuenta(String numeroCuenta);
    
    List<Movimiento> findByClienteClienteId(Long clienteId);
    
    @Query("SELECT m FROM Movimiento m WHERE m.cliente.clienteId = :clienteId " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY m.fecha DESC")
    List<Movimiento> findByClienteAndFechaBetween(
            @Param("clienteId") Long clienteId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimiento m " +
           "WHERE m.cuenta.numeroCuenta = :numeroCuenta " +
           "AND m.tipoMovimiento = 'RETIRO' " +
           "AND m.fecha = :fecha")
    Double getTotalRetirosDiarios(
            @Param("numeroCuenta") String numeroCuenta,
            @Param("fecha") LocalDate fecha);
    
    @Query("SELECT COALESCE(SUM(CASE WHEN m.tipoMovimiento = 'DEPOSITO' THEN m.valor ELSE 0 END), 0) as creditos, " +
           "COALESCE(SUM(CASE WHEN m.tipoMovimiento = 'RETIRO' THEN m.valor ELSE 0 END), 0) as debitos " +
           "FROM Movimiento m WHERE m.cliente.clienteId = :clienteId " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin")
    Object[] getTotalesCreditosDebitos(
            @Param("clienteId") Long clienteId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}