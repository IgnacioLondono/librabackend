Write-Host "Configurando IntelliJ IDEA para Java 21..." -ForegroundColor Cyan

$java21Path = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"

Write-Host "`nPasos para configurar IntelliJ IDEA:" -ForegroundColor Yellow
Write-Host "1. Abre IntelliJ IDEA" -ForegroundColor White
Write-Host "2. File > Open > Selecciona la carpeta libra_services" -ForegroundColor White
Write-Host "3. File > Project Structure (Ctrl+Alt+Shift+S)" -ForegroundColor White
Write-Host "   - Project > SDK: Selecciona Java 21" -ForegroundColor White
Write-Host "   - Si no aparece, click '+' > Add JDK > Selecciona:" -ForegroundColor White
Write-Host "     $java21Path" -ForegroundColor Gray
Write-Host "   - Project > Language Level: 21" -ForegroundColor White
Write-Host "4. Para cada modulo (Modules):" -ForegroundColor White
Write-Host "   - Selecciona el modulo" -ForegroundColor White
Write-Host "   - SDK: Java 21" -ForegroundColor White
Write-Host "5. File > Settings > Build, Execution, Deployment > Build Tools > Maven" -ForegroundColor White
Write-Host "   - Maven home path: (ruta a Maven si esta instalado)" -ForegroundColor White
Write-Host "6. Click derecho en cada modulo > Maven > Reload Project" -ForegroundColor White





