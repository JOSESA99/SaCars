-- INSERT para usuario administrador
-- Email: admin@sacars.com
-- Contraseña: admin123
-- Hash BCrypt compatible con Spring Security ($2a$)

DELETE FROM usuarios WHERE email = 'admin@sacars.com';

INSERT INTO usuarios (
    nombre, 
    apellido, 
    dni, 
    email, 
    telefono, 
    direccion, 
    fecha_registro, 
    rol, 
    activo, 
    contrasena
) VALUES (
    'Admin',
    'Principal',
    '00000000',
    'admin@sacars.com',
    '+000000000',
    'Oficina Central',
    NOW(),
    'administrador',
    1,
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
);

-- Verificar que se creó correctamente
SELECT * FROM usuarios WHERE email = 'admin@sacars.com';
