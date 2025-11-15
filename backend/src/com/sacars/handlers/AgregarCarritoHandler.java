package com.sacars.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sacars.db.Database;
import com.sacars.util.JsonUtil;
import com.sacars.util.JWTUtil;
import java.io.*;
import java.sql.*;
import java.util.*;

public class AgregarCarritoHandler implements HttpHandler {
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
                
                int productoId = Integer.parseInt(data.get("id_producto"));
                int cantidad = Integer.parseInt(data.getOrDefault("cantidad", "1"));
                
                if (agregarAlCarrito(usuarioId, productoId, cantidad)) {
                    String response = JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("success", true);
                        put("message", "Producto agregado al carrito");
                    }});
                    enviarRespuesta(exchange, 201, response);
                } else {
                    enviarRespuesta(exchange, 400, "{\"success\": false, \"message\": \"Error al agregar\"}");
                }
            } catch (Exception e) {
                enviarError(exchange, e.getMessage());
            }
        }
    }
    
    private boolean agregarAlCarrito(int usuarioId, int productoId, int cantidad) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO carrito (id_usuario, id_producto, cantidad) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE cantidad = cantidad + ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, productoId);
            stmt.setInt(3, cantidad);
            stmt.setInt(4, cantidad);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
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
