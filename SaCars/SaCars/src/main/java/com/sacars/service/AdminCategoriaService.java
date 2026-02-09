package com.sacars.service;

import com.sacars.dto.CategoriaFormDTO;
import com.sacars.model.Categoria;
import com.sacars.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para CRUD de Categorías (RQ1.7)
 * Gestión de categorías que organizan el catálogo de productos
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminCategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    
    /**
     * Listar todas las categorías
     */
    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }
    
    /**
     * Buscar categoría por ID
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }
    
    /**
     * Buscar categoría por nombre
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }
    
    /**
     * Crear nueva categoría
     * Valida que el nombre sea único
     */
    public Categoria crearCategoria(CategoriaFormDTO dto) {
        // Validar que el nombre no exista
        if (categoriaRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }
        
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setImagenUrl(dto.getImagenUrl());
        
        return categoriaRepository.save(categoria);
    }
    
    /**
     * Editar categoría existente
     * Valida que el nombre sea único (excluyendo la categoría actual)
     */
    public Categoria editarCategoria(Long id, CategoriaFormDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        
        // Validar que el nombre no exista en otra categoría
        if (categoriaRepository.existsByNombreAndIdCategoriaNot(dto.getNombre(), id)) {
            throw new IllegalArgumentException("Ya existe otra categoría con ese nombre");
        }
        
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setImagenUrl(dto.getImagenUrl());
        
        return categoriaRepository.save(categoria);
    }
    
    /**
     * Eliminar categoría permanentemente
     * NOTA: Verificar que no tenga productos asociados antes de eliminar
     */
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("Categoría no encontrada");
        }
        
        // TODO: Verificar si hay productos asociados a esta categoría
        // Si hay productos, no permitir eliminar o establecer categoría a NULL
        
        categoriaRepository.deleteById(id);
    }
}
