# Script PowerShell para compilar todos los microservicios
# Ejecuta: .\compile-all.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Compilando Microservicios de Biblioteca" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar Java
Write-Host "Verificando Java..." -ForegroundColor Yellow
$javaVersion = java -version 2>&1 | Select-String "version"
Write-Host $javaVersion

# Verificar Maven
Write-Host "`nVerificando Maven..." -ForegroundColor Yellow
try {
    $mvnVersion = mvn -version 2>&1 | Select-String "Apache Maven"
    Write-Host $mvnVersion
} catch {
    Write-Host "ERROR: Maven no está instalado o no está en el PATH" -ForegroundColor Red
    Write-Host "Por favor, instala Maven y agrégalo al PATH" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Lista de servicios
$services = @(
    "user-management-service",
    "book-catalog-service",
    "loan-management-service",
    "reports-service",
    "notifications-service"
)

$successCount = 0
$failCount = 0

foreach ($service in $services) {
    Write-Host "----------------------------------------" -ForegroundColor Cyan
    Write-Host "Compilando: $service" -ForegroundColor Green
    Write-Host "----------------------------------------" -ForegroundColor Cyan
    
    if (Test-Path $service) {
        Push-Location $service
        try {
            mvn clean install -DskipTests
            if ($LASTEXITCODE -eq 0) {
                Write-Host "✓ $service compilado exitosamente" -ForegroundColor Green
                $successCount++
            } else {
                Write-Host "✗ Error al compilar $service" -ForegroundColor Red
                $failCount++
            }
        } catch {
            Write-Host "✗ Error al compilar $service: $_" -ForegroundColor Red
            $failCount++
        }
        Pop-Location
    } else {
        Write-Host "✗ Carpeta $service no encontrada" -ForegroundColor Red
        $failCount++
    }
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Resumen de Compilación" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Exitosos: $successCount" -ForegroundColor Green
Write-Host "Fallidos: $failCount" -ForegroundColor Red

if ($failCount -eq 0) {
    Write-Host "`n✓ Todos los servicios compilados correctamente!" -ForegroundColor Green
} else {
    Write-Host "`n✗ Algunos servicios fallaron. Revisa los errores arriba." -ForegroundColor Red
}





