# Solución de Errores de Compilación en el IDE

## 🔴 Problemas Detectados

Los errores que estás viendo indican que:

1. **Java 8 está instalado pero el proyecto requiere Java 21**
2. **Maven no está configurado o no está en el PATH**
3. **Las dependencias de Maven no se han descargado**
4. **El IDE no tiene configurado el JDK correcto para el proyecto**

## ✅ Solución Paso a Paso

### Paso 1: Instalar Java 21

1. Descarga Java 21 JDK desde: https://www.oracle.com/java/technologies/downloads/#java21
   - O usa OpenJDK: https://adoptium.net/temurin/releases/?version=21

2. Instala Java 21

3. Verifica la instalación:
```bash
java -version
```
Debe mostrar: `java version "21.x.x"` o similar

4. Configura la variable de entorno `JAVA_HOME`:
   - Windows: `JAVA_HOME=C:\Program Files\Java\jdk-21` (ajusta la ruta)
   - Agrega `%JAVA_HOME%\bin` al PATH

### Paso 2: Instalar Maven

1. Descarga Maven desde: https://maven.apache.org/download.cgi
   - Descarga el archivo `apache-maven-x.x.x-bin.zip`

2. Extrae Maven a una carpeta (ej: `C:\Program Files\Apache\maven`)

3. Configura las variables de entorno:
   - `MAVEN_HOME=C:\Program Files\Apache\maven` (ajusta la ruta)
   - Agrega `%MAVEN_HOME%\bin` al PATH

4. Verifica la instalación:
```bash
mvn -version
```

### Paso 3: Configurar el IDE (Eclipse/IntelliJ/VS Code)

#### Si usas Eclipse/Spring Tool Suite:

1. Abre **Window > Preferences > Java > Installed JREs**
2. Haz click en **Add...**
3. Selecciona **Standard VM** y Next
4. Browse a la carpeta donde instalaste Java 21
5. Click **Finish** y marca Java 21 como default
6. Click **Apply and Close**

7. Para cada proyecto:
   - Click derecho en el proyecto > **Properties**
   - **Java Build Path > Libraries**
   - Remueve "JRE System Library [JavaSE-1.8]"
   - Click **Add Library > JRE System Library**
   - Selecciona Java 21
   - Click **Apply and Close**

8. Click derecho en cada proyecto > **Maven > Update Project**
   - Marca "Force Update of Snapshots/Releases"
   - Click **OK**

#### Si usas IntelliJ IDEA:

1. **File > Project Structure** (Ctrl+Alt+Shift+S)
2. **Project Settings > Project**
   - **SDK:** Selecciona Java 21
   - **Language Level:** 21
3. **Modules:**
   - Selecciona cada módulo
   - **SDK:** Java 21
4. Click **OK**

5. **File > Settings > Build, Execution, Deployment > Build Tools > Maven**
   - **Maven home path:** Ruta a Maven instalado
   - Click **OK**

6. Click derecho en cada módulo > **Maven > Reload Project**

#### Si usas VS Code:

1. Abre Command Palette (Ctrl+Shift+P)
2. Busca: **Java: Configure Java Runtime**
3. Selecciona Java 21
4. En cada proyecto, busca: **Java: Clean Java Language Server Workspace**
5. Reinicia VS Code

### Paso 4: Descargar Dependencias de Maven

Abre una terminal en la carpeta raíz del proyecto y ejecuta:

```bash
# Para cada microservicio, ejecuta:
cd user-management-service
mvn clean install -DskipTests
cd ..

cd book-catalog-service
mvn clean install -DskipTests
cd ..

cd loan-management-service
mvn clean install -DskipTests
cd ..

cd reports-service
mvn clean install -DskipTests
cd ..

cd notifications-service
mvn clean install -DskipTests
cd ..
```

O en PowerShell (ejecuta todo de una vez):

```powershell
$services = @("user-management-service", "book-catalog-service", "loan-management-service", "reports-service", "notifications-service")
foreach ($service in $services) {
    Write-Host "Compilando $service..."
    cd $service
    mvn clean install -DskipTests
    cd ..
}
```

### Paso 5: Actualizar Proyectos en el IDE

**Eclipse:**
- Click derecho en cada proyecto > **Maven > Update Project**
- Marca todos los proyectos
- Marca "Force Update of Snapshots/Releases"
- Click **OK**

**IntelliJ:**
- Click derecho en cada módulo > **Maven > Reload Project**

**VS Code:**
- Abre Command Palette
- **Java: Clean Java Language Server Workspace**
- Reinicia VS Code

### Paso 6: Limpiar y Recompilar

En el IDE:

**Eclipse:**
- **Project > Clean...**
- Selecciona todos los proyectos
- Click **Clean**

**IntelliJ:**
- **Build > Rebuild Project**

**VS Code:**
- **Java: Clean Java Language Server Workspace**
- Reinicia VS Code

## 🔍 Verificación

Después de estos pasos, los errores deberían desaparecer. Verifica:

1. ✅ No hay errores de "cannot be resolved" en tipos básicos de Java
2. ✅ Las dependencias de Spring Boot se resuelven correctamente
3. ✅ Las clases JWT se importan sin errores
4. ✅ El proyecto compila sin errores

## ⚠️ Si los Errores Persisten

1. **Verifica que Java 21 esté configurado:**
   ```bash
   java -version  # Debe mostrar versión 21
   ```

2. **Verifica Maven:**
   ```bash
   mvn -version  # Debe mostrar Maven instalado
   ```

3. **Limpia el caché de Maven:**
   ```bash
   mvn dependency:purge-local-repository
   ```

4. **Elimina las carpetas target:**
   - Elimina la carpeta `target` de cada microservicio
   - Vuelve a ejecutar `mvn clean install`

5. **Reimporta el proyecto en el IDE:**
   - Cierra el IDE
   - Elimina las carpetas `.metadata`, `.settings`, `.classpath`, `.project` (solo en Eclipse)
   - Abre el proyecto de nuevo

## 📝 Notas Importantes

- **El proyecto requiere Java 21**, no funcionará con Java 8 o versiones anteriores
- **Maven debe estar instalado** y configurado correctamente
- **El IDE debe usar el JDK correcto** para cada proyecto
- **Las dependencias deben descargarse** antes de que el IDE pueda resolver las clases




