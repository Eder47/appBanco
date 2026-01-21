package com.bancario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClienteDTO {
   // @NotBlank(message = "Nombre es requerido")
    private String nombre;
    
    private String genero;
    
    //@Min(value = 18, message = "Edad mínima 18 años")
    private Integer edad;
    
    //@NotBlank(message = "Identificación es requerida")
    private String identificacion;
    
    private String direccion;
    
    //@Pattern(regexp = "^09\\d{8}$", message = "Teléfono inválido")
    private String telefono;
    
   // @NotBlank(message = "Contraseña es requerida")
    //@Size(min = 4, message = "Contraseña mínimo 4 caracteres")
    private String contrasena;
    
    private Boolean estado = true;
}