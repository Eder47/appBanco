package com.bancario.service;

import java.util.List;

import com.bancario.dto.ReporteDTO;

public interface ReportePdfService {
	
	public byte[] generarPdf(List<ReporteDTO> datos);

}
