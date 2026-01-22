package com.bancario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bancario.dto.ReporteDTO;
import com.bancario.dto.ReporteRequestDTO;
import com.bancario.service.ReportePdfService;
import com.bancario.service.ReporteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(
	    origins = "http://localhost:4200",
	    allowedHeaders = "*",
	    methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
	)
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {
    
	@Autowired
    private ReporteService reporteService;
	
	@Autowired
	private ReportePdfService reportePdfService;

    
    @GetMapping
    public ResponseEntity<List<ReporteDTO>> generarReporteJSON(@Valid ReporteRequestDTO request) {
        List<ReporteDTO> reporte = reporteService.generarReportePDF(request);
        return ResponseEntity.ok(reporte);
    }
    
    @PostMapping("/pdf")
    public ResponseEntity<byte[]> generarReportePDF(@Valid @RequestBody ReporteRequestDTO request) {

        List<ReporteDTO> lista = reporteService.generarReportePDF(request);
        byte[] pdf = reportePdfService.generarPdf(lista);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reporte.pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }




}