package com.sacars.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categorias")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;
    
    @Column(nullable = false, length = 50)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "imagen_url")
    private String imagenUrl;
}
