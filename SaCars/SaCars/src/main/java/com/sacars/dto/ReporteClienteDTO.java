package com.sacars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para reportes de clientes (RQ1.9)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteClienteDTO {
    
    private Long idUsuario;
    private String nombreCompleto;
    private String email;
    
    private Long totalPedidos;
    private BigDecimal totalGastado;
    
    private String ultimoPedidoFecha;
    private String estado; // "Activo", "Inactivo"
}
