package com.sacars.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class CORSHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Headers CORS para TODAS las peticiones
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Solo responder sin body
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        // Si llega aquí, no es OPTIONS → permitir que los demás handlers actúen
        String response = "CORS OK";
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
