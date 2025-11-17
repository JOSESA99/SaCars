package com.sacars.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sacars.db.Database;
import com.sacars.models.Usuario;
import com.sacars.util.JWTUtil;
import com.sacars.util.JsonUtil;

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.sql.*;
import java.util.*;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!"POST".equals(exchange.getRequestMethod())) {
            enviarRespuesta(exchange, 405, "{\"success\":false,\"message\":\"Método no permitido\"}");
            return;
        }

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
                    put("token", token);
                    put("usuario", usuario);
                }});

                enviarRespuesta(exchange, 200, response);

            } else {

                enviarRespuesta(exchange, 401, "{\"success\":false,\"message\":\"Email o contraseña incorrectos\"}");
            }

        } catch (Exception e) {
            enviarError(exchange, e.getMessage());
        }
    }

    private Usuario autenticar(String email, String passwordIngresada) {

        try (Connection conn = Database.getConnection()) {

            String sql = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashGuardado = rs.getString("contrasena");

                if (BCrypt.checkpw(passwordIngresada, hashGuardado)) {
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error login: " + e.getMessage());
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
        enviarRespuesta(exchange, 500, "{\"success\":false,\"message\":\"Error: " + error + "\"}");
    }
}
