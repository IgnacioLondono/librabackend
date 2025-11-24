Write-Host "Buscando Java 21 instalado..." -ForegroundColor Cyan

$possiblePaths = @(
    "C:\Program Files\Java\jdk-21",
    "C:\Program Files\Java\jdk-21.0.*",
    "C:\Program Files\Eclipse Adoptium\jdk-21*",
    "C:\Program Files\Microsoft\jdk-21*",
    "C:\Program Files\Amazon Corretto\jdk21*",
    "$env:LOCALAPPDATA\Programs\Eclipse Adoptium\jdk-21*",
    "$env:ProgramFiles\Eclipse Adoptium\jdk-21*"
)

$java21Path = $null

foreach ($pathPattern in $possiblePaths) {
    $found = Get-ChildItem -Path (Split-Path $pathPattern -Parent) -Filter (Split-Path $pathPattern -Leaf) -Directory -ErrorAction SilentlyContinue | 
             Where-Object { Test-Path "$($_.FullName)\bin\java.exe" } |
             Sort-Object Name -Descending |
             Select-Object -First 1
    
    if ($found) {
        $java21Path = $found.FullName
        Write-Host "Encontrado Java 21 en: $java21Path" -ForegroundColor Green
        break
    }
}

if (-not $java21Path) {
    Write-Host "`nNo se encontro Java 21 automaticamente." -ForegroundColor Yellow
    Write-Host "Por favor, indica la ruta donde instalaste Java 21" -ForegroundColor Yellow
    Write-Host "Ejemplo: C:\Program Files\Java\jdk-21" -ForegroundColor Gray
    $java21Path = Read-Host "Ruta de Java 21"
    
    if (-not (Test-Path "$java21Path\bin\java.exe")) {
        Write-Host "ERROR: No se encontro java.exe en esa ruta" -ForegroundColor Red
        exit 1
    }
}

Write-Host "`nVerificando version de Java..." -ForegroundColor Cyan
$version = & "$java21Path\bin\java.exe" -version 2>&1
Write-Host $version

if ($version -notmatch "21") {
    Write-Host "ADVERTENCIA: La version no parece ser 21" -ForegroundColor Yellow
}

Write-Host "`nConfigurando variables de entorno..." -ForegroundColor Cyan

# Configurar JAVA_HOME para esta sesion
$env:JAVA_HOME = $java21Path
Write-Host "JAVA_HOME (sesion actual): $env:JAVA_HOME" -ForegroundColor Green

# Agregar al PATH de esta sesion
$env:PATH = "$java21Path\bin;$env:PATH"
Write-Host "PATH actualizado (sesion actual)" -ForegroundColor Green

Write-Host "`nVerificando configuracion..." -ForegroundColor Cyan
$newVersion = java -version 2>&1 | Select-String "version"
Write-Host $newVersion

if ($newVersion -match "21") {
    Write-Host "`nOK: Java 21 configurado correctamente para esta sesion" -ForegroundColor Green
} else {
    Write-Host "`nADVERTENCIA: Aun se esta usando una version anterior" -ForegroundColor Yellow
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Para hacer permanente la configuracion:" -ForegroundColor Yellow
Write-Host "1. Abre 'Variables de entorno' en Windows" -ForegroundColor White
Write-Host "2. Crea/modifica JAVA_HOME = $java21Path" -ForegroundColor White
Write-Host "3. Agrega %JAVA_HOME%\bin al inicio del PATH" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "`nO ejecuta este comando como Administrador:" -ForegroundColor Yellow
Write-Host "[System.Environment]::SetEnvironmentVariable('JAVA_HOME', '$java21Path', 'Machine')" -ForegroundColor Gray
Write-Host '$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH' -ForegroundColor Gray
Write-Host '[System.Environment]::SetEnvironmentVariable("PATH", $env:PATH, "Machine")' -ForegroundColor Gray





