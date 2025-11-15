package com.sacars.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sacars.db.Database;
import com.sacars.util.JsonUtil;
import java.io.*;
import java.sql.*;
import java.util.*;

public class RegistroHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> data = JsonUtil.parseJson(body);
                
                String nombre = data.get("nombre");
                String email = data.get("email");
                String telefono = data.get("telefono");
                String password = data.get("password");
                
                if (registrarUsuario(nombre, email, telefono, password)) {
                    String response = JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("success", true);
                        put("message", "Registro exitoso");
                    }});
                    enviarRespuesta(exchange, 201, response);
                } else {
                    String response = JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("success", false);
                        put("message", "El email ya está registrado");
                    }});
                    enviarRespuesta(exchange, 400, response);
                }
            } catch (Exception e) {
                enviarError(exchange, e.getMessage());
            }
        }
    }
    
    private boolean registrarUsuario(String nombre, String email, String telefono, String password) {
        try (Connection conn = Database.getConnection()) {
            // Verificar si existe
            String checkSql = "SELECT id_usuario FROM usuarios WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            if (checkStmt.executeQuery().next()) {
                return false;
            }
            
            // Insertar nuevo usuario
            String insertSql = "INSERT INTO usuarios (nombre, apellido, email, contraseña, telefono, rol) VALUES (?, ?, ?, ?, ?, 'cliente')";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, nombre);
            insertStmt.setString(2, ""); // apellido vacío por ahora
            insertStmt.setString(3, email);
            insertStmt.setString(4, password);
            insertStmt.setString(5, telefono);
            
            insertStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en registro: " + e.getMessage());
            return false;
        }
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
