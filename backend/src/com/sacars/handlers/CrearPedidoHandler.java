package com.sacars.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sacars.db.Database;
import com.sacars.util.JsonUtil;
import com.sacars.util.JWTUtil;
import java.io.*;
import java.sql.*;
import java.util.*;

public class CrearPedidoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String token = extraerToken(exchange);
                int usuarioId = JWTUtil.verificarToken(token);
                
                if (usuarioId == -1) {
                    enviarRespuesta(exchange, 401, "{\"success\": false, \"message\": \"Token inválido\"}");
                    return;
                }
                
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> data = JsonUtil.parseJson(body);
                
                String direccion = data.get("direccion");
                String ciudad = data.get("ciudad");
                String codigoPostal = data.get("codigo_postal");
                String metodoPago = data.get("metodo_pago");
                double total = Double.parseDouble(data.get("total"));
                
                int pedidoId = crearPedido(usuarioId, direccion, ciudad, codigoPostal, total, metodoPago);
                
                if (pedidoId > 0) {
                    String response = JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("success", true);
                        put("message", "Pedido creado exitosamente");
                        put("data", new HashMap<String, Object>() {{
                            put("pedido_id", pedidoId);
                        }});
                    }});
                    enviarRespuesta(exchange, 201, response);
                } else {
                    enviarRespuesta(exchange, 400, "{\"success\": false, \"message\": \"Error al crear pedido\"}");
                }
            } catch (Exception e) {
                enviarError(exchange, e.getMessage());
            }
        }
    }
    
    private int crearPedido(int usuarioId, String direccion, String ciudad, String codigoPostal, double total, String metodoPago) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO pedidos (id_usuario, direccion_envio, ciudad_envio, codigo_postal, total, metodo_pago, estado) VALUES (?, ?, ?, ?, ?, ?, 'pendiente')";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, usuarioId);
            stmt.setString(2, direccion);
            stmt.setString(3, ciudad);
            stmt.setString(4, codigoPostal);
            stmt.setDouble(5, total);
            stmt.setString(6, metodoPago);
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return -1;
    }
    
    private String extraerToken(HttpExchange exchange) {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return null;
    }
    
    private void enviarRespuesta(HttpExchange exchange, int code, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(code, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
    
    private void enviarError(HttpExchange exchange, String error) throws IOException {
        String response = JsonUtil.toJson(new HashMap<String, Object>() {{
            put("success", false);
            put("message", "Error: " + error);
        }});
        enviarRespuesta(exchange, 500, response);
    }
}
