-- Creación de la tabla clientes
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    rut VARCHAR(12) NOT NULL UNIQUE,
    telefono VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    direccion VARCHAR(200) NOT NULL,
    fecha_registro DATE NOT NULL
);

-- Datos iniciales
INSERT INTO clientes (nombre, rut, telefono, email, direccion, fecha_registro) VALUES
('Juan Pérez González',    '12345678-9', '+56912345678', 'juan.perez@email.com',    'Av. Providencia 1234, Santiago',     '2024-01-10'),
('María López Soto',       '98765432-1', '+56987654321', 'maria.lopez@email.com',   'Calle Las Rosas 456, Las Condes',    '2024-02-15'),
('Carlos Muñoz Díaz',      '11223344-5', '+56911223344', 'carlos.munoz@email.com',  'Pasaje Los Andes 789, Ñuñoa',        '2024-03-20'),
('Ana Ramírez Torres',     '55667788-K', '+56955667788', 'ana.ramirez@email.com',   'Av. Irarrázaval 2020, Providencia',  '2024-04-05'),
('Pedro Castillo Vega',    '33445566-7', '+56933445566', 'pedro.castillo@email.com','Los Leones 321, Vitacura',           '2024-05-12');
