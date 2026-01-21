package com.bancario.repository;

import com.bancario.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findByClienteClienteId(Long clienteId);
    List<Cuenta> findByEstado(Boolean estado);
    List<Cuenta> findByTipoCuenta(String tipoCuenta);
    
    @Query("SELECT c FROM Cuenta c WHERE c.cliente.clienteId = :clienteId AND c.estado = true")
    List<Cuenta> findCuentasActivasPorCliente(@Param("clienteId") Long clienteId);
    
    @Query("SELECT COALESCE(SUM(c.saldoDisponible), 0) FROM Cuenta c WHERE c.cliente.clienteId = :clienteId")
    Double getSaldoTotalCliente(@Param("clienteId") Long clienteId);
}