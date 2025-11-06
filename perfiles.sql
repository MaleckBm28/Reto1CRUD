CREATE DATABASE gestionadt;
USE gestionadt;

CREATE TABLE perfil(
    codigo_usuario VARCHAR(30) PRIMARY KEY,   -- clave primaria
    email VARCHAR(30) NOT NULL UNIQUE,        -- único y no nulo
    contrasena VARCHAR(30) NOT NULL,
    nombre_usuario VARCHAR(30) NOT NULL,
    telefono INT NOT NULL,
    nombre VARCHAR(30) NOT NULL,
    apellido VARCHAR(30) NOT NULL
);
CREATE TABLE usuarios(
    codigo_usuario VARCHAR(30),
    FOREIGN KEY (codigo_usuario) REFERENCES perfil(codigo_usuario) ON DELETE CASCADE,
    genero VARCHAR(30) NOT NULL,
    n_tarjeta BIGINT NOT NULL
);

CREATE TABLE administrador(
    codigo_usuario VARCHAR(30),
    FOREIGN KEY (codigo_usuario) REFERENCES perfil(codigo_usuario) ON DELETE CASCADE,
    cuenta_corriente CHAR(24)
);

-- Administradores
INSERT INTO perfil (codigo_usuario, email, contrasena, nombre_usuario, telefono, nombre, apellido) VALUES
('A0001', 'admin@example.com', '1234', 'admin', 600111222, 'Admin', '_1');
-- Clientes
INSERT INTO perfil (codigo_usuario, email, contrasena, nombre_usuario, telefono, nombre, apellido) VALUES
('C0001', 'carlos.mendez@example.com', 'userpass1', 'carlos', 600111111, 'Carlos', 'Méndez'),
('C0002', 'ana.torres@example.com', 'userpass2', 'ana', 600222222, 'Ana', 'Torres'),
('C0003', 'luis.fernandez@example.com', 'userpass3', 'luis', 600333333, 'Luis', 'Fernández'),
('C0004', 'maria.santos@example.com', 'userpass4', 'maria', 600444444, 'María', 'Santos');


INSERT INTO usuarios (codigo_usuario, genero, n_tarjeta) VALUES
('C0001', 'Masculino', 1111222233334444),
('C0002', 'Femenino', 2222333344445555),
('C0003', 'Masculino', 3333444455556666),
('C0004', 'Femenino', 4444555566667777);

INSERT INTO administrador (codigo_usuario, cuenta_corriente) VALUES
('A0001', 'ES9121000418450200051332');