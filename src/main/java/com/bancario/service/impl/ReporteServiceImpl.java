package com.bancario.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bancario.dto.ReporteDTO;
import com.bancario.dto.ReporteRequestDTO;
import com.bancario.model.Movimiento;
import com.bancario.service.ReporteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final MovimientoServiceImpl movimientoService;
    private final ModelMapper mapper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<ReporteDTO> generarReporteJSON(ReporteRequestDTO request) {

        List<Movimiento> movimientos =
                movimientoService.getMovimientosPorClienteYFecha(
                        request.getClienteId(),
                        request.getFechaInicio(),
                        request.getFechaFin()
                );
        return movimientos.stream()
                .map(this::mapearMovimiento)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReporteDTO> generarReportePDF(ReporteRequestDTO request) {
        return generarReporteJSON(request);
    }

    private ReporteDTO mapearMovimiento(Movimiento movimiento) {

        ReporteDTO dto = mapper.map(movimiento, ReporteDTO.class);
        dto.setCliente(movimiento.getCliente().getNombre());
        dto.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        dto.setTipo(movimiento.getCuenta().getTipoCuenta());
        dto.setEstado(movimiento.getCuenta().getEstado());
        dto.setSaldoInicial(movimiento.getSaldo());
        dto.setMovimiento(
                "RETIRO".equals(movimiento.getTipoMovimiento())
                        ? -movimiento.getValor()
                        : movimiento.getValor()
        );

        dto.setSaldoDisponible(movimiento.getSaldoDisponible());

        return dto;
    }
}
