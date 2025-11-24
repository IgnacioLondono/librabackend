# Configuración de APIs REST para Android Studio

## 📦 Archivos Creados

1. **postman/Library_Microservices.postman_collection.json** - Colección de Postman
2. **android-studio/ApiConfig.kt** - Configuración de URLs y endpoints
3. **android-studio/ApiService.kt** - Interfaces Retrofit
4. **android-studio/DataModels.kt** - Modelos de datos
5. **android-studio/RetrofitClient.kt** - Cliente Retrofit configurado
6. **android-studio/build.gradle** - Dependencias necesarias
7. **android-studio/AndroidManifest.xml** - Permisos requeridos
8. **android-studio/EjemploUso.kt** - Ejemplos de uso

## 🚀 Pasos para Configurar en Android Studio

### 1. Agregar Dependencias

En tu `build.gradle (Module: app)`, agrega:

```gradle
dependencies {
    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // OkHttp
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    
    // Gson
    implementation 'com.google.code.gson:gson:2.10.1'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
}
```

### 2. Configurar AndroidManifest.xml

Agrega permisos:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

Y en `<application>`:

```xml
<application
    android:usesCleartextTraffic="true"
    ...>
```

### 3. Copiar Archivos a tu Proyecto

Copia estos archivos a tu proyecto Android:

- `ApiConfig.kt` → `app/src/main/java/com/tu/paquete/api/`
- `ApiService.kt` → `app/src/main/java/com/tu/paquete/api/`
- `DataModels.kt` → `app/src/main/java/com/tu/paquete/api/`
- `RetrofitClient.kt` → `app/src/main/java/com/tu/paquete/api/`

### 4. Configurar URLs

En `ApiConfig.kt`, ajusta las URLs:

**Para Emulador Android:**
```kotlin
const val BASE_URL_USER = "http://10.0.2.2:8081"
```

**Para Dispositivo Físico:**
```kotlin
const val BASE_URL_USER = "http://192.168.1.X:8081" // IP de tu PC
```

Para encontrar tu IP:
```bash
ipconfig  # Windows
ifconfig  # Linux/Mac
```

### 5. Usar las APIs

```kotlin
// Ejemplo en un ViewModel o Activity
lifecycleScope.launch {
    try {
        // Login
        val loginResponse = RetrofitClient.userApiService.login(
            LoginRequest("email@example.com", "password")
        )
        
        if (loginResponse.isSuccessful) {
            val token = loginResponse.body()?.token
            // Guardar token
        }
        
        // Obtener libros
        val booksResponse = RetrofitClient.bookApiService.getAllBooks()
        val books = booksResponse.body()?.content
        
        // Crear préstamo (con token)
        val loanResponse = RetrofitClient.loanApiService.createLoan(
            CreateLoanRequest(userId = 1, bookId = 1),
            token = "Bearer $token"
        )
        
    } catch (e: Exception) {
        // Manejar error
    }
}
```

## 📱 Importar Colección de Postman

1. Abre Postman
2. Click en **Import**
3. Selecciona `postman/Library_Microservices.postman_collection.json`
4. La colección se importará con todos los endpoints

## 🔑 Variables de Postman

Configura estas variables en Postman:
- `token` - Token JWT después de login
- `userId` - ID del usuario actual
- `bookId` - ID del libro
- `loanId` - ID del préstamo

## ⚠️ Notas Importantes

1. **Emulador:** Usa `10.0.2.2` en lugar de `localhost`
2. **Dispositivo Físico:** Usa la IP de tu PC en la misma red WiFi
3. **Token JWT:** Guárdalo después del login y úsalo en todas las peticiones autenticadas
4. **CORS:** Si tienes problemas, verifica que los servicios permitan requests desde Android




