Write-Host "Configurando VS Code para Java 21..." -ForegroundColor Cyan

$java21Path = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"

# Crear archivo settings.json para VS Code
$vscodeSettings = @{
    "java.configuration.runtimes" = @(
        @{
            "name" = "JavaSE-21"
            "path" = $java21Path
            "default" = $true
        }
    )
    "java.home" = $java21Path
    "java.jdt.ls.java.home" = $java21Path
    "java.compile.nullAnalysis.mode" = "automatic"
} | ConvertTo-Json -Depth 10

# Crear carpeta .vscode si no existe
if (-not (Test-Path ".vscode")) {
    New-Item -ItemType Directory -Path ".vscode" | Out-Null
}

Set-Content -Path ".vscode\settings.json" -Value $vscodeSettings

Write-Host "`nArchivo .vscode\settings.json creado" -ForegroundColor Green
Write-Host "`nInstala estas extensiones en VS Code:" -ForegroundColor Yellow
Write-Host "1. Extension Pack for Java (Microsoft)" -ForegroundColor White
Write-Host "2. Spring Boot Extension Pack (VMware)" -ForegroundColor White
Write-Host "`nLuego:" -ForegroundColor Yellow
Write-Host "1. Abre VS Code en esta carpeta" -ForegroundColor White
Write-Host "2. Ctrl+Shift+P > Java: Clean Java Language Server Workspace" -ForegroundColor White
Write-Host "3. Reinicia VS Code" -ForegroundColor White





