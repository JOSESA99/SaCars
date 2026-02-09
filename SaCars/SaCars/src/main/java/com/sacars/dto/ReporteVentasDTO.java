package com.sacars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para reportes de ventas (RQ1.9)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteVentasDTO {
    
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    
    private Long totalPedidos;
    private Long pedidosCompletados;
    private Long pedidosPendientes;
    
    private BigDecimal totalVentas;
    private BigDecimal ticketPromedio;
    
    private String periodoDescripcion; // "Hoy", "Última semana", "Último mes"
}
