# 📱 Biblioteca Digital - Android App

## 🚀 Configuración Inicial

### 1. Requisitos
- Android Studio Hedgehog o superior
- JDK 17 o superior
- Android SDK 24+ (mínimo), 34 (target)
- Kotlin 1.9.20+

### 2. Instalación

1. Abre el proyecto en Android Studio
2. Sincroniza Gradle (Sync Now)
3. Espera a que se descarguen las dependencias

### 3. Configuración de URLs

**Para Emulador Android:**
Las URLs ya están configuradas en `NetworkModule.kt`:
- `http://10.0.2.2:8081` (User Service)
- `http://10.0.2.2:8082` (Book Service)
- etc.

**Para Dispositivo Físico:**
1. Encuentra la IP de tu máquina: `ipconfig` (Windows) o `ifconfig` (Mac/Linux)
2. Actualiza las URLs en `NetworkModule.kt`:
```kotlin
private const val BASE_URL_USER = "http://192.168.1.100:8081/"
```

### 4. Generar Keystore (para APK Release)

```bash
keytool -genkey -v -keystore keystore/library-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias library-key
```

O usa el script PowerShell: `generate-keystore.ps1`

## 📁 Estructura del Proyecto

```
app/
├── src/main/java/com/library/app/
│   ├── data/
│   │   ├── local/
│   │   │   └── TokenManager.kt          # Gestión de tokens
│   │   ├── remote/
│   │   │   ├── api/                      # Interfaces Retrofit
│   │   │   │   ├── UserApiService.kt
│   │   │   │   ├── BookApiService.kt
│   │   │   │   ├── LoanApiService.kt
│   │   │   │   ├── NotificationApiService.kt
│   │   │   │   └── ReportsApiService.kt
│   │   │   ├── dto/                      # Data Transfer Objects
│   │   │   │   ├── UserDTOs.kt
│   │   │   │   ├── BookDTOs.kt
│   │   │   │   ├── LoanDTOs.kt
│   │   │   │   ├── NotificationDTOs.kt
│   │   │   │   └── ReportsDTOs.kt
│   │   │   └── AuthInterceptor.kt        # Interceptor JWT
│   │   ├── repository/                   # Repositorios
│   │   └── model/                        # Modelos de dominio
│   ├── domain/
│   │   ├── usecase/                      # Casos de uso
│   │   └── repository/                   # Interfaces de repositorio
│   ├── ui/
│   │   ├── theme/                        # Tema de Compose
│   │   ├── screens/                      # Pantallas
│   │   └── components/                   # Componentes reutilizables
│   ├── di/                               # Dependency Injection (Hilt)
│   │   ├── NetworkModule.kt
│   │   └── AppModule.kt
│   └── LibraryApplication.kt
```

## 🔧 Uso Básico

### Ejemplo: Login

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.login(email, password)
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        tokenManager.saveToken(loginResponse.token)
                        tokenManager.saveUserInfo(loginResponse.user.id, loginResponse.user.email)
                        // Navegar a pantalla principal
                    }
                } else {
                    // Manejar error
                }
            } catch (e: Exception) {
                // Manejar excepción
            }
        }
    }
}
```

### Ejemplo: Obtener Libros

```kotlin
@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _books = MutableStateFlow<List<BookResponseDTO>>(emptyList())
    val books: StateFlow<List<BookResponseDTO>> = _books.asStateFlow()

    fun loadBooks() {
        viewModelScope.launch {
            try {
                val response = bookRepository.getAllBooks(page = 0, size = 20)
                if (response.isSuccessful) {
                    _books.value = response.body()?.content ?: emptyList()
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
```

## 🔐 Autenticación

El `AuthInterceptor` agrega automáticamente el token JWT a todas las peticiones (excepto login/register).

El token se almacena de forma segura usando DataStore.

## 📦 Dependencias Principales

- **Retrofit 2.9.0** - Cliente HTTP
- **OkHttp 4.12.0** - Cliente HTTP subyacente
- **Gson** - Serialización JSON
- **Hilt** - Dependency Injection
- **Coroutines** - Programación asíncrona
- **Compose** - UI moderna
- **DataStore** - Almacenamiento de preferencias
- **Coil** - Carga de imágenes

## 🧪 Testing

Ejecutar tests:
```bash
./gradlew test
```

Generar reporte de cobertura:
```bash
./gradlew jacocoTestReport
```

## 📱 Generar APK Release

1. Configurar keystore (ver arriba)
2. Ejecutar:
```bash
./gradlew assembleRelease
```

El APK estará en: `app/build/outputs/apk/release/app-release.apk`

## ⚠️ Notas Importantes

1. **Permisos de Internet**: Ya están en el AndroidManifest
2. **Cleartext Traffic**: Habilitado para desarrollo local (deshabilitar en producción)
3. **Token Storage**: Usa DataStore (seguro) en lugar de SharedPreferences
4. **Error Handling**: Implementa manejo de errores en todos los ViewModels
5. **Loading States**: Muestra estados de carga en la UI

## 🔗 URLs de los Microservicios

- User Management: `http://10.0.2.2:8081`
- Book Catalog: `http://10.0.2.2:8082`
- Loan Management: `http://10.0.2.2:8083`
- Reports: `http://10.0.2.2:8084`
- Notifications: `http://10.0.2.2:8085`

## 📚 Documentación Adicional

Ver carpeta `documentacion/` para:
- Guías de testing
- Integración con APIs externas
- Checklist de entrega
- Requisitos EFT


