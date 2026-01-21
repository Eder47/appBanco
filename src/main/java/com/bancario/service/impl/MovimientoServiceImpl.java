package com.bancario.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class MovimientoServiceImpl implements MovimientoService{

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    private final ModelMapper mapper;

    private static final double LIMITE_DIARIO_RETIRO = 1000.0;

    private final BiFunction<Double, Double, Double> calcularRetiro =
            (saldoActual, valor) -> saldoActual - valor;

    private final BiFunction<Double, Double, Double> calcularDeposito =
            (saldoActual, valor) -> saldoActual + valor;

            @Transactional
            public Movimiento realizarMovimiento(MovimientoDTO dto) {

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

                if ("RETIRO".equalsIgnoreCase(dto.getTipoMovimiento())) {

                    validarRetiro(cuenta, dto.getValor());
                    nuevoSaldo = calcularRetiro.apply(cuenta.getSaldoDisponible(), dto.getValor());

                } else if ("DEPOSITO".equalsIgnoreCase(dto.getTipoMovimiento())) {

                    nuevoSaldo = calcularDeposito.apply(cuenta.getSaldoDisponible(), dto.getValor());

                } else {
                    throw new RuntimeException("Tipo de movimiento no válido");
                }

                cuenta.setSaldoDisponible(nuevoSaldo);
                movimiento.setSaldoDisponible(nuevoSaldo);

                cuentaRepository.save(cuenta);
                return movimientoRepository.save(movimiento);
            }


    private void validarRetiro(Cuenta cuenta, Double valor) {

        if (cuenta.getSaldoDisponible() < valor) {
            throw new SaldoNoDisponibleException("Saldo no disponible");
        }

        Double totalRetiradoHoy = movimientoRepository.getTotalRetirosDiarios(
                cuenta.getNumeroCuenta(),
                LocalDate.now()
        );

        if (totalRetiradoHoy + valor > LIMITE_DIARIO_RETIRO) {
            throw new CupoDiarioExcedidoException("Cupo diario Excedido");
        }
    }

    @Transactional(readOnly = true)
    public List<Movimiento> getMovimientosPorCuenta(String numeroCuenta) {
        return movimientoRepository.findByCuentaNumeroCuenta(numeroCuenta);
    }

    @Transactional(readOnly = true)
    public List<MovimientoDTO> getMovimientosPorCliente(Long clienteId) {
        return movimientoRepository.findByClienteClienteId(clienteId)
                .stream()
                .map(m -> mapper.map(m, MovimientoDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Movimiento> getMovimientosPorClienteYFecha(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        return movimientoRepository.findByClienteAndFechaBetween(
                clienteId, fechaInicio, fechaFin
        );
    }

    @Transactional(readOnly = true)
    public Optional<Movimiento> getMovimientoById(Long id) {
        return movimientoRepository.findById(id);
    }

    @Transactional
    public void eliminarMovimiento(Long id) {
        movimientoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Object[] getTotalesCreditosDebitos(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        return movimientoRepository.getTotalesCreditosDebitos(
                clienteId, fechaInicio, fechaFin
        );
    }
}
