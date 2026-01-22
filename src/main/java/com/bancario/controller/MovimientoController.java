package com.bancario.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bancario.dto.MovimientoDTO;
import com.bancario.service.MovimientoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

	@Autowired
	private MovimientoService movimientoService;

	@PostMapping
	public ResponseEntity<MovimientoDTO> realizarMovimiento(@Valid @RequestBody MovimientoDTO movimientoDTO) {
		try {
			MovimientoDTO movimiento = movimientoService.realizarMovimiento(movimientoDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}

	@GetMapping("/cuenta/{numeroCuenta}")
	public ResponseEntity<List<MovimientoDTO>> getMovimientosPorCuenta(@PathVariable String numeroCuenta) {
		List<MovimientoDTO> movimientos = movimientoService.getMovimientosPorCuenta(numeroCuenta);
		return ResponseEntity.ok(movimientos);
	}

	@GetMapping("/cliente/{clienteId}")
	public ResponseEntity<List<MovimientoDTO>> getMovimientosPorCliente(@PathVariable Long clienteId) {
		List<MovimientoDTO> movimientos = movimientoService.getMovimientosPorCliente(clienteId);
		return ResponseEntity.ok(movimientos);
	}

	@GetMapping("/cliente/{clienteId}/fecha")
	public ResponseEntity<List<MovimientoDTO>> getMovimientosPorClienteYFecha(@PathVariable Long clienteId,
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaInicio,
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaFin) {

		List<MovimientoDTO> movimientos = movimientoService.getMovimientosPorClienteYFecha(clienteId, fechaInicio,
				fechaFin);
		return ResponseEntity.ok(movimientos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MovimientoDTO> getMovimientoById(@PathVariable Long id) {
		return movimientoService.getMovimientoById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
		movimientoService.eliminarMovimiento(id);
		return ResponseEntity.noContent().build();
	}
}