Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Verificando configuracion de Java 21" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$java21Path = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"

# Configurar para esta sesion
$env:JAVA_HOME = $java21Path
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Yellow
Write-Host ""

Write-Host "Version de Java:" -ForegroundColor Cyan
java -version
Write-Host ""

$version = java -version 2>&1 | Select-String "version"
if ($version -match "21") {
    Write-Host "OK: Java 21 esta activo" -ForegroundColor Green
} else {
    Write-Host "ERROR: Aun se esta usando otra version" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Configuracion del IDE" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Ahora en Eclipse/Spring Tool Suite:" -ForegroundColor Yellow
Write-Host "1. Window > Preferences > Java > Installed JREs" -ForegroundColor White
Write-Host "2. Click 'Add...' > Standard VM > Next" -ForegroundColor White
Write-Host "3. JRE home: $java21Path" -ForegroundColor White
Write-Host "4. Click 'Finish' y marca como default" -ForegroundColor White
Write-Host "5. Click derecho en cada proyecto > Maven > Update Project" -ForegroundColor White
Write-Host ""





