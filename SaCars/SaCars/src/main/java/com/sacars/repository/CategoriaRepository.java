package com.sacars.repository;

import com.sacars.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Buscar categoría por nombre
    Optional<Categoria> findByNombre(String nombre);
    
    // Verificar si existe una categoría con ese nombre
    boolean existsByNombre(String nombre);
    
    // Verificar si existe una categoría con ese nombre excluyendo un ID específico
    boolean existsByNombreAndIdCategoriaNot(String nombre, Long idCategoria);
}
