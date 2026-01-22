package com.bancario.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bancario.controller.TipoMovimiento;
import com.bancario.dto.MovimientoDTO;
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

        List<MovimientoDTO> movimientos =
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

    private ReporteDTO mapearMovimiento(MovimientoDTO dto) {
        ReporteDTO reporte = mapper.map(dto, ReporteDTO.class);
        reporte.setNumeroCuenta(dto.getNumeroCuenta());
        reporte.setTipo(dto.getTipoMovimiento().name());
        reporte.setMovimiento(
                dto.getTipoMovimiento() == TipoMovimiento.RETIRO
                        ? -dto.getValor()
                        : dto.getValor()
        );
        reporte.setSaldoInicial(dto.getSaldo());
        reporte.setSaldoDisponible(dto.getSaldoDisponible());

        return reporte;
    }

}
