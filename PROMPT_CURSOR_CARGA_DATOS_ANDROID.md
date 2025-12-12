# 📱 Prompt para Cursor: Cargar Datos desde Android a Microservicios

## 🎯 Objetivo

Implementar la funcionalidad para que la aplicación Android cargue automáticamente los datos iniciales (34 libros y 2 usuarios) desde un archivo JSON en assets hacia los microservicios backend.

## 📋 Contexto

Tienes un archivo JSON `seed-data-for-microservices.json` con:
- **34 libros** con información completa (título, autor, ISBN, categoría, editorial, año, descripción, URL de portada, stock, etc.)
- **2 usuarios** de demostración (Demo User y Admin User)

Los microservicios ya tienen endpoints configurados:
- `POST /api/users/bulk` - Carga masiva de usuarios
- `POST /api/books/bulk` - Carga masiva de libros

## 🔧 Implementación Requerida

### 1. Agregar archivo JSON a Assets

1. Crea la carpeta `assets` si no existe:
   - `app/src/main/assets/`

2. Coloca el archivo `seed-data-for-microservices.json` en esa carpeta

3. El JSON tiene esta estructura:
```json
{
  "books": [
    {
      "id": 1,
      "title": "1984",
      "author": "George Orwell",
      "isbn": "9788497593793",
      "categoria": "Ciencia",
      "categoryId": 1,
      "publisher": "Debolsillo",
      "publishDate": "1949-06-08",
      "anio": 1949,
      "status": "Available",
      "inventoryCode": "C001",
      "stock": 5,
      "disponibles": 5,
      "descripcion": "Una distopía clásica...",
      "coverUrl": "https://...",
      "homeSection": "Trending"
    },
    // ... más libros
  ],
  "users": [
    {
      "name": "Demo User",
      "email": "demo@duoc.cl",
      "phone": "912345678",
      "password": "Demo123!",
      "role": "USER"
    },
    {
      "name": "Admin User",
      "email": "admin123@gmail.com",
      "phone": "000000000",
      "password": "admin12345678",
      "role": "ADMIN"
    }
  ]
}
```

### 2. Usar el DataLoadService existente

Ya existe un servicio `DataLoadService.kt` en:
- `app/src/main/java/com/library/app/data/remote/service/DataLoadService.kt`

Este servicio:
- ✅ Lee el JSON desde assets
- ✅ Parsea los datos
- ✅ Convierte el formato del JSON al formato de los DTOs
- ✅ Llama a los endpoints de bulk load
- ✅ Retorna estadísticas de la carga

### 3. Crear un ViewModel o UseCase para la carga

Crea un ViewModel o UseCase que:
1. Obtenga el InputStream del archivo JSON desde assets
2. Llame al `DataLoadService.loadAllData()`
3. Muestre el progreso y resultado al usuario

**Ejemplo de código:**

```kotlin
class DataLoadViewModel @Inject constructor(
    private val dataLoadService: DataLoadService,
    private val application: Application
) : ViewModel() {
    
    private val _loadResult = MutableStateFlow<DataLoadService.LoadResult?>(null)
    val loadResult: StateFlow<DataLoadService.LoadResult?> = _loadResult
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val inputStream = application.assets.open("seed-data-for-microservices.json")
                val result = dataLoadService.loadAllData(inputStream)
                _loadResult.value = result
            } catch (e: Exception) {
                _loadResult.value = DataLoadService.LoadResult(
                    usersLoaded = 0,
                    usersSkipped = 0,
                    booksLoaded = 0,
                    booksSkipped = 0,
                    success = false,
                    message = "Error: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
```

### 4. Agregar botón en la UI

Agrega un botón en la pantalla principal o en una pantalla de configuración que:
- Llame al método `loadInitialData()` del ViewModel
- Muestre un diálogo de progreso mientras carga
- Muestre el resultado (cuántos usuarios y libros se cargaron)

**Ejemplo de UI:**

```kotlin
// En tu Activity/Fragment
viewModel.loadResult.collect { result ->
    result?.let {
        if (it.success) {
            showSuccessDialog(
                "Datos cargados exitosamente",
                "Usuarios: ${it.usersLoaded} nuevos, ${it.usersSkipped} ya existían\n" +
                "Libros: ${it.booksLoaded} nuevos, ${it.booksSkipped} ya existían"
            )
        } else {
            showErrorDialog("Error", it.message)
        }
    }
}
```

### 5. Mapeo de campos del JSON a DTOs

El `DataLoadService` ya hace el mapeo, pero verifica que:
- `categoria` → `category`
- `anio` → `year`
- `stock` → `totalCopies`
- `disponibles` → `availableCopies` (se calcula automáticamente)
- `descripcion` → `description`
- `homeSection` → `featured` (si es "Trending" o "Free" = true)

### 6. Configuración de Dependencias

Asegúrate de que `DataLoadService` esté inyectado correctamente en tu módulo de Dependencias (Dagger/Hilt):

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DataLoadModule {
    @Provides
    @Singleton
    fun provideDataLoadService(
        bookApiService: BookApiService,
        userApiService: UserApiService,
        gson: Gson
    ): DataLoadService {
        return DataLoadService(bookApiService, userApiService, gson)
    }
}
```

## ✅ Checklist de Implementación

- [ ] Archivo JSON en `app/src/main/assets/seed-data-for-microservices.json`
- [ ] ViewModel/UseCase creado para manejar la carga
- [ ] Botón en la UI para iniciar la carga
- [ ] Diálogo de progreso mientras carga
- [ ] Mensaje de éxito/error al finalizar
- [ ] Verificar que los endpoints `/api/users/bulk` y `/api/books/bulk` estén accesibles
- [ ] Probar la carga y verificar que los datos aparezcan en los microservicios

## 🔍 Verificación

Después de cargar los datos, verifica:
1. En Swagger de User Service (`http://localhost:8081/swagger-ui.html`):
   - GET `/api/users` - Debe mostrar 2 usuarios
2. En Swagger de Book Service (`http://localhost:8082/swagger-ui.html`):
   - GET `/api/books?size=100` - Debe mostrar los 34 libros

## 📝 Notas Importantes

1. **Contraseñas**: Las contraseñas en el JSON están en texto plano, pero el backend las encripta automáticamente con BCrypt
2. **Duplicados**: Si un usuario/libro ya existe (por email/ISBN), se omite automáticamente
3. **Roles**: El backend detecta automáticamente si un usuario es admin basándose en el email
4. **URLs de imágenes**: Algunos libros (21-34) tienen `coverUrl` vacío, esto es normal

## 🚀 Uso

Una vez implementado, el usuario puede:
1. Abrir la app Android
2. Presionar el botón "Cargar Datos Iniciales"
3. Esperar a que se complete la carga
4. Ver el resultado con estadísticas

¡Listo! Los datos estarán disponibles en los microservicios para usar en la aplicación.







