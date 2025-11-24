# 🚀 Instrucciones Rápidas para Arreglar el IDE

## ✅ Ya está configurado automáticamente

He creado los archivos de configuración necesarios. Ahora solo necesitas:

### Paso 1: Instalar Java 21 (OBLIGATORIO)

El proyecto **requiere Java 21**. No funcionará con Java 8.

**Descarga e instala:**
- **Oracle JDK 21:** https://www.oracle.com/java/technologies/downloads/#java21
- **O OpenJDK 21:** https://adoptium.net/temurin/releases/?version=21

**Después de instalar:**
1. Configura la variable de entorno `JAVA_HOME`:
   - `JAVA_HOME=C:\Program Files\Java\jdk-21` (ajusta la ruta)
2. Agrega al PATH: `%JAVA_HOME%\bin`

### Paso 2: Instalar Maven (OBLIGATORIO)

**Descarga e instala:**
- https://maven.apache.org/download.cgi
- Extrae a: `C:\Program Files\Apache\maven`
- Configura variables:
  - `MAVEN_HOME=C:\Program Files\Apache\maven`
  - PATH: `%MAVEN_HOME%\bin`

### Paso 3: Configurar el IDE

#### Si usas Eclipse/Spring Tool Suite:

1. **Window > Preferences > Java > Installed JREs**
   - Click **Add...**
   - **Standard VM** > Next
   - Browse a Java 21
   - **Finish** y marca como default

2. **File > Import > Existing Maven Projects**
   - Selecciona la carpeta `libra_services`
   - Marca todos los proyectos
   - Click **Finish**

3. **Click derecho en cada proyecto > Maven > Update Project**
   - Marca todos
   - Marca "Force Update of Snapshots/Releases"
   - Click **OK**

4. **Project > Clean...**
   - Selecciona todos los proyectos
   - Click **Clean**

#### Si usas IntelliJ IDEA:

1. **File > Project Structure** (Ctrl+Alt+Shift+S)
   - **Project > SDK:** Selecciona Java 21
   - **Language Level:** 21

2. **File > Open**
   - Selecciona la carpeta `libra_services`
   - Marca "Import Maven projects automatically"

3. **File > Settings > Build Tools > Maven**
   - Configura Maven home path

4. **Click derecho en cada módulo > Maven > Reload Project**

#### Si usas VS Code:

1. Instala la extensión "Extension Pack for Java"
2. **Ctrl+Shift+P > Java: Configure Java Runtime**
   - Selecciona Java 21
3. **Ctrl+Shift+P > Java: Clean Java Language Server Workspace**
4. Reinicia VS Code

### Paso 4: Verificar

Después de configurar, los errores deberían desaparecer. Si persisten:

```powershell
# Verifica Java
java -version  # Debe mostrar versión 21

# Verifica Maven
mvn -version  # Debe mostrar Maven instalado
```

## 📝 Nota Importante

**NO puedes usar Java 8** con este proyecto. Spring Boot 3.2.0 requiere Java 17 como mínimo, y el proyecto está configurado para Java 21.

Si no puedes instalar Java 21 ahora, puedes temporalmente cambiar en todos los `pom.xml`:
- `java.version` de `21` a `17` (mínimo requerido)
- Pero **recomiendo usar Java 21** como está especificado.

## 🔧 Archivos Creados

He creado automáticamente:
- ✅ `.settings/org.eclipse.jdt.core.prefs` (configuración Java 21)
- ✅ `.settings/org.eclipse.m2e.core.prefs` (configuración Maven)
- ✅ `.settings/org.eclipse.core.resources.prefs` (encoding UTF-8)
- ✅ `fix-ide-config.ps1` (script de configuración)

Estos archivos ya están en cada microservicio.




