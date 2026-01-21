package com.bancario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bancario.dto.CuentaDTO;
import com.bancario.model.Cuenta;
import com.bancario.service.CuentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(
	    origins = "http://localhost:4200",
	    allowedHeaders = "*",
	    methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
	)
@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    
	@Autowired
    private CuentaService cuentaService;
    
    @PostMapping
    public ResponseEntity<Cuenta> crearCuenta(@Valid @RequestBody CuentaDTO cuentaDTO) {
        Cuenta cuenta = cuentaService.crearCuenta(cuentaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cuenta);
    }
    
    @GetMapping
    public ResponseEntity<List<Cuenta>> getAllCuentas() {
        List<Cuenta> cuentas = cuentaService.getAllCuentas();
        return ResponseEntity.ok(cuentas);
    }
    
    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<Cuenta> getCuentaByNumero(@PathVariable String numeroCuenta) {
        return cuentaService.getCuentaByNumero(numeroCuenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Cuenta>> getCuentasByCliente(@PathVariable Long clienteId) {
        List<Cuenta> cuentas = cuentaService.getCuentasByClienteId(clienteId);
        return ResponseEntity.ok(cuentas);
    }
    
    @GetMapping("/cliente/{clienteId}/activas")
    public ResponseEntity<List<Cuenta>> getCuentasActivasByCliente(@PathVariable Long clienteId) {
        List<Cuenta> cuentas = cuentaService.getCuentasActivasByClienteId(clienteId);
        return ResponseEntity.ok(cuentas);
    }
    
    @PutMapping("/{numeroCuenta}")
    public ResponseEntity<Cuenta> actualizarCuenta(
            @PathVariable String numeroCuenta,
            @Valid @RequestBody CuentaDTO cuentaDTO) {
        try {
            Cuenta cuenta = cuentaService.actualizarCuenta(numeroCuenta, cuentaDTO);
            return ResponseEntity.ok(cuenta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable String numeroCuenta) {
        cuentaService.eliminarCuenta(numeroCuenta);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/cliente/{clienteId}/saldo-total")
    public ResponseEntity<Double> getSaldoTotalCliente(@PathVariable Long clienteId) {
        Double saldoTotal = cuentaService.getSaldoTotalCliente(clienteId);
        return ResponseEntity.ok(saldoTotal);
    }
}