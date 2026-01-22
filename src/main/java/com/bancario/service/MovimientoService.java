package com.bancario.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.bancario.dto.MovimientoDTO;

public interface MovimientoService {

	MovimientoDTO realizarMovimiento(MovimientoDTO dto);

    List<MovimientoDTO> getMovimientosPorCuenta(String numeroCuenta);

    List<MovimientoDTO> getMovimientosPorCliente(Long clienteId);

    List<MovimientoDTO> getMovimientosPorClienteYFecha(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    Optional<MovimientoDTO> getMovimientoById(Long id);

    void eliminarMovimiento(Long id);

    Object[] getTotalesCreditosDebitos(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}
