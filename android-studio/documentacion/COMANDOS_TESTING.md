# 🧪 Comandos para Testing en Android Studio / VS Code

## 📋 Ejecutar Tests

### Android Studio

1. **Ejecutar todos los tests:**
   - Click derecho en carpeta `test/` → "Run 'Tests in 'test''"
   - O: `Ctrl+Shift+F10` (Windows/Linux) / `Cmd+Shift+R` (Mac)

2. **Ejecutar un test específico:**
   - Click en icono ▶️ junto a `@Test`
   - O: `Ctrl+Shift+F10` sobre el método

3. **Ejecutar con cobertura:**
   - Run → Run with Coverage
   - O: Click derecho en test → "Run 'Test' with Coverage"

4. **Desde terminal (Gradle):**
   ```bash
   ./gradlew test
   ```

### Visual Studio Code

1. **Instalar extensiones:**
   - Extension Pack for Java
   - Kotlin Language
   - Test Explorer UI

2. **Ejecutar tests:**
   - Abrir archivo de test
   - Click en "Run Test" sobre `@Test`
   - O: Command Palette (`Ctrl+Shift+P`) → "Java: Run Tests"

3. **Ver resultados:**
   - Panel "Testing" en barra lateral
   - Ver detalles de cada test

## 📊 Ver Cobertura

### Android Studio

1. **Ejecutar con cobertura:**
   - Run → Run with Coverage
   - Seleccionar tests a ejecutar

2. **Ver reporte:**
   - Panel "Coverage" muestra porcentajes
   - Click en archivo para ver líneas cubiertas

### VS Code

1. **Instalar extensión:**
   - Coverage Gutters

2. **Generar reporte:**
   ```bash
   ./gradlew test jacocoTestReport
   ```

3. **Ver cobertura:**
   - Command Palette → "Coverage Gutters: Display Coverage"
   - Líneas verdes = cubiertas, rojas = no cubiertas

## 🔧 Configuración JaCoCo

Agrega al `build.gradle.kts`:

```kotlin
plugins {
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    
    reports {
        xml.required = true
        html.required = true
    }
    
    executionData.setFrom(
        fileTree(layout.buildDirectory.dir("jacoco")).include("**/*.exec")
    )
    
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/R.class",
                    "**/R\$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/*_Hilt*.*",
                    "**/Hilt*.*"
                )
            }
        })
    )
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
```

## 📈 Generar Reporte de Cobertura

```bash
# Ejecutar tests y generar reporte
./gradlew test jacocoTestReport

# Ver reporte HTML
# Abrir: app/build/reports/jacoco/test/html/index.html
```

## ✅ Verificar Cobertura Mínima

```kotlin
tasks.jacocoTestReport {
    // ... configuración anterior ...
    
    doLast {
        val report = file("${reports.html.outputLocation.get()}/index.html")
        val html = report.readText()
        
        val coverageMatch = Regex("Total.*?([0-9]+)%").find(html)
        val coverage = coverageMatch?.groupValues?.get(1)?.toInt() ?: 0
        
        if (coverage < 80) {
            throw GradleException("Cobertura de tests es $coverage%, se requiere mínimo 80%")
        }
        
        println("✅ Cobertura de tests: $coverage%")
    }
}
```

## 🎯 Tips para Aumentar Cobertura

1. **Testear casos exitosos y de error**
2. **Testear casos límite (null, empty, etc.)**
3. **Mockear dependencias externas**
4. **Testear lógica de negocio en Use Cases**
5. **Testear transformaciones de datos en Repositories**

