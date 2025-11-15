package com.sacars.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sacars.db.Database;
import com.sacars.models.Usuario;
import com.sacars.util.JWTUtil;
import com.sacars.util.JsonUtil;
import java.io.*;
import java.sql.*;
import java.util.*;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> data = JsonUtil.parseJson(body);
                
                String email = data.get("email");
                String password = data.get("password");
                
                Usuario usuario = autenticar(email, password);
                
                if (usuario != null) {
                    String token = JWTUtil.generateToken(usuario.getId());
                    
                    String response = JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("success", true);
                        put("message", "Login exitoso");
                        put("data", new HashMap<String, Object>() {{
                            put("token", token);
                            put("usuario", new HashMap<String, Object>() {{
                                put("id", usuario.getId());
                                put("nombre", usuario.getNombre());
                                put("email", usuario.getEmail());
                                put("rol", usuario.getRol());
                            }});
                        }});
                    }});
                    
                    enviarRespuesta(exchange, 200, response);
                } else {
                    String response = JsonUtil.toJson(new HashMap<String, Object>() {{
                        put("success", false);
                        put("message", "Email o contraseña incorrectos");
                    }});
                    enviarRespuesta(exchange, 401, response);
                }
            } catch (Exception e) {
                enviarError(exchange, e.getMessage());
            }
        }
    }
    
    private Usuario autenticar(String email, String password) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE email = ? AND contraseña = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getString("rol")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error en autenticación: " + e.getMessage());
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
            put("message", "Error del servidor: " + error);
        }});
        enviarRespuesta(exchange, 500, response);
    }
}
