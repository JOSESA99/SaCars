-- UPDATE para usuario administrador con hash correcto
-- Email: santiadmin@gmail.com
-- Contraseña: 12345
-- Hash generado por Spring Security BCrypt

UPDATE usuarios 
SET contrasena = '$2a$10$ZQZBeZMa3OLscNKs7BGQ5./6HjrC6YcM31NY5YGA32S1pKnCL/5zC'
WHERE email = 'santiadmin@gmail.com';

-- Si el usuario no existe, ejecuta esto:
INSERT INTO usuarios (
    nombre, apellido, dni, email, telefono, direccion, 
    fecha_registro, rol, activo, contrasena
) 
SELECT * FROM (
    SELECT 
        'Santiago' as nombre,
        'Administrador' as apellido,
        '12345678' as dni,
        'santiadmin@gmail.com' as email,
        '+51999999999' as telefono,
        'Oficina Central SaCars' as direccion,
        NOW() as fecha_registro,
        'administrador' as rol,
        1 as activo,
        '$2a$10$ZQZBeZMa3OLscNKs7BGQ5./6HjrC6YcM31NY5YGA32S1pKnCL/5zC' as contrasena
) AS tmp
WHERE NOT EXISTS (
    SELECT email FROM usuarios WHERE email = 'santiadmin@gmail.com'
) LIMIT 1;

-- Verificar
SELECT idUsuario, nombre, apellido, email, rol, activo 
FROM usuarios 
WHERE email = 'santiadmin@gmail.com';
