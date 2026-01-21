package com.bancario.service;

import com.bancario.dto.ReporteDTO;
import com.bancario.dto.ReporteRequestDTO;

import java.util.List;

public interface ReporteService {

    List<ReporteDTO> generarReporteJSON(ReporteRequestDTO request);

    List<ReporteDTO> generarReportePDF(ReporteRequestDTO request);
}
