# 📱 Aplicación Android - Sistema de Biblioteca Digital

## 📋 Descripción del Proyecto

Aplicación móvil Android desarrollada en Kotlin que se conecta con un sistema de microservicios backend desarrollado en Spring Boot. La aplicación permite gestionar usuarios, consultar catálogo de libros, realizar préstamos, recibir notificaciones y visualizar reportes.

## 🏗️ Arquitectura

### Patrón Arquitectónico: MVVM (Model-View-ViewModel)

```
app/
├── data/
│   ├── local/          # Base de datos local (Room)
│   └── remote/         # APIs y servicios remotos
│       └── dto/        # Data Transfer Objects
├── domain/             # Lógica de negocio
│   ├── model/         # Modelos de dominio
│   └── usecase/       # Casos de uso
├── presentation/       # Capa de presentación
│   ├── ui/            # Activities, Fragments
│   └── viewmodel/     # ViewModels
└── di/                # Inyección de dependencias (Hilt)
```

### Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación
- **Retrofit** - Cliente HTTP para APIs REST
- **Hilt** - Inyección de dependencias
- **Coroutines** - Programación asíncrona
- **Room** - Base de datos local
- **Navigation Component** - Navegación entre pantallas
- **LiveData/Flow** - Observables reactivos
- **Glide** - Carga de imágenes

## 🔌 Integración con Microservicios

### Microservicios Conectados

1. **User Management Service** (Puerto 8081)
   - Autenticación y registro de usuarios
   - Gestión de perfiles
   - Validación de tokens JWT

2. **Book Catalog Service** (Puerto 8082)
   - Catálogo de libros
   - Búsqueda y filtros
   - Disponibilidad de libros

3. **Loan Management Service** (Puerto 8083)
   - Creación de préstamos
   - Gestión de devoluciones
   - Extensión de préstamos

4. **Reports Service** (Puerto 8084)
   - Estadísticas del dashboard
   - Reportes personalizados

5. **Notifications Service** (Puerto 8085)
   - Notificaciones push
   - Alertas de préstamos
   - Contador de no leídas

### APIs Externas

- **Google Books API** - Para obtener información adicional de libros
- **Open Library API** - Para portadas de libros

## 📦 Instalación y Configuración

### Requisitos Previos

- Android Studio Hedgehog o superior
- JDK 17 o superior
- Android SDK 24+ (mínimo) / 34 (target)
- Gradle 8.0+

### Pasos de Instalación

1. **Clonar o importar el proyecto**
   ```bash
   git clone [url-del-repositorio]
   cd uinavegacion
   ```

2. **Configurar URLs de microservicios**

   Edita `build.gradle.kts` y ajusta las URLs:
   ```kotlin
   buildConfigField("String", "BASE_URL_USER", "\"http://10.0.2.2:8081\"")
   ```
   
   - **Emulador:** `http://10.0.2.2:808X`
   - **Dispositivo físico:** `http://TU_IP:808X` (IP de tu PC en la red local)

3. **Generar Keystore para Release**
   ```powershell
   .\generate-keystore.ps1
   ```

4. **Sincronizar proyecto**
   - File > Sync Project with Gradle Files

5. **Ejecutar aplicación**
   - Click en Run o `Shift+F10`

## 🧪 Pruebas Unitarias

### Ejecutar Tests

```bash
# Todos los tests
./gradlew test

# Tests específicos
./gradlew test --tests "com.example.uinavegacion.*Test"

# Con cobertura
./gradlew test jacocoTestReport
```

### Cobertura de Tests

- **ViewModels:** > 80%
- **Repositories:** > 75%
- **Use Cases:** > 70%
- **Servicios API:** > 70%

### Estructura de Tests

```
test/
├── java/com/example/uinavegacion/
│   ├── data/
│   │   └── remote/
│   │       └── dto/        # Tests de DTOs
│   ├── domain/
│   │   └── usecase/        # Tests de casos de uso
│   └── presentation/
│       └── viewmodel/      # Tests de ViewModels
```

## 📦 Generación de APK Firmado

