package com.sacars.models;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String imagenUrl;
    private boolean destacado;
    
    public Producto(int id, String nombre, String descripcion, double precio, int stock, String imagenUrl, boolean destacado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.destacado = destacado;
    }
    
    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getImagenUrl() { return imagenUrl; }
    public boolean isDestacado() { return destacado; }
}
