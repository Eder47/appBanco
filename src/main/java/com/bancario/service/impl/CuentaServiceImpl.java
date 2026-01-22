package com.bancario.service.impl;

import java.util.List;
import java.util.Optional;

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
    public List<CuentaDTO> getAllCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        return cuentas.stream()
                      .map(cuenta -> {
                          CuentaDTO dto = mapper.map(cuenta, CuentaDTO.class);
                          if (cuenta.getCliente() != null) {
                              dto.setClienteId(cuenta.getCliente().getClienteId());
                          }
                          return dto;
                      })
                      .toList();
    }



    @Transactional(readOnly = true)
    public Optional<CuentaDTO> getCuentaByNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(cuenta -> {
                    CuentaDTO dto = mapper.map(cuenta, CuentaDTO.class);
                    if (cuenta.getCliente() != null) {
                        dto.setClienteId(cuenta.getCliente().getClienteId());
                        dto.setClienteNombre(cuenta.getCliente().getNombre());
                    }
                    return dto;
                });
    }


    @Transactional(readOnly = true)
    public List<CuentaDTO> getCuentasByClienteId(Long clienteId) {
        List<Cuenta> cuentas = cuentaRepository.findByClienteClienteId(clienteId);
        return cuentas.stream()
                      .map(cuenta -> {
                          CuentaDTO dto = mapper.map(cuenta, CuentaDTO.class);
                          if (cuenta.getCliente() != null) {
                              dto.setClienteId(cuenta.getCliente().getClienteId());
                              dto.setClienteNombre(cuenta.getCliente().getNombre());
                          }
                          return dto;
                      })
                      .toList();
    }


    @Transactional(readOnly = true)
    public List<CuentaDTO> getCuentasActivasByClienteId(Long clienteId) {
        List<Cuenta> cuentas = cuentaRepository.findCuentasActivasPorCliente(clienteId);
        return cuentas.stream()
                      .map(cuenta -> {
                          CuentaDTO dto = mapper.map(cuenta, CuentaDTO.class);
                          if (cuenta.getCliente() != null) {
                              dto.setClienteId(cuenta.getCliente().getClienteId());
                              dto.setClienteNombre(cuenta.getCliente().getNombre());
                          }
                          return dto;
                      })
                      .toList();
    }


    @Transactional
    public CuentaDTO actualizarCuenta(String numeroCuenta, CuentaDTO dto) {
        Cuenta cuentaActualizada = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(cuenta -> {
                    if (dto.getTipoCuenta() != null) cuenta.setTipoCuenta(dto.getTipoCuenta());
                    if (dto.getEstado() != null) cuenta.setEstado(dto.getEstado());
                    return cuentaRepository.save(cuenta);
                })
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        CuentaDTO dtoActualizado = mapper.map(cuentaActualizada, CuentaDTO.class);
        if (cuentaActualizada.getCliente() != null) {
            dtoActualizado.setClienteId(cuentaActualizada.getCliente().getClienteId());
            dtoActualizado.setClienteNombre(cuentaActualizada.getCliente().getNombre());
        }
        return dtoActualizado;
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
