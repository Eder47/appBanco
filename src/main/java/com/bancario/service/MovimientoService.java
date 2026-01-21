package com.bancario.service;

import com.bancario.dto.MovimientoDTO;
import com.bancario.model.Movimiento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoService {

    Movimiento realizarMovimiento(MovimientoDTO dto);

    List<Movimiento> getMovimientosPorCuenta(String numeroCuenta);

    List<MovimientoDTO> getMovimientosPorCliente(Long clienteId);

    List<Movimiento> getMovimientosPorClienteYFecha(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    Optional<Movimiento> getMovimientoById(Long id);

    void eliminarMovimiento(Long id);

    Object[] getTotalesCreditosDebitos(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}
