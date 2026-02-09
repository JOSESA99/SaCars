package com.sacars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para las estadísticas del dashboard administrativo (RQ1.4)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasDTO {
    
    // Clientes registrados
    private Long totalClientes;
    private Long clientesNuevosUltimaSemana;
    
    // Pedidos
    private Long pedidosPendientes;
    private Long pedidosCompletados;
    private Long pedidosTotales;
    
    // Ingresos
    private BigDecimal ingresosMesActual;
    private BigDecimal ingresosMesAnterior;
    private Double porcentajeCrecimiento;
    
    // Productos
    private Long totalProductos;
    private Long productosActivos;
    private Long totalUnidadesStock;
    private Long productosStockBajo;
    private Long productosSinStock;
}
