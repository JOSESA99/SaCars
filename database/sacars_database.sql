-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS sacars_db;
USE sacars_db;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contraseña VARCHAR(20) NOT NULL,  -- Contraseña en texto plano para fines académicos
    telefono VARCHAR(20),
    direccion TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rol ENUM('cliente', 'administrador') DEFAULT 'cliente',
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla de categorías
CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    imagen_url VARCHAR(255)
);

-- Tabla de productos
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    imagen_url VARCHAR(255),
    id_categoria INT,
    destacado BOOLEAN DEFAULT FALSE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

-- Tabla de pedidos
CREATE TABLE pedidos (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('pendiente', 'procesando', 'enviado', 'entregado', 'cancelado') DEFAULT 'pendiente',
    direccion_envio TEXT NOT NULL,
    ciudad_envio VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(20) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    metodo_pago ENUM('tarjeta', 'efectivo', 'transferencia') NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- Tabla de detalles del pedido
CREATE TABLE detalle_pedido (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- Tabla de carrito de compras
CREATE TABLE carrito (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    UNIQUE KEY (id_usuario, id_producto)
);

-- Tabla de contactos
CREATE TABLE contactos (
    id_contacto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    asunto VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_contacto TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leido BOOLEAN DEFAULT FALSE
);

-- Inserción de datos de ejemplo
-- Insertar categorías
INSERT INTO categorias (nombre, descripcion) VALUES
('Deportivos', 'Autos deportivos de alta gama'),
('Clásicos', 'Autos clásicos y de colección'),
('Tuners', 'Autos modificados y personalizados'),
('Carreras', 'Autos de competición'),
('Fantasía', 'Diseños únicos y especiales');

-- Insertar usuarios de ejemplo (contraseñas en texto plano para fines académicos)
INSERT INTO usuarios (nombre, apellido, email, contraseña, rol) VALUES
('Admin', 'Sistema', 'admin@sacars.com', 'admin123', 'administrador'),
('Juan', 'Pérez', 'cliente@example.com', 'cliente123', 'cliente');
