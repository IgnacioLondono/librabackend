# Script PowerShell para verificar y crear bases de datos MySQL en XAMPP

Write-Host "Verificando conexión a MySQL..." -ForegroundColor Yellow

# Intentar conectar a MySQL (sin contraseña por defecto en XAMPP)
$mysqlPath = "C:\xampp\mysql\bin\mysql.exe"

if (-not (Test-Path $mysqlPath)) {
    Write-Host "ERROR: No se encontró MySQL en XAMPP. Verifica la ruta o que XAMPP esté instalado." -ForegroundColor Red
    Write-Host "Ruta esperada: $mysqlPath" -ForegroundColor Yellow
    exit 1
}

Write-Host "MySQL encontrado en: $mysqlPath" -ForegroundColor Green

# Crear las bases de datos
Write-Host "`nCreando bases de datos..." -ForegroundColor Yellow

$sqlScript = @"
CREATE DATABASE IF NOT EXISTS library_users_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS library_books_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS library_loans_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS library_reports_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS library_notifications_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
SHOW DATABASES LIKE 'library_%';
"@

try {
    $sqlScript | & $mysqlPath -u root
    Write-Host "`nBases de datos creadas exitosamente!" -ForegroundColor Green
} catch {
    Write-Host "`nERROR al crear bases de datos: $_" -ForegroundColor Red
    Write-Host "`nAsegúrate de que:" -ForegroundColor Yellow
    Write-Host "1. XAMPP MySQL esté corriendo" -ForegroundColor Yellow
    Write-Host "2. El usuario root no tenga contraseña (o ajusta el script)" -ForegroundColor Yellow
    Write-Host "`nAlternativa: Ejecuta el archivo database-setup.sql en phpMyAdmin" -ForegroundColor Cyan
}

Write-Host "`nVerificando puertos MySQL..." -ForegroundColor Yellow
$port3306 = netstat -an | findstr ":3306"
if ($port3306) {
    Write-Host "MySQL está corriendo en el puerto 3306" -ForegroundColor Green
} else {
    Write-Host "ADVERTENCIA: No se detectó MySQL en el puerto 3306" -ForegroundColor Red
    Write-Host "Inicia MySQL desde el Panel de Control de XAMPP" -ForegroundColor Yellow
}







