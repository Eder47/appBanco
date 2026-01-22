package com.bancario.dto;

import lombok.Data;

@Data
public class ClienteDTO {

    private Long clienteId;

    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private String contrasena;
    private Boolean estado = true;
}