package com.bancario.service;

import com.bancario.dto.CuentaDTO;
import com.bancario.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaService {

    Cuenta crearCuenta(CuentaDTO dto);

    List<CuentaDTO> getAllCuentas();

    Optional<CuentaDTO> getCuentaByNumero(String numeroCuenta);

    List<CuentaDTO> getCuentasByClienteId(Long clienteId);

    List<CuentaDTO> getCuentasActivasByClienteId(Long clienteId);

    CuentaDTO actualizarCuenta(String numeroCuenta, CuentaDTO dto);

    void eliminarCuenta(String numeroCuenta);

    void actualizarSaldo(String numeroCuenta, Double nuevoSaldo);

    Double getSaldoTotalCliente(Long clienteId);
}
