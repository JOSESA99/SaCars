package com.sacars.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sacars.db.Database;
import com.sacars.models.Producto;
import com.sacars.util.JsonUtil;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ProductosHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                List<Producto> productos = obtenerProductos();
                
                String response = JsonUtil.toJson(new HashMap<String, Object>() {{
                    put("success", true);
                    put("data", productos);
                }});
                
                enviarRespuesta(exchange, 200, response);
            } catch (Exception e) {
                enviarError(exchange, e.getMessage());
            }
        }
    }
    
    private List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM productos WHERE activo = true";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                productos.add(new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getString("imagen_url"),
                    rs.getBoolean("destacado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
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
