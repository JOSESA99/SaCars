package com.sacars.repository;

import com.sacars.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Métodos existentes
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // Métodos para panel administrativo
    
    // Buscar por DNI
    Optional<Usuario> findByDni(String dni);
    boolean existsByDni(String dni);
    
    // Verificar si existe email excluyendo un usuario específico (para editar)
    boolean existsByEmailAndIdUsuarioNot(String email, Long idUsuario);
    
    // Verificar si existe DNI excluyendo un usuario específico (para editar)
    boolean existsByDniAndIdUsuarioNot(String dni, Long idUsuario);
    
    // Buscar solo clientes activos
    List<Usuario> findByRolAndActivo(Usuario.RolUsuario rol, boolean activo);
    
    // Contar clientes activos
    long countByRolAndActivo(Usuario.RolUsuario rol, boolean activo);
    
    // Contar nuevos clientes en un rango de fechas (última semana)
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    long countClientesActivos(Usuario.RolUsuario rol);
    
    // Buscar por nombre o apellido (búsqueda parcial)
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Usuario> buscarUsuarios(String busqueda);
}
