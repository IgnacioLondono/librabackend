# Script para generar APK firmado en modo release

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Generando APK Release Firmado" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que existe el keystore
$keystorePath = "keystore\library-release.jks"
if (-not (Test-Path $keystorePath)) {
    Write-Host "ERROR: No se encontro el keystore" -ForegroundColor Red
    Write-Host "Ejecuta primero: .\generate-keystore.ps1" -ForegroundColor Yellow
    exit 1
}

Write-Host "1. Limpiando proyecto..." -ForegroundColor Yellow
.\gradlew clean

Write-Host "`n2. Generando APK Release..." -ForegroundColor Yellow
.\gradlew assembleRelease

if ($LASTEXITCODE -eq 0) {
    $apkPath = "app\build\outputs\apk\release\app-release.apk"
    if (Test-Path $apkPath) {
        Write-Host "`n========================================" -ForegroundColor Green
        Write-Host "APK generado exitosamente!" -ForegroundColor Green
        Write-Host "========================================" -ForegroundColor Green
        Write-Host "Ubicacion: $apkPath" -ForegroundColor Yellow
        Write-Host "Tamaño: $((Get-Item $apkPath).Length / 1MB) MB" -ForegroundColor Yellow
        
        # Abrir carpeta
        Start-Process explorer.exe -ArgumentList "/select,$apkPath"
    } else {
        Write-Host "ERROR: APK no encontrado en la ruta esperada" -ForegroundColor Red
    }
} else {
    Write-Host "ERROR: Fallo al generar APK" -ForegroundColor Red
}




