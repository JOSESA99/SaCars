package com.sacars.service;

import com.sacars.dto.ReporteClienteDTO;
import com.sacars.dto.ReporteVentasDTO;
import com.sacars.model.Pedido;
import com.sacars.model.Usuario;
import com.sacars.repository.PedidoRepository;
import com.sacars.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para Reportes del Sistema (RQ1.9)
 * Generación de reportes simples para negocio local
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReporteService {
    
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    
    /**
     * Reporte de ventas de HOY
     */
    public ReporteVentasDTO reporteVentasHoy() {
        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return generarReporteVentas(inicio, fin, "Hoy");
    }
    
    /**
     * Reporte de ventas de la ÚLTIMA SEMANA
     */
    public ReporteVentasDTO reporteVentasUltimaSemana() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fin = LocalDateTime.now();
        return generarReporteVentas(inicio, fin, "Última semana");
    }
    
    /**
     * Reporte de ventas del ÚLTIMO MES
     */
    public ReporteVentasDTO reporteVentasUltimoMes() {
        LocalDateTime inicio = LocalDateTime.now().minusMonths(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fin = LocalDateTime.now();
        return generarReporteVentas(inicio, fin, "Último mes");
    }
    
    /**
     * Reporte de ventas del MES ACTUAL
     */
    public ReporteVentasDTO reporteVentasMesActual() {
        LocalDateTime inicio = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fin = LocalDateTime.now();
        return generarReporteVentas(inicio, fin, "Mes actual");
    }
    
    /**
     * Reporte de ventas por período personalizado
     */
    public ReporteVentasDTO reporteVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String periodo = inicio.format(formatter) + " - " + fin.format(formatter);
        return generarReporteVentas(inicio, fin, periodo);
    }
    
    /**
     * Método interno para generar reporte de ventas
     */
    private ReporteVentasDTO generarReporteVentas(LocalDateTime inicio, LocalDateTime fin, String descripcion) {
        List<Pedido> pedidos = pedidoRepository.findByFechaPedidoBetween(inicio, fin);
        
        long totalPedidos = pedidos.size();
        long completados = pedidos.stream().filter(p -> "COMPLETADO".equals(p.getEstado())).count();
        long pendientes = pedidos.stream().filter(p -> "PENDIENTE".equals(p.getEstado())).count();
        
        BigDecimal totalVentas = pedidos.stream()
            .filter(p -> "COMPLETADO".equals(p.getEstado()))
            .map(Pedido::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal ticketPromedio = BigDecimal.ZERO;
        if (completados > 0) {
            ticketPromedio = totalVentas.divide(BigDecimal.valueOf(completados), 2, RoundingMode.HALF_UP);
        }
        
        return new ReporteVentasDTO(
            inicio,
            fin,
            totalPedidos,
            completados,
            pendientes,
            totalVentas,
            ticketPromedio,
            descripcion
        );
    }
    
    /**
     * Reporte de clientes registrados
     */
    public List<ReporteClienteDTO> reporteClientesRegistrados() {
        List<Usuario> clientes = usuarioRepository.findByRolAndActivo(Usuario.RolUsuario.cliente, true);
        
        return clientes.stream().map(cliente -> {
            List<Pedido> pedidos = pedidoRepository.findByUsuario_IdUsuario(cliente.getIdUsuario());
            
            BigDecimal totalGastado = pedidos.stream()
                .filter(p -> "COMPLETADO".equals(p.getEstado()))
                .map(Pedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            String ultimoPedido = pedidos.isEmpty() ? "Sin pedidos" :
                pedidos.get(pedidos.size() - 1).getFechaPedido()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            
            return new ReporteClienteDTO(
                cliente.getIdUsuario(),
                cliente.getNombre() + " " + cliente.getApellido(),
                cliente.getEmail(),
                (long) pedidos.size(),
                totalGastado,
                ultimoPedido,
                cliente.isActivo() ? "Activo" : "Inactivo"
            );
        }).collect(Collectors.toList());
    }
    
    /**
     * Reporte de clientes que más compraron (top 10)
     */
    public List<ReporteClienteDTO> reporteTopClientes() {
        List<ReporteClienteDTO> todosClientes = reporteClientesRegistrados();
        
        return todosClientes.stream()
            .sorted((c1, c2) -> c2.getTotalGastado().compareTo(c1.getTotalGastado()))
            .limit(10)
            .collect(Collectors.toList());
    }
    
    /**
     * Reporte de pedidos por estado
     */
    public Map<String, Long> reportePedidosPorEstado() {
        List<Pedido> todosPedidos = pedidoRepository.findAll();
        
        return todosPedidos.stream()
            .collect(Collectors.groupingBy(
                Pedido::getEstado,
                Collectors.counting()
            ));
    }
    
    /**
     * Reporte de pedidos por zona (ciudad)
     */
    public Map<String, Long> reportePedidosPorZona() {
        List<Pedido> todosPedidos = pedidoRepository.findAll();
        
        return todosPedidos.stream()
            .collect(Collectors.groupingBy(
                Pedido::getCiudadEnvio,
                Collectors.counting()
            ));
    }
    
    /**
     * Reporte de ingresos por zona
     */
    public Map<String, BigDecimal> reporteIngresosPorZona() {
        List<Pedido> pedidosCompletados = pedidoRepository.findByEstado("COMPLETADO");
        
        return pedidosCompletados.stream()
            .collect(Collectors.groupingBy(
                Pedido::getCiudadEnvio,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    Pedido::getTotal,
                    BigDecimal::add
                )
            ));
    }
    
    /**
     * Resumen general del negocio
     */
    public ResumenGeneral obtenerResumenGeneral() {
        // Clientes
        long totalClientes = usuarioRepository.countByRolAndActivo(Usuario.RolUsuario.cliente, true);
        
        // Pedidos
        long totalPedidos = pedidoRepository.count();
        long pendientes = pedidoRepository.countByEstado("PENDIENTE");
        long completados = pedidoRepository.countByEstado("COMPLETADO");
        
        // Ventas totales
        BigDecimal ventasTotales = pedidoRepository.findByEstado("COMPLETADO")
            .stream()
            .map(Pedido::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Ventas mes actual
        int anio = LocalDateTime.now().getYear();
        int mes = LocalDateTime.now().getMonthValue();
        BigDecimal ventasMes = pedidoRepository.calcularVentasMes(anio, mes);
        
        return new ResumenGeneral(
            totalClientes,
            totalPedidos,
            pendientes,
            completados,
            ventasTotales,
            ventasMes != null ? ventasMes : BigDecimal.ZERO
        );
    }
    
    /**
     * Clase interna para resumen general
     */
    public record ResumenGeneral(
        long totalClientes,
        long totalPedidos,
        long pedidosPendientes,
        long pedidosCompletados,
        BigDecimal ventasTotales,
        BigDecimal ventasMesActual
    ) {}
}
