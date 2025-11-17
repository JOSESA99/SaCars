package com.sacars.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sacars.db.Database;
import com.sacars.util.JsonUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.sql.*;
import java.util.*;

public class RegistroHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!"POST".equals(exchange.getRequestMethod())) {
            enviarRespuesta(exchange, 405, "{\"message\":\"Método no permitido\"}");
            return;
        }

        try {
            String body = new String(exchange.getRequestBody().readAllBytes());
            Map<String, String> data = JsonUtil.parseJson(body);

            String nombre = data.get("nombre");
            String apellido = data.get("apellido");
            String email = data.get("email");
            String telefono = data.get("telefono");
            String password = data.get("password");

            // Generar hash seguro
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));

            boolean ok = registrarUsuario(nombre, apellido, email, telefono, hashedPassword);

            if (ok) {
                enviarRespuesta(exchange, 201, "{\"success\":true,\"message\":\"Registro exitoso\"}");
            } else {
                enviarRespuesta(exchange, 400, "{\"success\":false,\"message\":\"Email ya está registrado\"}");
            }

        } catch (Exception e) {
            enviarError(exchange, e.toString());
        }
    }

    private boolean registrarUsuario(String nombre, String apellido, String email, String telefono, String hashedPassword) {
        try (Connection conn = Database.getConnection()) {

            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT id_usuario FROM usuarios WHERE email = ?"
            );
            checkStmt.setString(1, email);

            if (checkStmt.executeQuery().next()) {
                return false;
            }

            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO usuarios (nombre, apellido, email, contrasena, telefono, rol) VALUES (?, ?, ?, ?, ?, 'cliente')"
            );

            insertStmt.setString(1, nombre);
            insertStmt.setString(2, apellido);
            insertStmt.setString(3, email);
            insertStmt.setString(4, hashedPassword);
            insertStmt.setString(5, telefono);

            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR SQL REGISTRO: " + e.getMessage());
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
        String response = "{\"success\":false,\"message\":\"Error interno: " + error + "\"}";
        enviarRespuesta(exchange, 500, response);
    }
}
