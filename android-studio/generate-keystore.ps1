# Script para generar keystore para firma de APK

$keystorePath = "keystore\library-release.jks"
$alias = "library-key"
$password = "library123"
$validity = 36500  # 100 años

Write-Host "Generando keystore para firma de APK..." -ForegroundColor Cyan

# Crear carpeta keystore si no existe
if (-not (Test-Path "keystore")) {
    New-Item -ItemType Directory -Path "keystore" | Out-Null
}

# Generar keystore
$keytoolPath = "$env:JAVA_HOME\bin\keytool.exe"
if (-not (Test-Path $keytoolPath)) {
    $keytoolPath = "keytool"
}

& $keytoolPath -genkeypair `
    -v `
    -keystore $keystorePath `
    -alias $alias `
    -keyalg RSA `
    -keysize 2048 `
    -validity $validity `
    -storepass $password `
    -keypass $password `
    -dname "CN=Library Digital, OU=Development, O=Library, L=City, ST=State, C=ES"

if ($LASTEXITCODE -eq 0) {
    Write-Host "`nKeystore generado exitosamente en: $keystorePath" -ForegroundColor Green
    Write-Host "Alias: $alias" -ForegroundColor Yellow
    Write-Host "Password: $password" -ForegroundColor Yellow
    Write-Host "`nIMPORTANTE: Guarda estos datos de forma segura!" -ForegroundColor Red
} else {
    Write-Host "Error al generar keystore" -ForegroundColor Red
}




