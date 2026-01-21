package com.bancario.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancario.dto.CuentaDTO;
import com.bancario.model.Cliente;
import com.bancario.model.Cuenta;
import com.bancario.repository.ClienteRepository;
import com.bancario.repository.CuentaRepository;
import com.bancario.service.CuentaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService{

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final ModelMapper mapper;

    private interface SaldoStrategy {
        Double calcularSaldo(Double saldoInicial);
    }

    private final SaldoStrategy ahorroStrategy = saldo -> saldo;
    private final SaldoStrategy corrienteStrategy = saldo -> saldo - 5.0;

    @Transactional
    public Cuenta crearCuenta(CuentaDTO dto) {

        if (cuentaRepository.existsById(dto.getNumeroCuenta())) {
            throw new RuntimeException("Número de cuenta ya existe");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Cuenta cuenta = mapper.map(dto, Cuenta.class);

        SaldoStrategy strategy =
                "AHORRO".equals(dto.getTipoCuenta()) ? ahorroStrategy : corrienteStrategy;

        Double saldoFinal = strategy.calcularSaldo(dto.getSaldoInicial());

        cuenta.setSaldoInicial(saldoFinal);
        cuenta.setSaldoDisponible(saldoFinal);
        cuenta.setCliente(cliente);

        return cuentaRepository.save(cuenta);
    }

    @Transactional(readOnly = true)
    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cuenta> getCuentaByNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    @Transactional(readOnly = true)
    public List<Cuenta> getCuentasByClienteId(Long clienteId) {
        return cuentaRepository.findByClienteClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Cuenta> getCuentasActivasByClienteId(Long clienteId) {
        return cuentaRepository.findCuentasActivasPorCliente(clienteId);
    }

    @Transactional
    public Cuenta actualizarCuenta(String numeroCuenta, CuentaDTO dto) {

        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(cuenta -> {

                    Consumer<Cuenta> actualizador = c -> {
                        if (dto.getTipoCuenta() != null) {
                            c.setTipoCuenta(dto.getTipoCuenta());
                        }
                        if (dto.getEstado() != null) {
                            c.setEstado(dto.getEstado());
                        }
                    };

                    actualizador.accept(cuenta);
                    return cuentaRepository.save(cuenta);
                })
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    @Transactional
    public void eliminarCuenta(String numeroCuenta) {
        cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .ifPresent(cuenta -> {
                    cuenta.setEstado(false);
                    cuentaRepository.save(cuenta);
                });
    }

    @Transactional
    public void actualizarSaldo(String numeroCuenta, Double nuevoSaldo) {
        cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .ifPresent(cuenta -> {
                    cuenta.setSaldoDisponible(nuevoSaldo);
                    cuentaRepository.save(cuenta);
                });
    }

    @Transactional(readOnly = true)
    public Double getSaldoTotalCliente(Long clienteId) {
        return cuentaRepository.getSaldoTotalCliente(clienteId);
    }
}
