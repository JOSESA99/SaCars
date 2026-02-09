-- INSERT para usuario administrador
-- Email: santiadmin@gmail.com
-- Contraseña: 12345
-- Hash BCrypt compatible con Spring Security ($2a$10$)

-- Primero eliminar si existe
DELETE FROM usuarios WHERE email = 'santiadmin@gmail.com';

-- Insertar el nuevo usuario administrador
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
    'Santiago',
    'Administrador',
    '12345678',
    'santiadmin@gmail.com',
    '+51999999999',
    'Oficina Central SaCars',
    NOW(),
    'administrador',
    1,
    '$2a$10$8K1p/wlY7dFJctPCmBQ5n.X5yH8i0Dq7FgBZvL0YJ9kB0gJqNbK6K'
);

-- Verificar que se creó correctamente
SELECT idUsuario, nombre, apellido, email, rol, activo, contrasena 
FROM usuarios 
WHERE email = 'santiadmin@gmail.com';
