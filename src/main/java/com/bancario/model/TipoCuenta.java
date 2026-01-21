package com.bancario.model;

public enum TipoCuenta {
    AHORRO("AHORRO"),
    CORRIENTE("CORRIENTE");
    
    private final String valor;
    
    TipoCuenta(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
}