### Modo Debug

```bash
./gradlew assembleDebug
```

APK generado en: `app/build/outputs/apk/debug/app-debug.apk`

### Modo Release (Firmado)

1. **Generar keystore** (si no existe):
   ```powershell
   .\generate-keystore.ps1
   ```

2. **Generar APK Release**:
   ```powershell
   .\generate-apk-release.ps1
   ```

   O manualmente:
   ```bash
   ./gradlew assembleRelease
   ```

3. **APK generado en:**
   `app/build/outputs/apk/release/app-release.apk`

### Configuración de Firma

El keystore se configura en `build.gradle.kts`:

```kotlin
signingConfigs {
    create("release") {
        storeFile = file("../keystore/library-release.jks")
        storePassword = "library123"
        keyAlias = "library-key"
        keyPassword = "library123"
    }
}
```

**⚠️ IMPORTANTE:** En producción, usa variables de entorno para las contraseñas.

## 📚 Documentación de APIs

### User Management API

#### POST /api/users/register
Registra un nuevo usuario.

**Request:**
```json
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123",
  "phone": "123456789"
}
```

**Response:** `UserDto`

#### POST /api/users/login
Autentica un usuario.

**Request:**
```json
{
  "email": "juan@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": { ... },
  "expiresIn": 86400000
}
```

### Book Catalog API

#### GET /api/books
Obtiene lista paginada de libros.

**Query Parameters:**
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 10)
- `sortBy`: Campo de ordenamiento (default: "title")
- `sortDir`: Dirección (ASC/DESC)

**Response:** `BookPageResponseDto`

#### GET /api/books/search?q={query}
Busca libros por título, autor o ISBN.

### Loan Management API

#### POST /api/loans
Crea un nuevo préstamo.

**Headers:**
- `Authorization: Bearer {token}`

**Request:**
```json
{
  "userId": 1,
  "bookId": 1,
  "loanDays": 14
}
```

## 🔐 Autenticación JWT

La aplicación utiliza tokens JWT para autenticación:

1. **Login:** Usuario se autentica y recibe token
2. **Almacenamiento:** Token se guarda en SharedPreferences o DataStore
3. **Uso:** Token se incluye en header `Authorization: Bearer {token}`
4. **Renovación:** Token se valida antes de cada petición

## 📊 Diagramas

### Flujo de Autenticación

```
Login Screen
    ↓
[POST /api/users/login]
    ↓
[Guardar Token]
    ↓
[Navegar a Home]
    ↓
[Usar Token en peticiones]
```

### Flujo de Préstamo

```
Catálogo de Libros
    ↓
[Seleccionar Libro]
    ↓
[Verificar Disponibilidad]
    ↓
[Crear Préstamo]
    ↓
[POST /api/loans]
    ↓
[Mostrar Confirmación]
```

## 🐛 Solución de Problemas

### Error de Conexión

**Problema:** No se puede conectar a los microservicios.

**Solución:**
- Verifica que los servicios estén corriendo
- Para emulador: usa `10.0.2.2` en lugar de `localhost`
- Para dispositivo físico: usa la IP de tu PC
- Verifica permisos de Internet en AndroidManifest.xml

### Error de Autenticación

**Problema:** Token inválido o expirado.

**Solución:**
- Verifica que el token se esté guardando correctamente
- Implementa renovación automática de token
- Valida token antes de cada petición

### Error de Compilación

**Problema:** Errores al compilar.

**Solución:**
- Sync Project with Gradle Files
- Clean Project
- Invalidate Caches / Restart

## 📝 Changelog

### Versión 1.0.0
- ✅ Integración con 5 microservicios
- ✅ Autenticación JWT
- ✅ Gestión de préstamos
- ✅ Sistema de notificaciones
- ✅ Reportes y estadísticas
- ✅ Pruebas unitarias
- ✅ APK release firmado

## 👥 Autores

- [Tu Nombre]
- Sistema de Biblioteca Digital - EFT

## 📄 Licencia

Este proyecto es de uso educativo.

---

**Última actualización:** 2024



