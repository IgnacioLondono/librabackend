-- Script SQL para crear las bases de datos en XAMPP MySQL
-- Ejecutar este script en phpMyAdmin o en la línea de comandos de MySQL

-- Crear base de datos para User Management Service
CREATE DATABASE IF NOT EXISTS library_users_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear base de datos para Book Catalog Service
CREATE DATABASE IF NOT EXISTS library_books_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear base de datos para Loan Management Service
CREATE DATABASE IF NOT EXISTS library_loans_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear base de datos para Reports Service
CREATE DATABASE IF NOT EXISTS library_reports_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear base de datos para Notifications Service
CREATE DATABASE IF NOT EXISTS library_notifications_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verificar que se crearon correctamente
SHOW DATABASES LIKE 'library_%';





