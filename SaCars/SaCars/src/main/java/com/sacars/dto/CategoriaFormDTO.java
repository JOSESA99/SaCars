package com.sacars.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para formulario de creación/edición de categorías (RQ1.7)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaFormDTO {
    
    private Long idCategoria;
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;
    
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;
    
    @Size(max = 255, message = "La URL de imagen no puede exceder 255 caracteres")
    private String imagenUrl;
}
