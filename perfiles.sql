create database gestionadt;
use gestionadt;
create table perfil(codigo_usuario varchar(30) primary key,nombre_usuario varchar(30) not null ,
					email varchar(30) not null,contrasena varchar(30) not null ,telefono int not null, nombre varchar(30) not null, apellido varchar(30) not null);

create table usuarios(codigo_usuario varchar(30), foreign key (codigo_usuario) references perfil(codigo_usuario), genero varchar(30) not null, n_tarjeta float not null);

create table administrador(codigo_usuario varchar(30), foreign key (codigo_usuario) references perfil(codigo_usuario), cuenta_corriente char(24));

INSERT INTO perfil (codigo_usuario, nombre_usuario, email, contrasena, telefono, nombre, apellido) VALUES
('A0001', 'maleck', 'maleck@example.com', 'mb28', 600111222, 'Maleck', 'Rodríguez'),
('A0002', 'alex', 'alex@example.com', 'pass123', 600333444, 'Alex', 'García'),
('A0003', 'sachin', 'sachin@example.com', 'abcd*1234', 600777888, 'Sachin', 'Patel'),
('A0004', 'jimena', 'jimena@example.com', '123', 600555666, 'Jimena', 'López');

-- Inserts en administrador para los perfiles A0001 a A0004
INSERT INTO administrador (codigo_usuario, cuenta_corriente) VALUES
('A0001', 'ES9121000418450200051332'),
('A0002', 'ES7921000418450200051333'),
('A0003', 'ES5621000418450200051334'),
('A0004', 'ES6821000418450200051335');

-- Inserts en perfil para usuarios (nombre de usuario derivado del nombre real)
INSERT INTO perfil (codigo_usuario, nombre_usuario, email, contrasena, telefono, nombre, apellido) VALUES
('C0001', 'carlos', 'carlos.mendez@example.com', 'userpass1', 600111111, 'Carlos', 'Méndez'),
('C0002', 'ana', 'ana.torres@example.com', 'userpass2', 600222222, 'Ana', 'Torres'),
('C0003', 'luis', 'luis.fernandez@example.com', 'userpass3', 600333333, 'Luis', 'Fernández'),
('C0004', 'maria', 'maria.santos@example.com', 'userpass4', 600444444, 'María', 'Santos');

-- Ejemplo de inserts en usuarios
INSERT INTO usuarios (codigo_usuario, genero, n_tarjeta) VALUES
('C0001', 'Masculino', 1111222233334444),
('C0002', 'Femenino', 2222333344445555),
('C0003', 'Masculino', 3333444455556666),
('C0004', 'Femenino', 4444555566667777);

