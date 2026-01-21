package com.bancario.model;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class Persona {
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "genero")
    private String genero;
    
    @Column(name = "edad")
    private Integer edad;
    
    @Column(name = "identificacion", unique = true, nullable = false)
    private String identificacion;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "telefono")
    private String telefono;
}