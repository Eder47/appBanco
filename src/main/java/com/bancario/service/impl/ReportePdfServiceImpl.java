package com.bancario.service.impl;

import com.bancario.dto.ReporteDTO;
import com.bancario.service.ReportePdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportePdfServiceImpl implements ReportePdfService{

	public byte[] generarPdf(List<ReporteDTO> datos) {

		try {
			Document document = new Document(PageSize.A4);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, out);

			document.open();

			Font titulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
			Paragraph p = new Paragraph("Reporte de Movimientos", titulo);
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			document.add(new Paragraph(" "));

			PdfPTable table = new PdfPTable(8);
			table.setWidthPercentage(100);

			agregarHeader(table, "Fecha");
			agregarHeader(table, "Cliente");
			agregarHeader(table, "Cuenta");
			agregarHeader(table, "Tipo");
			agregarHeader(table, "Saldo Inicial");
			agregarHeader(table, "Movimiento");
			agregarHeader(table, "Saldo Disponible");
			agregarHeader(table, "Estado");

			for (ReporteDTO r : datos) {
				table.addCell(
						r.getFecha() != null ? r.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
				table.addCell(r.getCliente() != null ? r.getCliente() : "");
				table.addCell(r.getNumeroCuenta() != null ? r.getNumeroCuenta() : "");
				table.addCell(r.getTipo() != null ? r.getTipo() : "");
				table.addCell(r.getSaldoInicial() != null ? String.valueOf(r.getSaldoInicial()) : "0.0");
				table.addCell(r.getMovimiento() != null ? String.valueOf(r.getMovimiento()) : "0.0");
				table.addCell(r.getSaldoDisponible() != null ? String.valueOf(r.getSaldoDisponible()) : "0.0");
				table.addCell(r.getEstado() != null && r.getEstado() ? "ACTIVO" : "INACTIVO");
			}

			document.add(table);
			document.close();

			return out.toByteArray();

		} catch (Exception e) {
			throw new RuntimeException("Error generando PDF", e);
		}
	}

    private void agregarHeader(PdfPTable table, String titulo) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(titulo));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
}
