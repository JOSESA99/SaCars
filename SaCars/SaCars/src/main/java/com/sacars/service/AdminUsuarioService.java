package com.sacars.service;

import com.sacars.dto.UsuarioFormDTO;
import com.sacars.model.Usuario;
import com.sacars.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para CRUD de Usuarios (RQ1.5)
 * Gestión completa de usuarios por administradores
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminUsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Listar todos los usuarios (clientes y administradores)
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    /**
     * Listar solo clientes activos
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarClientes() {
        return usuarioRepository.findByRolAndActivo(Usuario.RolUsuario.cliente, true);
    }
    
    /**
     * Buscar usuario por ID
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    /**
     * Buscar usuarios por nombre, apellido o email
     */
    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuarios(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return listarTodos();
        }
        return usuarioRepository.buscarUsuarios(busqueda.trim());
    }
    
    /**
     * Crear nuevo usuario
     * Valida que email y DNI sean únicos
     */
    public Usuario crearUsuario(UsuarioFormDTO dto) {
        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
        
        // Validar que el DNI no exista
        if (usuarioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese DNI");
        }
        
        // Validar que se proporcione contraseña
        if (dto.getContrasena() == null || dto.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria para crear un usuario");
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDni(dto.getDni());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setRol(dto.getRol());
        usuario.setActivo(dto.getActivo());
        
        // Encriptar contraseña
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Editar usuario existente
     * Permite modificar todos los campos excepto la contraseña
     * Valida que email y DNI sean únicos (excluyendo el usuario actual)
     */
    public Usuario editarUsuario(Long id, UsuarioFormDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        // Validar que el email no exista en otro usuario
        if (usuarioRepository.existsByEmailAndIdUsuarioNot(dto.getEmail(), id)) {
            throw new IllegalArgumentException("Ya existe otro usuario con ese email");
        }
        
        // Validar que el DNI no exista en otro usuario
        if (usuarioRepository.existsByDniAndIdUsuarioNot(dto.getDni(), id)) {
            throw new IllegalArgumentException("Ya existe otro usuario con ese DNI");
        }
        
        // Actualizar campos
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setDni(dto.getDni());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setRol(dto.getRol());
        usuario.setActivo(dto.getActivo());
        
        // NO actualizamos la contraseña aquí (se haría en un método separado)
        
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Activar un usuario
     */
    public Usuario activarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Desactivar un usuario (soft delete)
     * Al desactivar:
     * - No podrá iniciar sesión
     * - Sus pedidos pendientes permanecen
     * - Su información se mantiene en BD
     * - Puede ser reactivado
     */
    public Usuario desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuario.setActivo(false);
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Cambiar contraseña de un usuario
     */
    public void cambiarContrasena(Long id, String nuevaContrasena) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
    }
    
    /**
     * Eliminar usuario permanentemente (hard delete)
     * NOTA: Usar con precaución, puede causar problemas con pedidos asociados
     */
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
