package com.sacars;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sacars.handlers.*;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Rutas de autenticación
        server.createContext("/api/auth/login", new LoginHandler());
        server.createContext("/api/auth/registro", new RegistroHandler());
        
        // Rutas de productos
        server.createContext("/api/productos", new ProductosHandler());
        server.createContext("/api/productos/destacados", new ProductosDestacadosHandler());
        
        // Rutas de carrito
        server.createContext("/api/carrito", new CarritoHandler());
        server.createContext("/api/carrito/agregar", new AgregarCarritoHandler());
        server.createContext("/api/carrito/eliminar", new EliminarCarritoHandler());
        
        // Rutas de pedidos
        server.createContext("/api/pedidos", new PedidosHandler());
        server.createContext("/api/pedidos/crear", new CrearPedidoHandler());
        
        // Habilitación de CORS
        server.createContext("/", new CORSHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("✅ Servidor SaCars corriendo en http://localhost:8080");
    }
}
