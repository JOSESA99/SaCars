package com.sacars.dto;

import com.sacars.model.Usuario.RolUsuario;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para formulario de creación/edición de usuarios (RQ1.5)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioFormDTO {
    
    private Long idUsuario;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;
    
    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe contener solo números")
    private String dni;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    private String direccion;
    
    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol = RolUsuario.cliente;
    
    private Boolean activo = true;
    
    // Solo se usa cuando se crea un nuevo usuario
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
}
