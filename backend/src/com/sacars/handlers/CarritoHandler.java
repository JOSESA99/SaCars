package com.sacars.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sacars.db.Database;
import com.sacars.util.JsonUtil;
import com.sacars.util.JWTUtil;
import java.io.*;
import java.sql.*;
import java.util.*;

public class CarritoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                String token = extraerToken(exchange);
                int usuarioId = JWTUtil.verificarToken(token);
                
                if (usuarioId == -1) {
                    enviarRespuesta(exchange, 401, "{\"success\": false, \"message\": \"Token inválido\"}");
                    return;
                }
                
                List<Map<String, Object>> carrito = obtenerCarrito(usuarioId);
                
                String response = JsonUtil.toJson(new HashMap<String, Object>() {{
                    put("success", true);
                    put("data", carrito);
                }});
                
                enviarRespuesta(exchange, 200, response);
            } catch (Exception e) {
                enviarError(exchange, e.getMessage());
            }
        }
    }
    
    private List<Map<String, Object>> obtenerCarrito(int usuarioId) {
        List<Map<String, Object>> items = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT c.*, p.nombre, p.precio, p.imagen_url FROM carrito c JOIN productos p ON c.id_producto = p.id_producto WHERE c.id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                items.add(new HashMap<String, Object>() {{
                    put("id_item", rs.getInt("id_item"));
                    put("id_producto", rs.getInt("id_producto"));
                    put("nombre", rs.getString("nombre"));
                    put("precio", rs.getDouble("precio"));
                    put("cantidad", rs.getInt("cantidad"));
                    put("imagen_url", rs.getString("imagen_url"));
                }});
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return items;
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