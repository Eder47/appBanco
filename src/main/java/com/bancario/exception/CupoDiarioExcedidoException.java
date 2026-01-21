package com.bancario.exception;

public class CupoDiarioExcedidoException extends RuntimeException {
    public CupoDiarioExcedidoException(String message) {
        super(message);
    }
}