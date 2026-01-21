package com.bancario.service;

import com.bancario.dto.CuentaDTO;
import com.bancario.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaService {

    Cuenta crearCuenta(CuentaDTO dto);

    List<Cuenta> getAllCuentas();

    Optional<Cuenta> getCuentaByNumero(String numeroCuenta);

    List<Cuenta> getCuentasByClienteId(Long clienteId);

    List<Cuenta> getCuentasActivasByClienteId(Long clienteId);

    Cuenta actualizarCuenta(String numeroCuenta, CuentaDTO dto);

    void eliminarCuenta(String numeroCuenta);

    void actualizarSaldo(String numeroCuenta, Double nuevoSaldo);

    Double getSaldoTotalCliente(Long clienteId);
}
