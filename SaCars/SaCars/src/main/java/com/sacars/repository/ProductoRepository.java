package com.sacars.repository;

import com.sacars.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar productos por categoría
    List<Producto> findByIdCategoria(Long idCategoria);
    
    // Buscar productos activos
    List<Producto> findByActivo(Boolean activo);
    
    // Buscar productos por categoría y estado
    List<Producto> findByIdCategoriaAndActivo(Long idCategoria, Boolean activo);
    
    // Buscar por nombre (búsqueda parcial)
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Producto> buscarProductos(String busqueda);
    
    // Productos con stock bajo (menos de X unidades)
    @Query("SELECT p FROM Producto p WHERE p.stock < :cantidad AND p.activo = true")
    List<Producto> findProductosStockBajo(Integer cantidad);
    
    // Productos sin stock
    @Query("SELECT p FROM Producto p WHERE p.stock = 0 AND p.activo = true")
    List<Producto> findProductosSinStock();
    
    // Contar productos activos
    long countByActivo(Boolean activo);
    
    // Contar total de unidades en stock
    @Query("SELECT COALESCE(SUM(p.stock), 0) FROM Producto p WHERE p.activo = true")
    Long sumTotalStock();
}
