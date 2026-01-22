package com.bancario.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancario.controller.TipoMovimiento;
import com.bancario.dto.MovimientoDTO;
import com.bancario.exception.CupoDiarioExcedidoException;
import com.bancario.exception.SaldoNoDisponibleException;
import com.bancario.model.Cuenta;
import com.bancario.model.Movimiento;
import com.bancario.repository.ClienteRepository;
import com.bancario.repository.CuentaRepository;
import com.bancario.repository.MovimientoRepository;
import com.bancario.service.MovimientoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

	private final MovimientoRepository movimientoRepository;
	private final CuentaRepository cuentaRepository;
	private final ClienteRepository clienteRepository;

	private final ModelMapper mapper;

	private static final double LIMITE_DIARIO_RETIRO = 1000.0;

	private final BiFunction<Double, Double, Double> calcularRetiro = (saldoActual, valor) -> saldoActual - valor;

	private final BiFunction<Double, Double, Double> calcularDeposito = (saldoActual, valor) -> saldoActual + valor;

	@Transactional
	public MovimientoDTO realizarMovimiento(MovimientoDTO dto) {

		Cuenta cuenta = cuentaRepository.findByNumeroCuenta(dto.getNumeroCuenta())
				.orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

		if (!cuenta.getEstado()) {
			throw new RuntimeException("Cuenta inactiva");
		}

		Movimiento movimiento = mapper.map(dto, Movimiento.class);
		movimiento.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
		movimiento.setCuenta(cuenta);
		movimiento.setCliente(cuenta.getCliente());
		movimiento.setSaldo(cuenta.getSaldoDisponible());

		Double nuevoSaldo;

		if (dto.getTipoMovimiento() == TipoMovimiento.RETIRO) {
			validarRetiro(cuenta, dto.getValor());
			nuevoSaldo = calcularRetiro.apply(cuenta.getSaldoDisponible(), dto.getValor());
		} else if (dto.getTipoMovimiento() == TipoMovimiento.DEPOSITO) {
			nuevoSaldo = calcularDeposito.apply(cuenta.getSaldoDisponible(), dto.getValor());
		} else {
			throw new RuntimeException("Tipo de movimiento no válido");
		}

		cuenta.setSaldoDisponible(nuevoSaldo);
		movimiento.setSaldoDisponible(nuevoSaldo);

		cuentaRepository.save(cuenta);
		Movimiento movimientoGuardado = movimientoRepository.save(movimiento);

		// Convertimos a DTO
		MovimientoDTO resultado = mapper.map(movimientoGuardado, MovimientoDTO.class);
		resultado.setNumeroCuenta(cuenta.getNumeroCuenta());
		resultado.setClienteId(cuenta.getCliente().getClienteId());

		return resultado;
	}

	private void validarRetiro(Cuenta cuenta, Double valor) {

		if (cuenta.getSaldoDisponible() < valor) {
			throw new SaldoNoDisponibleException("Saldo no disponible");
		}

		Double totalRetiradoHoy = movimientoRepository.getTotalRetirosDiarios(cuenta.getNumeroCuenta(),
				LocalDate.now());

		if (totalRetiradoHoy + valor > LIMITE_DIARIO_RETIRO) {
			throw new CupoDiarioExcedidoException("Cupo diario Excedido");
		}
	}

	@Transactional(readOnly = true)
	public List<MovimientoDTO> getMovimientosPorCuenta(String numeroCuenta) {
		return movimientoRepository.findByCuentaNumeroCuenta(numeroCuenta).stream().map(m -> {
			MovimientoDTO dto = mapper.map(m, MovimientoDTO.class);
			dto.setNumeroCuenta(m.getCuenta().getNumeroCuenta());
			dto.setClienteId(m.getCliente().getClienteId());
			return dto;
		}).toList();
	}

	@Transactional(readOnly = true)
	public List<MovimientoDTO> getMovimientosPorCliente(Long clienteId) {
		return movimientoRepository.findByClienteClienteId(clienteId).stream().map(m -> {
			MovimientoDTO dto = mapper.map(m, MovimientoDTO.class);
			dto.setNumeroCuenta(m.getCuenta().getNumeroCuenta());
			dto.setClienteId(m.getCliente().getClienteId());
			return dto;
		}).toList();
	}

	@Transactional(readOnly = true)
	public List<MovimientoDTO> getMovimientosPorClienteYFecha(Long clienteId, LocalDate fechaInicio,
			LocalDate fechaFin) {
		return movimientoRepository.findByClienteAndFechaBetween(clienteId, fechaInicio, fechaFin).stream().map(m -> {
			MovimientoDTO dto = mapper.map(m, MovimientoDTO.class);
			dto.setNumeroCuenta(m.getCuenta().getNumeroCuenta());
			dto.setClienteId(m.getCliente().getClienteId());
			return dto;
		}).toList();
	}

	@Transactional(readOnly = true)
	public Optional<MovimientoDTO> getMovimientoById(Long id) {
		return movimientoRepository.findById(id).map(m -> {
			MovimientoDTO dto = mapper.map(m, MovimientoDTO.class);
			dto.setNumeroCuenta(m.getCuenta().getNumeroCuenta());
			dto.setClienteId(m.getCliente().getClienteId());
			return dto;
		});
	}

	@Transactional
	public void eliminarMovimiento(Long id) {
		movimientoRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Object[] getTotalesCreditosDebitos(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {

		return movimientoRepository.getTotalesCreditosDebitos(clienteId, fechaInicio, fechaFin);
	}
}
