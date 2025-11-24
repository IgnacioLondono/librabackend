Write-Host "Verificando Java..." -ForegroundColor Cyan
$javaVersion = java -version 2>&1 | Select-String "version"
if ($javaVersion -match "21") {
    Write-Host "OK: Java 21 encontrado" -ForegroundColor Green
} else {
    Write-Host "ERROR: Java 21 no encontrado. Instala Java 21 primero." -ForegroundColor Red
    Write-Host "Descarga: https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Yellow
    exit 1
}

Write-Host "`nVerificando Maven..." -ForegroundColor Cyan
try {
    $mvnVersion = mvn -version 2>&1 | Select-String "Apache Maven"
    if ($mvnVersion) {
        Write-Host "OK: Maven encontrado" -ForegroundColor Green
    }
} catch {
    Write-Host "ERROR: Maven no encontrado. Instala Maven primero." -ForegroundColor Red
    Write-Host "Descarga: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    exit 1
}

Write-Host "`nArchivos de configuracion del IDE creados." -ForegroundColor Green
Write-Host "Ahora:" -ForegroundColor Yellow
Write-Host "1. Abre Eclipse/Spring Tool Suite" -ForegroundColor White
Write-Host "2. Window > Preferences > Java > Installed JREs" -ForegroundColor White
Write-Host "3. Agrega Java 21 y marca como default" -ForegroundColor White
Write-Host "4. File > Import > Existing Projects into Workspace" -ForegroundColor White
Write-Host "5. Selecciona la carpeta libra_services" -ForegroundColor White
Write-Host "6. Click derecho en cada proyecto > Maven > Update Project" -ForegroundColor White





